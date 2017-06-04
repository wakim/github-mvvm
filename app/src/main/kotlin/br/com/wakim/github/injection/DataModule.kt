package br.com.wakim.github.injection

import br.com.wakim.github.data.RepositoryDataSource
import br.com.wakim.github.data.RepositoryRepository
import br.com.wakim.github.data.remote.Api

@Module
class DataModule {
    @Provides
    @Singleton
    fun providesRepositoryDataSource(api: Api): RepositoryDataSource = RepositoryRepository(api)
}