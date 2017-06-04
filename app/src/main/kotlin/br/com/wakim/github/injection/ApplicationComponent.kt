package br.com.wakim.github.injection

import dagger.Component

@Component(modules = arrayOf(AppModule::class, ApiModule::class))
interface ApplicationComponent {
}