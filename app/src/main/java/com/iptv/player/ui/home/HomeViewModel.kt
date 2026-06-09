package com.iptv.player.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iptv.player.data.repository.ChannelRepository
import com.iptv.player.data.repository.CustomChannelRepository
import com.iptv.player.data.repository.PlaylistRepository
import com.iptv.player.domain.model.Channel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val channels: List<Channel> = emptyList(),
    val groups: List<String> = emptyList(),
    val selectedGroup: String? = null, // null 表示全部
    val searchQuery: String = "",
    val isLoading: Boolean = true,
    val error: String? = null,
    val showImportDialog: Boolean = false,
    val showAddChannelDialog: Boolean = false
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val channelRepository: ChannelRepository,
    private val playlistRepository: PlaylistRepository,
    private val customChannelRepository: CustomChannelRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    private val _selectedGroup = MutableStateFlow<String?>(null)

    init {
        loadChannels()
        loadGroups()
    }

    private fun loadChannels() {
        viewModelScope.launch {
            combine(
                channelRepository.getAllChannels(),
                customChannelRepository.getAllChannels(),
                _searchQuery,
                _selectedGroup
            ) { playlistChannels, customChannels, query, group ->
                // 将自定义频道转换为 Channel 模型并合并
                val customChannelList = customChannels.map { custom ->
                    Channel(
                        id = custom.id + 1000000, // 使用偏移量避免 ID 冲突
                        name = custom.name,
                        url = custom.url,
                        logo = custom.logo,
                        group = custom.group ?: "自定义频道",
                        isFavorite = false,
                        playlistId = -1 // 标记为自定义频道
                    )
                }

                // 合并频道列表（自定义频道在前）
                var allChannels = customChannelList + playlistChannels

                // 按分组筛选
                if (group != null) {
                    allChannels = allChannels.filter { it.group == group }
                }

                // 按搜索词筛选
                if (query.isNotBlank()) {
                    allChannels = allChannels.filter {
                        it.name.contains(query, ignoreCase = true) ||
                        it.group?.contains(query, ignoreCase = true) == true
                    }
                }

                allChannels
            }.collect { filteredChannels ->
                _uiState.value = _uiState.value.copy(
                    channels = filteredChannels,
                    isLoading = false
                )
            }
        }
    }

    private fun loadGroups() {
        viewModelScope.launch {
            combine(
                channelRepository.getAllGroups(),
                customChannelRepository.getAllChannels()
            ) { playlistGroups, customChannels ->
                val customGroups = customChannels.mapNotNull { it.group }.distinct()
                val allGroups = (customGroups + playlistGroups).distinct()
                allGroups
            }.collect { groups ->
                _uiState.value = _uiState.value.copy(groups = groups)
            }
        }
    }

    /**
     * 更新搜索查询
     */
    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
        _uiState.value = _uiState.value.copy(searchQuery = query)
    }

    /**
     * 选择分组
     */
    fun selectGroup(group: String?) {
        _selectedGroup.value = group
        _uiState.value = _uiState.value.copy(selectedGroup = group)
    }

    /**
     * 切换收藏状态
     */
    fun toggleFavorite(channelId: Long) {
        viewModelScope.launch {
            channelRepository.toggleFavorite(channelId)
        }
    }

    /**
     * 显示导入对话框
     */
    fun showImportDialog() {
        _uiState.value = _uiState.value.copy(showImportDialog = true)
    }

    /**
     * 隐藏导入对话框
     */
    fun hideImportDialog() {
        _uiState.value = _uiState.value.copy(showImportDialog = false)
    }

    /**
     * 从 URL 导入播放列表
     */
    fun importFromUrl(url: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            val result = playlistRepository.importFromUrl(url)
            result.fold(
                onSuccess = { count ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        showImportDialog = false
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
     * 从本地内容导入
     */
    fun importFromContent(content: String, name: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            val result = playlistRepository.importFromContent(content, name)
            result.fold(
                onSuccess = { count ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        showImportDialog = false
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
     * 显示添加频道对话框
     */
    fun showAddChannelDialog() {
        _uiState.value = _uiState.value.copy(showAddChannelDialog = true)
    }

    /**
     * 隐藏添加频道对话框
     */
    fun hideAddChannelDialog() {
        _uiState.value = _uiState.value.copy(showAddChannelDialog = false)
    }

    /**
     * 添加自定义频道
     */
    fun addCustomChannel(name: String, url: String) {
        viewModelScope.launch {
            try {
                customChannelRepository.insertChannel(name = name, url = url)
                _uiState.value = _uiState.value.copy(showAddChannelDialog = false)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message ?: "添加频道失败"
                )
            }
        }
    }

    /**
     * 清除错误
     */
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}
