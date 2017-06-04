package br.com.wakim.github.injection

import br.com.wakim.github.App

class ViewModelAppComponentFactory(app: App) : ViewModelProviders.DefaultFactory(app) {

    val appComponent = app.appComponent

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (InjectableViewModel::class.java.isAssignableFrom(modelClass)) {
            try {
                return modelClass.getConstructor(ApplicationComponent::class.java).newInstance(appComponent)
            } catch (e: Exception) {
                throw RuntimeException("Cannot create an instance of " + modelClass, e)
            }
        }

        return super.create(modelClass)
    }
}

interface InjectableViewModel