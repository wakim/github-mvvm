package br.com.wakim.github.ui

import br.com.wakim.github.BuildConfig
import br.com.wakim.github.data.ImmediateSchedulerProvider
import br.com.wakim.github.data.RepositoryDataSource
import br.com.wakim.github.data.exception.NetworkConnectivityException
import br.com.wakim.github.data.model.LCE
import br.com.wakim.github.data.model.NextPage
import br.com.wakim.github.data.model.RepositorySearchResponse
import br.com.wakim.github.ui.repository_list.RepositoryListViewModel
import br.com.wakim.github.util.newRepositoryList
import com.nhaarman.mockito_kotlin.mock
import io.reactivex.Observable
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.`when`
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@Suppress("IllegalIdentifier")
@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class)
class RepositoryListViewModelTest {

    lateinit var viewModel: RepositoryListViewModel
    lateinit var repository: RepositoryDataSource

    val repositories = newRepositoryList(5)

    @Before
    fun setup() {
        repository = mock()
        viewModel = RepositoryListViewModel(repository, ImmediateSchedulerProvider())
    }

    @Test
    fun `should not search with empty query`() {
        `when`(repository.search("", NextPage(true, 1)))
                .thenReturn(Observable.just(LCE.error(NetworkConnectivityException)))

        viewModel.bindSearch(Observable.just(""))
        assertNull(viewModel.repositoriesLiveData.value)
    }

    @Test
    fun `should notify error to view when search is submitted`() {
        `when`(repository.search("abc", NextPage(true, 1)))
                .thenReturn(Observable.just(LCE.error(NetworkConnectivityException)))

        viewModel.bindSearch(Observable.just("abc"))
        assertEquals(viewModel.repositoriesLiveData.value, LCE.error<RepositorySearchResponse>(NetworkConnectivityException))
    }

    @Test
    fun `should notify repositories to view when search is submitted`() {
        val lce = LCE.content(RepositorySearchResponse(repositories, 1, NextPage(true, 2)))

        `when`(repository.search("abc", NextPage(true, 1)))
                .thenReturn(Observable.just(lce))

        viewModel.bindSearch(Observable.just("abc"))
        assertEquals(viewModel.repositoriesLiveData.value, lce)
    }

    @Test
    fun `should not fetch next page when no query was submitted`() {
        val lce1 = LCE.content(RepositorySearchResponse(repositories, 1, NextPage(true, 2)))

        `when`(repository.search("abc", NextPage(true, 2)))
                .thenReturn(Observable.just(lce1))

        viewModel.bindPage(Observable.just(Any()))

        assertNull(viewModel.repositoriesLiveData.value)
    }

    @Test
    fun `should notify repositories to view when next page is submitted`() {
        val lce1 = LCE.content(RepositorySearchResponse(repositories, 1, NextPage(true, 2)))
        val lce2 = LCE.content(RepositorySearchResponse(newRepositoryList(2), 2, NextPage(false, 3)))

        `when`(repository.search("abc", NextPage(true, 1)))
                .thenReturn(Observable.just(lce1))

        `when`(repository.search("abc", NextPage(true, 2)))
                .thenReturn(Observable.just(lce2))

        viewModel.bindSearch(Observable.just("abc"))

        assertEquals(viewModel.repositoriesLiveData.value, lce1)

        viewModel.bindPage(Observable.just(Any()))

        assertEquals(viewModel.repositoriesLiveData.value, lce2)
    }
}