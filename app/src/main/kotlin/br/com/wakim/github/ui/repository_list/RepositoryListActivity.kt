package br.com.wakim.github.ui.repository_list

import android.os.Bundle
import android.view.inputmethod.EditorInfo
import br.com.wakim.github.App
import br.com.wakim.github.BR
import br.com.wakim.github.R
import br.com.wakim.github.app.BaseActivity
import br.com.wakim.github.data.model.Repository
import br.com.wakim.github.databinding.ActivityRepositoryListBinding
import br.com.wakim.github.databinding.ListItemRepositoryBinding
import br.com.wakim.github.injection.ViewModelAppComponentFactory
import br.com.wakim.github.util.gone
import br.com.wakim.github.util.visible
import br.com.wakim.github.widget.DataBindAdapter

class RepositoryListActivity : BaseActivity() {

    lateinit var binding: ActivityRepositoryListBinding

    val adapter: DataBindAdapter<Repository, ListItemRepositoryBinding> by lazy {
        DataBindAdapter<Repository, ListItemRepositoryBinding>(R.layout.list_item_repository, BR.repository, Repository::id, this)
    }

    val viewModel: RepositoryListViewModel by lazy {
        ViewModelProviders.of(this, ViewModelAppComponentFactory(application as App)).get(RepositoryListViewModel::class.java)
    }

    val adapterSubject: PublishSubject<List<Repository>> = PublishSubject.create<List<Repository>>()
    var disposable : Disposable? = null

    override fun onDestroy() {
        super.onDestroy()

        viewModel.clearBindings()
        disposable?.dispose()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView<ActivityRepositoryListBinding>(this, R.layout.activity_repository_list)

        setupToolbar()
        setupAdapter()
        setupSearch()

        viewModel.repositoriesLiveData.observe(this, Observer {
            it?.content?.items?.let(adapterSubject::onNext)
            it?.error?.apply {
                Snackbar.make(binding.coordinatorLayout, this.toString(), Snackbar.LENGTH_LONG).show()
            }

            if (it?.loading ?: false) {
                binding.loading.visible()
            } else {
                binding.loading.gone()
            }
        })
    }

    fun setupToolbar() {
        setSupportActionBar(binding.includeAppBar.toolbar)
    }

    fun setupAdapter() {
        binding.recyclerView.adapter = adapter
        disposable = adapter.bind(adapterSubject.observeOn(Schedulers.computation()))
    }

    fun setupSearch() {
        RxTextView.editorActions(binding.includeAppBar.etSearch)
                .filter { it == EditorInfo.IME_ACTION_SEARCH }
                .map { binding.includeAppBar.etSearch.text.toString() }
                .apply(viewModel::bindSearch)
    }
}
