package br.com.wakim.github.data.model

data class RepositorySearchResponse(val items: List<Repository>,
                                    val currentPage: Int,
                                    val nextPage: NextPage)