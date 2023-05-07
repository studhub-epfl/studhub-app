package com.studhub.app.data

import com.studhub.app.data.network.NetworkStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class NetworkStatusImplTest : NetworkStatus {
    override val isConnected: Boolean
        get() = true

    override fun connectivityStatus(): Flow<Boolean> {
        return flowOf(true)
    }
}
