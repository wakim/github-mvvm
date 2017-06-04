package br.com.wakim.github.data.model

data class LCE<out M>(val loading: Boolean = false,
                 val content: M? = null,
                 val error: Throwable? = null)