package com.studhub.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.studhub.app.data.network.NetworkStatus
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val networkStatus: NetworkStatus
): ViewModel() {
    fun getNetworkState() = networkStatus.connectivityStatus().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        networkStatus.isConnected
    )

    fun flushCachedMessages() {}
}
