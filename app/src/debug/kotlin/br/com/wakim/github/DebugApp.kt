package br.com.wakim.github

import br.com.wakim.github.injection.AppModule
import br.com.wakim.github.injection.DaggerApplicationComponent
import br.com.wakim.github.injection.DebugApiModule
import com.facebook.stetho.Stetho

class DebugApp : App() {

    override fun onCreate() {
        super.onCreate()
        Stetho.initializeWithDefaults(this)
    }

    override fun buildComponent() {
        appComponent = DaggerApplicationComponent.builder()
                .appModule(AppModule(this))
                .apiModule(DebugApiModule(BuildConfig.API_URL))
                .build()
    }
}