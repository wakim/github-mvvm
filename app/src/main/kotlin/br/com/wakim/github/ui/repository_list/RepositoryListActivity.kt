package br.com.wakim.github.ui.repository_list

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.view.inputmethod.EditorInfo
import br.com.wakim.github.BR
import br.com.wakim.github.R
import br.com.wakim.github.app.BaseActivity
import br.com.wakim.github.data.model.Repository
import br.com.wakim.github.databinding.ActivityRepositoryListBinding
import br.com.wakim.github.databinding.ListItemRepositoryBinding
import br.com.wakim.github.util.gone
import br.com.wakim.github.util.visible
import br.com.wakim.github.widget.DataBindAdapter
import br.com.wakim.github.widget.ReactiveInfeniteListener
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable

class RepositoryListActivity : BaseActivity() {

    lateinit var binding: ActivityRepositoryListBinding

    val adapter: DataBindAdapter<Repository, ListItemRepositoryBinding> by lazy {
        DataBindAdapter<Repository, ListItemRepositoryBinding>(R.layout.list_item_repository, BR.repository, Repository::id, this)
    }

    val viewModel: RepositoryListViewModel by lazy { component.repositoryListViewModel() }
    val scrollListener = ReactiveInfeniteListener()

    val disposable = CompositeDisposable()

    override fun disposeConfigPersistentResources() {
        viewModel.onCleared()
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.clear()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView<ActivityRepositoryListBinding>(this, R.layout.activity_repository_list)

        setupToolbar()
        setupAdapter()
        setupSearch()
        setupRecyclerView()

        val obs = viewModel.repositoriesObservable
                .compose(bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext {
                    scrollListener.hasMore = it.content?.isNotEmpty() ?: true
                    scrollListener.enabled = !it.loading

                    binding.swipeRefreshLayout.isRefreshing = it.loading

                    if (it.loading) {
                        binding.loading.visible()
                    } else {
                        binding.loading.gone()
                    }

                    it.error?.apply {
                        Snackbar.make(binding.coordinatorLayout, this.toString(), Snackbar.LENGTH_LONG).show()
                    }
                }
                .filter { it.content != null }
                .map { it.content ?: emptyList() }

        disposable.add(adapter.bind(obs))
    }

    fun setupToolbar() {
        setSupportActionBar(binding.includeAppBar.toolbar)
    }

    fun setupAdapter() {
        binding.recyclerView.adapter = adapter
    }

    fun setupRecyclerView() {
        binding.recyclerView.addOnScrollListener(scrollListener)
        viewModel.bindPage(scrollListener.listen().compose(bindToLifecycle()))
                .apply { disposable.add(this) }
    }

    fun setupSearch() {
        RxTextView.editorActions(binding.includeAppBar.etSearch)
                .compose(bindToLifecycle())
                .filter { it == EditorInfo.IME_ACTION_SEARCH }
                .cast(Any::class.java)
                .map { binding.includeAppBar.etSearch.text.toString() }
                .filter { it.isNotBlank() }
                .let(viewModel::bindSearch)
                .apply { disposable.add(this) }

        binding.swipeRefreshLayout.setOnRefreshListener {}
    }
}
