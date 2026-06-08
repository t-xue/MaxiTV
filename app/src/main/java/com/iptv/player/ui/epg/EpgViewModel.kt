package com.iptv.player.ui.epg

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iptv.player.data.repository.ChannelRepository
import com.iptv.player.data.repository.EpgRepository
import com.iptv.player.domain.model.Channel
import com.iptv.player.domain.model.EpgProgram
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

data class EpgUiState(
    val channels: List<ChannelEpgInfo> = emptyList(),
    val selectedDate: LocalDate = LocalDate.now(),
    val isLoading: Boolean = true,
    val error: String? = null
)

data class ChannelEpgInfo(
    val channel: Channel,
    val currentProgram: EpgProgram?,
    val programs: List<EpgProgram>
)

@HiltViewModel
class EpgViewModel @Inject constructor(
    private val channelRepository: ChannelRepository,
    private val epgRepository: EpgRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(EpgUiState())
    val uiState: StateFlow<EpgUiState> = _uiState.asStateFlow()

    init {
        loadEpgData()
    }

    private fun loadEpgData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            try {
                // 获取所有频道
                channelRepository.getAllChannels().collect { channels ->
                    val channelEpgInfoList = channels.mapNotNull { channel ->
                        channel.tvgId?.let { tvgId ->
                            val currentProgram = epgRepository.getCurrentProgram(tvgId)
                            ChannelEpgInfo(
                                channel = channel,
                                currentProgram = currentProgram,
                                programs = emptyList() // 可以按需加载
                            )
                        }
                    }

                    _uiState.value = _uiState.value.copy(
                        channels = channelEpgInfoList,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "加载 EPG 数据失败"
                )
            }
        }
    }

    /**
     * 切换日期
     */
    fun selectDate(date: LocalDate) {
        _uiState.value = _uiState.value.copy(selectedDate = date)
        // 重新加载该日期的 EPG 数据
    }

    /**
     * 清除错误
     */
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}
