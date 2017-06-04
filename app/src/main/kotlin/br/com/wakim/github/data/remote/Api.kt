package br.com.wakim.github.data.remote

import br.com.wakim.github.data.model.RepositorySearchResponse
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {
    @GET("/search/repositories")
    fun searchRepositories(@Query("q") query: String, @Query("page") page: Int, @Query("per_page") perPage: Int? = 25): Observable<Response<RepositorySearchResponse>>
}

fun Response<*>.hasMore() =
        headers()
                .get("Link")?.let {

            it.split(",")
                    .asSequence()
                    .indexOfFirst { it.contains("rel=\"next\"") } >= 0
        } ?: false