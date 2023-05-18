package com.studhub.app.data.network

import kotlinx.coroutines.flow.Flow

interface NetworkStatus {
    /**
     * Current connectivity status (true iff the app has access to the internet)
     */
    val isConnected: Boolean

    /**
     * Subscribe to connectivity status updates
     *
     * @return [Flow] of connectivity statuses (true iff the app has access to the internet)
     */
    fun connectivityStatus(): Flow<Boolean>
}
