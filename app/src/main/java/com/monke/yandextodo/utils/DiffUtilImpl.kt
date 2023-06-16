package com.monke.yandextodo.utils

import androidx.recyclerview.widget.DiffUtil

class DiffUtilImpl<T>(
    private val oldItems: List<T>,
    private val newItems: List<T>,
    private val areItemsTheSameImpl: (newItem: T, oldItem:T) -> Boolean =
        {oldItem, newItem -> oldItem == newItem},
    private val areContentsTheSameImpl: (newItem: T, oldItem:T) -> Boolean =
            {oldItem, newItem -> oldItem == newItem}

): DiffUtil.Callback() {
    override fun getOldListSize() = oldItems.size

    override fun getNewListSize() = newItems.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return areItemsTheSameImpl(newItems[newItemPosition], oldItems[oldItemPosition])
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return areContentsTheSameImpl(newItems[newItemPosition], oldItems[oldItemPosition])
    }


}