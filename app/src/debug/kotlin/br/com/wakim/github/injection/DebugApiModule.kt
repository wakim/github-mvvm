package br.com.wakim.github.injection

import android.app.Application
import android.net.ConnectivityManager
import com.facebook.stetho.okhttp3.StethoInterceptor
import okhttp3.OkHttpClient

class DebugApiModule(baseUrl: String) : ApiModule(baseUrl) {

    override fun buildOkHttpClient(application: Application, connectivityManager: ConnectivityManager): OkHttpClient =
            super.buildOkHttpClient(application, connectivityManager)
                    .let {
                        it.newBuilder()
                                .addInterceptor(StethoInterceptor())
                                .build()
                    }
}