package br.com.wakim.github.injection

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import br.com.wakim.github.App
import br.com.wakim.github.ui.repository_list.RepositoryListViewModel

class ViewModelAppComponentFactory(app: App) : ViewModelProviders.DefaultFactory(app) {

    val appComponent = app.appComponent

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        when (modelClass) {
            RepositoryListViewModel::class.java -> return appComponent.repositoryViewModel() as T
        }

        return super.create(modelClass)
    }
}