package br.com.wakim.github.ui.repository_list

import br.com.wakim.github.data.RepositoryDataSource
import br.com.wakim.github.data.SchedulerProviderContract
import br.com.wakim.github.data.model.LCE
import br.com.wakim.github.data.model.NextPage
import br.com.wakim.github.data.model.Repository
import br.com.wakim.github.util.addToCompositeDisposable
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import javax.inject.Inject

class RepositoryListViewModel @Inject constructor(private val repositoryDataSource: RepositoryDataSource,
                                                  schedulerProvider: SchedulerProviderContract) {

    private val searchSubject = PublishSubject.create<Pair<String, NextPage>>().toSerialized()

    private var disposable = CompositeDisposable()
    private var currentQuery = ""

    private var currentPage = NextPage(true, 1)

    private var list: List<Repository> = mutableListOf()

    val repositoriesObservable: Subject<LCE<List<Repository>>> =
            BehaviorSubject.create<LCE<List<Repository>>>()

    init {
        searchSubject
                .observeOn(schedulerProvider.io)
                .doOnNext {
                    currentQuery = it.first
                    currentPage = it.second
                }
                .flatMap { repositoryDataSource.search(it.first, it.second) }
                .doOnNext { it.content?.items?.let { list += it } }
                .map {
                    when {
                        it.loading -> LCE.loading<List<Repository>>()
                        it.error != null -> LCE.error(it.error)
                        it.content != null -> LCE.content(list)
                        else -> LCE.error(IllegalStateException("When else :("))
                    }
                }
                .observeOn(schedulerProvider.ui)
                .subscribe(repositoriesObservable::onNext)
                .addToCompositeDisposable(disposable)
    }

    fun bindSearch(searchObservable: Observable<String>): Disposable =
            searchObservable
                    .filter { it.isNotBlank() }
                    .map { it to currentPage.copy(page = 1) }
                    .doOnNext { list = emptyList() }
                    .subscribe(searchSubject::onNext)

    fun bindPage(pageObservable: Observable<Any>): Disposable =
            pageObservable
                    .filter { !currentQuery.isNullOrEmpty() }
                    .map { currentQuery to currentPage.copy(page = currentPage.page + 1) }
                    .subscribe(searchSubject::onNext)

    fun onCleared() {
        disposable.clear()
        searchSubject.onComplete()
    }
}