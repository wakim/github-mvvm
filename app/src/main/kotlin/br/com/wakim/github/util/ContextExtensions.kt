package br.com.wakim.github.util

import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Build

val ConnectivityManager.isConnected: Boolean
    get() = activeNetworkInfo?.isConnected ?: false

fun NetworkInfo.isMobile() = type.run { this == ConnectivityManager.TYPE_MOBILE }
fun NetworkInfo.isWifi() = type.run { this == ConnectivityManager.TYPE_WIFI || this == ConnectivityManager.TYPE_WIMAX }

@Suppress("DEPRECATION")
val ConnectivityManager.allNetworkInfoCompat: Array<NetworkInfo>
    get() {
        if (Build.VERSION.SDK_INT >= 21) {
            return allNetworks.map { getNetworkInfo(it) }.toTypedArray()
        } else {
            return allNetworkInfo
        }
    }
