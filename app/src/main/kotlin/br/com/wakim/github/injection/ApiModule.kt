package br.com.wakim.github.injection

import android.app.Application
import android.net.ConnectivityManager
import br.com.wakim.github.BuildConfig
import br.com.wakim.github.data.exception.NetworkConnectivityException
import br.com.wakim.github.data.remote.Api
import br.com.wakim.github.util.isConnected

@Module
open class ApiModule(var baseUrl: String) {

    @Provides
    @Singleton
    fun providesOkHttpClient(application: Application, connectivityManager: ConnectivityManager) =
            buildOkHttpClient(application, connectivityManager)

    @Provides
    @Singleton
    fun providesApi(gson: Gson, okHttpClient: OkHttpClient): Api =
            Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(okHttpClient)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build()
                    .create(Api::class.java)

    @Provides
    @Singleton
    fun providesGson(): Gson = GsonBuilder().serializeNulls().create()

    open fun buildOkHttpClient(application: Application, connectivityManager: ConnectivityManager): OkHttpClient = OkHttpClient.Builder()
            .apply {
                if (BuildConfig.DEBUG) {
                    addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                }
            }
            .addInterceptor {
                if (!connectivityManager.isConnected) {
                    throw NetworkConnectivityException
                }

                it.proceed(it.request())
            }
            .build()
}