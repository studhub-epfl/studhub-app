package com.studhub.app.data

import com.studhub.app.data.network.NetworkStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class NetworkStatusImplTest : NetworkStatus {
    private var connectionStatus = true

    fun turnOffConnection() {
        connectionStatus = false
    }

    fun turnOnConnection() {
        connectionStatus = true
    }

    override val isConnected: Boolean
        get() = connectionStatus

    override fun connectivityStatus(): Flow<Boolean> {
        return flowOf(true)
    }
}
