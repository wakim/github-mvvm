package br.com.wakim.github.data.model

data class LCE<out M>(val loading: Boolean = false,
                 val content: M? = null,
                 val error: Throwable? = null) {

    companion object {
        fun <T> error(throwable: Throwable) = LCE<T>(false, null, throwable)
        fun <T> loading() = LCE<T>(true, null, null)
        fun <T> content(t: T) = LCE<T>(false, t, null)
    }
}