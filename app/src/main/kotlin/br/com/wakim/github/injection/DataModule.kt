package br.com.wakim.github.injection

import br.com.wakim.github.data.RepositoryDataSource
import br.com.wakim.github.data.RepositoryRepository
import br.com.wakim.github.data.remote.Api
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DataModule {
    @Provides
    @Singleton
    fun providesRepositoryDataSource(api: Api): RepositoryDataSource = RepositoryRepository(api)
}