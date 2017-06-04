package br.com.wakim.github

import android.app.Application
import br.com.wakim.github.injection.ApiModule
import br.com.wakim.github.injection.AppModule
import br.com.wakim.github.injection.ApplicationComponent
import br.com.wakim.github.injection.DaggerApplicationComponent

open class App : Application() {

    lateinit var appComponent : ApplicationComponent

    override fun onCreate() {
        super.onCreate()
        buildComponent()
    }

    open fun buildComponent() {
        appComponent = DaggerApplicationComponent.builder()
                .appModule(AppModule(this))
                .apiModule(ApiModule(BuildConfig.API_URL))
                .build()
    }
}