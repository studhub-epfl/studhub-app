package com.studhub.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.studhub.app.core.utils.ApiResponse
import com.studhub.app.data.network.NetworkStatus
import com.studhub.app.domain.usecase.conversation.FlushPendingMessages
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val networkStatus: NetworkStatus,
    private val flushPendingMessages: FlushPendingMessages
): ViewModel() {
    fun getNetworkState() = networkStatus.connectivityStatus().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        networkStatus.isConnected
    )

    fun flushCachedMessages() {
        viewModelScope.launch {
            flushPendingMessages().collect()
        }
    }
}
