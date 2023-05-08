package com.studhub.app.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class NetworkStatusImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : NetworkStatus {

    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    override var isConnected: Boolean = connectivityManager.activeNetwork != null
        private set

    override fun connectivityStatus(): Flow<Boolean> = callbackFlow {
        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                isConnected = true
                trySend(true)
            }

            override fun onLost(network: Network) {
                isConnected = false
                trySend(false)
            }
        }

        connectivityManager.isDefaultNetworkActive

        connectivityManager.registerDefaultNetworkCallback(callback)

        awaitClose { connectivityManager.unregisterNetworkCallback(callback) }
    }
}
