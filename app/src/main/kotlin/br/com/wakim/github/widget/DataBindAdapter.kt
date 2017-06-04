package br.com.wakim.github.widget

import android.content.Context
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable

class DataBindAdapter<M, in T : ViewDataBinding>(private val layoutResId: Int,
                                                 private val variableId: Int,
                                                 private val idAccessor: (M) -> Long,
                                                 context: Context)  : RecyclerView.Adapter<DataBindViewHolder>() {

    var data: List<M> = ArrayList()
    private val layoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): DataBindViewHolder =
        DataBindViewHolder(DataBindingUtil.inflate(layoutInflater, layoutResId, parent, false))

    override fun onBindViewHolder(holder: DataBindViewHolder?, position: Int) {
        holder?.binding?.apply {
            setVariable(variableId, data[position])
            executePendingBindings()
        }
    }

    override fun getItemCount() = data.size

    fun bind(source: Observable<List<M>>): Disposable {
        data class DataWithDiff(val list: List<M>, val diff: DiffUtil.DiffResult?)

        // Thanks dlew :D
        return source
                .scan(DataWithDiff(emptyList(), null)) { (old), new ->
                    DataWithDiff(new, DiffUtil.calculateDiff(DiffCallback(old, new, idAccessor)))
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    data = it.list
                    it.diff?.dispatchUpdatesTo(this) ?: notifyDataSetChanged()
                }
    }

    private class DiffCallback<M>(private val old: List<M>, private val new: List<M>, private val idAccessor: (M) -> Long) : DiffUtil.Callback() {
        override fun getOldListSize() = old.size

        override fun getNewListSize() = new.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
                idAccessor.invoke(old[oldItemPosition]) == idAccessor.invoke(new[newItemPosition])

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
                old[oldItemPosition] == new[newItemPosition]
    }
}

class DataBindViewHolder(val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root)
