package br.com.wakim.github.util

import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager

// Source: com.github.pwittchen.infinitescroll
open class InfiniteScrollListener(var hasMore: Boolean = true,
                                  var enabled: Boolean = true) : RecyclerView.OnScrollListener() {

    override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        if (canLoadMore() && hasMoreToLoad(recyclerView?.layoutManager ?: return)) {
            onScrolledToEnd()
        }
    }

    fun canLoadMore() = enabled && hasMore

    fun hasMoreToLoad(layoutManager: RecyclerView.LayoutManager): Boolean {
        val visibleItemsCount = layoutManager.childCount
        val totalItemsCount = layoutManager.itemCount
        val pastVisibleItemsCount = findFirstVisibleItemPosition(layoutManager)
        val lastItemShown = visibleItemsCount + pastVisibleItemsCount >= totalItemsCount

        return lastItemShown && hasMore
    }

    var info = intArrayOf(0, 0)

    fun findFirstVisibleItemPosition(layoutManager: RecyclerView.LayoutManager) =
            when (layoutManager) {
                is LinearLayoutManager -> layoutManager.findFirstVisibleItemPosition()
                is GridLayoutManager -> layoutManager.findFirstVisibleItemPosition()
                is StaggeredGridLayoutManager -> layoutManager.findFirstCompletelyVisibleItemPositions(info).let { minOf(info[0], info[1]) }
                else -> -1
            }

    open fun onScrolledToEnd() {}
}
