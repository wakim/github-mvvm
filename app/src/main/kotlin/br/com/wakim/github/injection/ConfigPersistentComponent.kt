package br.com.wakim.github.injection

import br.com.wakim.github.ui.repository_list.RepositoryListViewModel
import dagger.Subcomponent

/**
 * A dagger component that will live during the lifecycle of an Activity but it won't
 * be destroy during configuration changes. Check [br.com.wakim.github.app.BaseActivity] to see how this components
 * survives configuration changes.
 * Use the [ConfigPersistent] scope to annotate dependencies that need to survive
 * configuration changes (for example Presenters).
 */
@ConfigPersistent
@Subcomponent
interface ConfigPersistentComponent {
    fun repositoryListViewModel(): RepositoryListViewModel
}
