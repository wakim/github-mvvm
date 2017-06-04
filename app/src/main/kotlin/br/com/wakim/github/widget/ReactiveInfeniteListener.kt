package br.com.wakim.github.widget

import android.support.v7.widget.LinearLayoutManager
import br.com.wakim.github.util.InfiniteScrollListener
import io.reactivex.Emitter
import io.reactivex.Observable

class ReactiveInfeniteListener(hasMore: Boolean = true,
                               enabled: Boolean = true) : InfiniteScrollListener(hasMore, enabled) {

    val emitters = ArrayList<Emitter<Any>>()

    fun listen(): Observable<Any> {
        return Observable.create {
            emitters.add(it)

            it.setCancellable {
                emitters.remove(it)
            }
        }
    }

    override fun onScrolledToEnd() {
        val any = Any()
        emitters.forEach { it.onNext(any) }
    }
}