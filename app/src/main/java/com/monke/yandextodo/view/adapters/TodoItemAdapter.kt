package com.monke.yandextodo.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.monke.yandextodo.R
import com.monke.yandextodo.model.TodoItem

class TodoItemAdapter(todoItemList: ArrayList<TodoItem>): RecyclerView.Adapter<TodoItemAdapter.TodoItemViewHolder>() {

    private val todoItemList = todoItemList

    class TodoItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(todoItem: TodoItem) {
            val titleTxt = itemView.findViewById<TextView>(R.id.title_txt)
            titleTxt.text = todoItem.text
        }
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
    }
}