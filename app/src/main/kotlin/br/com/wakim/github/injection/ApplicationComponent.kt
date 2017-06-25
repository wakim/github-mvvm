package br.com.wakim.github.injection

import br.com.wakim.github.ui.repository_list.RepositoryListViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(AppModule::class, ApiModule::class, DataModule::class))
interface ApplicationComponent {
    fun repositoryViewModel(): RepositoryListViewModel
    fun configPersistentComponent(): ConfigPersistentComponent
}