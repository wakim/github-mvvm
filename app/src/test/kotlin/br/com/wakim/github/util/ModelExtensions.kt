package br.com.wakim.github.util

import br.com.wakim.github.data.model.Repository

fun newRepositoryList(count: Int) =
        (0..count).map { number ->
            Repository("repository$number", number.toLong(), "Repository $number", "Description of Repository $number",
                    "url$number", number, number, number, number, "Android")
        }.toList()