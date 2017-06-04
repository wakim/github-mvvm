package br.com.wakim.github.injection

import android.content.Context
import android.net.ConnectivityManager
import br.com.wakim.github.App
import br.com.wakim.github.data.SchedulerProvider
import br.com.wakim.github.data.SchedulerProviderContract
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val app: App) {

    @Provides
    @Singleton
    fun providesApp() = app

    @Provides
    @Singleton
    fun providesSchedulerProvider() : SchedulerProviderContract = SchedulerProvider()

    @Provides
    @Singleton
    fun providesConnectivityManager() =
            app.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
}