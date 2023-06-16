package com.monke.yandextodo.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.monke.yandextodo.R
import com.monke.yandextodo.domain.Constants
import com.monke.yandextodo.model.TodoItem
import com.monke.yandextodo.utils.DateUtils
import com.monke.yandextodo.utils.DiffUtilImpl
import java.util.Calendar

class TodoItemAdapter(private val onClickListener: OnClickListener): RecyclerView.Adapter<TodoItemAdapter.TodoItemViewHolder>() {

    // DiffUtil
    var todoItemList = listOf<TodoItem>()
        set (value) {
            val callback = DiffUtilImpl<TodoItem>(
                oldItems = field,
                newItems = value,
                {newItem, oldItem ->  newItem.id == oldItem.id}
            )

            field = value
            val diff = DiffUtil.calculateDiff(callback)
            diff.dispatchUpdatesTo(this)
        }

    interface OnClickListener {
        fun onClick(todoItem: TodoItem)
    }

    class TodoItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        // Настройка отображения задачи
        fun bind(todoItem: TodoItem) {
            val titleTxt = itemView.findViewById<TextView>(R.id.title_txt)
            titleTxt.text = todoItem.text

            // Устанавливает иконку в зависимости от важности задачи
            val importanceView = itemView.findViewById<ImageView>(R.id.importance_ic)
            when (todoItem.importance) {
                Constants.NO_IMPORTANCE -> importanceView.visibility = View.GONE
                Constants.LOW_IMPORTANCE -> importanceView.setImageDrawable(
                    AppCompatResources.getDrawable(itemView.context, R.drawable.ic_low_importance))
                Constants.HIGH_IMPORTANCE -> importanceView.setImageDrawable(
                    AppCompatResources.getDrawable(itemView.context, R.drawable.ic_high_importance))
            }

            // Отображает дату дедлайна, если у задачи она есть
            val dateTxt = itemView.findViewById<TextView>(R.id.date_txt)
            if (todoItem.deadlineDate != null) {
                val day = todoItem.deadlineDate!!.get(Calendar.DAY_OF_MONTH)
                val month = todoItem.deadlineDate!!.get(Calendar.MONTH)
                val year = todoItem.deadlineDate!!.get(Calendar.YEAR)
                dateTxt.text = DateUtils.formatDate(day, month, year)
            } else dateTxt.visibility = View.GONE
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
        holder.itemView.setOnClickListener { onClickListener.onClick(todoItemList[position]) }
    }
}