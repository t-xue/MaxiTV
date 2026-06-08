package com.iptv.player.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iptv.player.data.repository.EpgRepository
import com.iptv.player.data.repository.PlaylistRepository
import com.iptv.player.domain.model.Playlist
import com.iptv.player.ui.theme.ThemeManager
import com.iptv.player.ui.theme.ThemeMode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SettingsUiState(
    val playlists: List<Playlist> = emptyList(),
    val epgUrl: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val successMessage: String? = null
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val playlistRepository: PlaylistRepository,
    private val epgRepository: EpgRepository,
    private val themeManager: ThemeManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    // 主题模式状态
    val themeMode: StateFlow<ThemeMode> = themeManager.themeMode
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ThemeMode.SYSTEM
        )

    init {
        loadPlaylists()
    }

    private fun loadPlaylists() {
        viewModelScope.launch {
            playlistRepository.getAllPlaylists().collect { playlists ->
                _uiState.value = _uiState.value.copy(playlists = playlists)
            }
        }
    }

    /**
     * 设置主题模式
     */
    fun setThemeMode(mode: ThemeMode) {
        viewModelScope.launch {
            themeManager.setThemeMode(mode)
        }
    }

    /**
     * 从 URL 导入播放列表
     */
    fun importPlaylist(url: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            val result = playlistRepository.importFromUrl(url)
            result.fold(
                onSuccess = { count ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        successMessage = "导入成功，共 $count 个频道"
                    )
                },
                onFailure = { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = exception.message ?: "导入失败"
                    )
                }
            )
        }
    }

    /**
     * 刷新播放列表
     */
    fun refreshPlaylist(playlistId: Long) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            val result = playlistRepository.refreshPlaylist(playlistId)
            result.fold(
                onSuccess = { count ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        successMessage = "刷新成功，共 $count 个频道"
                    )
                },
                onFailure = { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = exception.message ?: "刷新失败"
                    )
                }
            )
        }
    }

    /**
     * 删除播放列表
     */
    fun deletePlaylist(playlistId: Long) {
        viewModelScope.launch {
            playlistRepository.deletePlaylist(playlistId)
            _uiState.value = _uiState.value.copy(
                successMessage = "播放列表已删除"
            )
        }
    }

    /**
     * 更新 EPG URL
     */
    fun updateEpgUrl(url: String) {
        _uiState.value = _uiState.value.copy(epgUrl = url)
    }

    /**
     * 同步 EPG 数据
     */
    fun syncEpg() {
        val url = _uiState.value.epgUrl
        if (url.isBlank()) {
            _uiState.value = _uiState.value.copy(error = "请输入 EPG URL")
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            val result = epgRepository.syncEpgFromUrl(url)
            result.fold(
                onSuccess = { count ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        successMessage = "EPG 同步成功，共 $count 条节目信息"
                    )
                },
                onFailure = { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = exception.message ?: "EPG 同步失败"
                    )
                }
            )
        }
    }

    /**
     * 清除消息
     */
    fun clearMessages() {
        _uiState.value = _uiState.value.copy(error = null, successMessage = null)
    }
}
