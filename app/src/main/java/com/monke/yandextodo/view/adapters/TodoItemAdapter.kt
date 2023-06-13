package com.monke.yandextodo.view.adapters

import android.opengl.Visibility
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.monke.yandextodo.R
import com.monke.yandextodo.domain.Constants
import com.monke.yandextodo.model.TodoItem

class TodoItemAdapter(todoItemList: ArrayList<TodoItem>, onClickListener: OnClickListener): RecyclerView.Adapter<TodoItemAdapter.TodoItemViewHolder>() {

    private val todoItemList = todoItemList
    private val onClickListener = onClickListener

    class TodoItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(todoItem: TodoItem) {
            val titleTxt = itemView.findViewById<TextView>(R.id.title_txt)
            titleTxt.text = todoItem.text

            val importanceView = itemView.findViewById<ImageView>(R.id.importance_ic)
            when (todoItem.importance) {
                Constants.NO_IMPORTANCE -> importanceView.visibility = View.GONE
                Constants.LOW_IMPORTANCE -> importanceView.setImageDrawable(
                    itemView.context.getDrawable(R.drawable.ic_low_importance))
                Constants.HIGH_IMPORTANCE -> importanceView.setImageDrawable(
                    itemView.context.getDrawable(R.drawable.ic_high_importance))
            }
        }
    }

    interface OnClickListener {
        fun OnClick(todoItem: TodoItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoItemViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_todo, parent, false)
        return TodoItemViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return todoItemList.size
    }

    override fun onBindViewHolder(holder: TodoItemViewHolder, position: Int) {
        holder.bind(todoItemList[position])
        holder.itemView.setOnClickListener { view -> onClickListener.OnClick(todoItemList[position]) }
    }
}