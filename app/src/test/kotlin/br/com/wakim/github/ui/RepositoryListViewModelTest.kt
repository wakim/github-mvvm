package br.com.wakim.github.ui

import br.com.wakim.github.data.ImmediateSchedulerProvider
import br.com.wakim.github.data.RepositoryDataSource
import br.com.wakim.github.data.exception.NetworkConnectivityException
import br.com.wakim.github.data.model.LCE
import br.com.wakim.github.data.model.NextPage
import br.com.wakim.github.data.model.Repository
import br.com.wakim.github.data.model.RepositorySearchResponse
import br.com.wakim.github.ui.repository_list.RepositoryListViewModel
import br.com.wakim.github.util.newRepositoryList
import com.nhaarman.mockito_kotlin.mock
import io.reactivex.Observable
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`

@Suppress("IllegalIdentifier")
class RepositoryListViewModelTest {

    lateinit var viewModel: RepositoryListViewModel
    lateinit var repository: RepositoryDataSource

    val repositories = newRepositoryList(1)

    @Before
    fun setup() {
        repository = mock()
        viewModel = RepositoryListViewModel(repository, ImmediateSchedulerProvider())
    }

    @Test
    fun `should not search with empty query`() {
        val ts = viewModel.repositoriesObservable.test()

        `when`(repository.search("", NextPage(true, 1)))
                .thenReturn(Observable.just(LCE.error(NetworkConnectivityException)))

        viewModel.bindSearch(Observable.just(""))

        ts.assertNoValues()
    }

    @Test
    fun `should notify error to view when search is submitted`() {
        val ts = viewModel.repositoriesObservable.test()

        `when`(repository.search("abc", NextPage(true, 1)))
                .thenReturn(Observable.just(LCE.error(NetworkConnectivityException)))

        viewModel.bindSearch(Observable.just("abc"))

        ts.assertValue(LCE.error<List<Repository>>(NetworkConnectivityException))
    }

    @Test
    fun `should notify repositories to view when search is submitted`() {
        val ts = viewModel.repositoriesObservable.test()
        val lce = LCE.content(RepositorySearchResponse(repositories, 1, NextPage(true, 2)))

        `when`(repository.search("abc", NextPage(true, 1)))
                .thenReturn(Observable.just(lce))

        viewModel.bindSearch(Observable.just("abc"))

        ts.assertValue(LCE.content(lce.content!!.items))
    }

    @Test
    fun `should not fetch next page when no query was submitted`() {
        val ts = viewModel.repositoriesObservable.test()
        val lce1 = LCE.content(RepositorySearchResponse(repositories, 1, NextPage(true, 2)))

        `when`(repository.search("abc", NextPage(true, 2)))
                .thenReturn(Observable.just(lce1))

        viewModel.bindPage(Observable.just(Any()))

        ts.assertNoValues()
    }

    @Test
    fun `should notify repositories to view when next page is submitted`() {
        val ts = viewModel.repositoriesObservable.test()
        val lce1 = LCE.content(RepositorySearchResponse(repositories, 1, NextPage(true, 2)))
        val lce2 = LCE.content(RepositorySearchResponse(newRepositoryList(2), 2, NextPage(false, 3)))

        `when`(repository.search("abc", NextPage(true, 1)))
                .thenReturn(Observable.just(lce1))

        `when`(repository.search("abc", NextPage(true, 2)))
                .thenReturn(Observable.just(lce2))

        viewModel.bindSearch(Observable.just("abc"))

        ts.assertValue(LCE.content(lce1.content!!.items))

        viewModel.bindPage(Observable.just(Any()))

        ts.assertValues(LCE.content(lce1.content!!.items),
                LCE.content(lce1.content!!.items + lce2.content!!.items))
    }

    @Test
    fun `should empty list when refreshing`() {
        val ts = viewModel.repositoriesObservable.test()

        val lce1 = LCE.content(RepositorySearchResponse(repositories, 1, NextPage(true, 2)))
        val lce2 = LCE.content(RepositorySearchResponse(newRepositoryList(2), 2, NextPage(false, 3)))

        `when`(repository.search("abc", NextPage(true, 1)))
                .thenReturn(Observable.just(lce1))

        `when`(repository.search("abc", NextPage(true, 2)))
                .thenReturn(Observable.just(lce2))

        viewModel.bindSearch(Observable.just("abc"))

        ts.assertValue(LCE.content(lce1.content!!.items))

        viewModel.bindPage(Observable.just(Any()))

        ts.assertValues(LCE.content(lce1.content!!.items),
                LCE.content(lce1.content!!.items + lce2.content!!.items))

        viewModel.bindSearch(Observable.just("abc"))

        ts.assertValueAt(2, { it.content == lce1.content!!.items })
    }
}