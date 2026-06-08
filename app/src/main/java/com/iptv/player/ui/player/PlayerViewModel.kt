package com.iptv.player.ui.player

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.iptv.player.data.repository.ChannelRepository
import com.iptv.player.data.repository.EpgRepository
import com.iptv.player.domain.model.Channel
import com.iptv.player.domain.model.EpgProgram
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PlayerUiState(
    val channel: Channel? = null,
    val currentProgram: EpgProgram? = null,
    val nextProgram: EpgProgram? = null,
    val isPlaying: Boolean = false,
    val isLoading: Boolean = true,
    val error: String? = null,
    val showChannelInfo: Boolean = false,
    val showControls: Boolean = true
)

@HiltViewModel
class PlayerViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    val exoPlayer: ExoPlayer,  // 改为 public，供 PlayerView 使用
    private val channelRepository: ChannelRepository,
    private val epgRepository: EpgRepository
) : ViewModel() {

    private val channelId: Long = savedStateHandle.get<Long>("channelId") ?: 0L

    private val _uiState = MutableStateFlow(PlayerUiState())
    val uiState: StateFlow<PlayerUiState> = _uiState.asStateFlow()

    // 所有频道列表 (用于切换)
    private var allChannels: List<Channel> = emptyList()
    private var currentChannelIndex: Int = -1

    init {
        setupPlayer()
        loadChannel()
        observeChannels()
    }

    private fun setupPlayer() {
        exoPlayer.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                when (playbackState) {
                    Player.STATE_BUFFERING -> {
                        _uiState.value = _uiState.value.copy(isLoading = true, error = null)
                    }
                    Player.STATE_READY -> {
                        _uiState.value = _uiState.value.copy(isLoading = false, isPlaying = true)
                    }
                    Player.STATE_IDLE -> {
                        _uiState.value = _uiState.value.copy(isLoading = false)
                    }
                    Player.STATE_ENDED -> {
                        _uiState.value = _uiState.value.copy(isPlaying = false)
                    }
                }
            }

            override fun onIsPlayingChanged(isPlaying: Boolean) {
                _uiState.value = _uiState.value.copy(isPlaying = isPlaying)
            }

            override fun onPlayerError(error: PlaybackException) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = error.message ?: "播放错误"
                )
            }
        })
    }

    private fun loadChannel() {
        viewModelScope.launch {
            val channel = channelRepository.getChannelById(channelId)
            if (channel != null) {
                _uiState.value = _uiState.value.copy(channel = channel)
                playChannel(channel)
                loadEpgInfo(channel)
            } else {
                _uiState.value = _uiState.value.copy(error = "频道不存在")
            }
        }
    }

    private fun observeChannels() {
        viewModelScope.launch {
            channelRepository.getAllChannels().collect { channels ->
                allChannels = channels
                currentChannelIndex = channels.indexOfFirst { it.id == channelId }
            }
        }
    }

    private fun playChannel(channel: Channel) {
        // 构建 MediaItem，添加自定义请求头
        val mediaItem = MediaItem.Builder()
            .setUri(channel.url)
            .setCustomCacheKey(channel.url)  // 使用 URL 作为缓存键
            .build()

        // 停止当前播放
        exoPlayer.stop()

        // 如果有自定义 User-Agent，需要设置请求头
        if (channel.userAgent != null || channel.httpReferer != null) {
            // 创建带有自定义请求头的 DataSourceFactory
            val headers = mutableMapOf<String, String>()
            channel.userAgent?.let { headers["User-Agent"] = it }
            channel.httpReferer?.let { headers["Referer"] = it }

            val dataSourceFactory = androidx.media3.datasource.DefaultHttpDataSource.Factory()
                .setUserAgent(channel.userAgent ?: "MaxiTV/1.0")
                .setDefaultRequestProperties(headers)

            val mediaSourceFactory = androidx.media3.exoplayer.source.DefaultMediaSourceFactory(dataSourceFactory)

            // 使用新的 MediaSourceFactory
            exoPlayer.setMediaSource(mediaSourceFactory.createMediaSource(mediaItem))
        } else {
            exoPlayer.setMediaItem(mediaItem)
        }

        exoPlayer.prepare()
        exoPlayer.play()

        // 显示频道信息叠加层
        showChannelInfo()
    }

    private fun loadEpgInfo(channel: Channel) {
        channel.tvgId?.let { tvgId ->
            viewModelScope.launch {
                val currentProgram = epgRepository.getCurrentProgram(tvgId)
                val nextProgram = epgRepository.getNextProgram(tvgId)
                _uiState.value = _uiState.value.copy(
                    currentProgram = currentProgram,
                    nextProgram = nextProgram
                )
            }
        }
    }

    /**
     * 切换到下一个频道
     */
    fun switchToNextChannel() {
        if (allChannels.isEmpty() || currentChannelIndex >= allChannels.size - 1) return

        currentChannelIndex++
        val nextChannel = allChannels[currentChannelIndex]
        _uiState.value = _uiState.value.copy(
            channel = nextChannel,
            currentProgram = null,
            nextProgram = null
        )
        playChannel(nextChannel)
        loadEpgInfo(nextChannel)
    }

    /**
     * 切换到上一个频道
     */
    fun switchToPreviousChannel() {
        if (allChannels.isEmpty() || currentChannelIndex <= 0) return

        currentChannelIndex--
        val previousChannel = allChannels[currentChannelIndex]
        _uiState.value = _uiState.value.copy(
            channel = previousChannel,
            currentProgram = null,
            nextProgram = null
        )
        playChannel(previousChannel)
        loadEpgInfo(previousChannel)
    }

    /**
     * 切换播放/暂停
     */
    fun togglePlayPause() {
        if (exoPlayer.isPlaying) {
            exoPlayer.pause()
        } else {
            exoPlayer.play()
        }
    }

    /**
     * 显示频道信息叠加层 (2 秒后自动隐藏)
     */
    fun showChannelInfo() {
        _uiState.value = _uiState.value.copy(showChannelInfo = true)
        viewModelScope.launch {
            delay(2000)
            _uiState.value = _uiState.value.copy(showChannelInfo = false)
        }
    }

    /**
     * 切换控件显示
     */
    fun toggleControls() {
        _uiState.value = _uiState.value.copy(
            showControls = !_uiState.value.showControls
        )
    }

    /**
     * 重试播放
     */
    fun retry() {
        _uiState.value = _uiState.value.copy(error = null)
        _uiState.value.channel?.let { playChannel(it) }
    }

    override fun onCleared() {
        super.onCleared()
        // 不释放 ExoPlayer（它是单例），只停止播放
        exoPlayer.stop()
        exoPlayer.clearMediaItems()
    }
}
