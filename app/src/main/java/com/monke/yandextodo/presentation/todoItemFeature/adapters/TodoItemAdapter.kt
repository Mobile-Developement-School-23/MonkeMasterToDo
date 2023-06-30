package com.monke.yandextodo.presentation.todoItemFeature.adapters

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.monke.yandextodo.R
import com.monke.yandextodo.databinding.ItemTodoBinding
import com.monke.yandextodo.domain.Importance
import com.monke.yandextodo.domain.TodoItem
import com.monke.yandextodo.utils.DateUtils
import com.monke.yandextodo.utils.DiffUtilImpl
import java.util.Calendar

class TodoItemAdapter(private val onClickListener: TodoItemClickListener):
    RecyclerView.Adapter<TodoItemAdapter.TodoItemViewHolder>() {

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

    interface TodoItemClickListener {
        fun onItemClick(todoItem: TodoItem)
        fun onCheckboxClick(todoItem: TodoItem, isCompleted: Boolean)
    }

    class TodoItemViewHolder(
        private val binding: ItemTodoBinding,
        private val todoItemClickListener: TodoItemClickListener
    ):
        RecyclerView.ViewHolder(binding.root) {

        // Настройка отображения задачи
        fun bind(todoItem: TodoItem) {
            configureTextView(todoItem)
            configureImportanceIcon(todoItem)
            configureDeadlineText(todoItem)
            configureCheckbox(todoItem)
            itemView.setOnClickListener { todoItemClickListener.onItemClick(todoItem) }
        }

        // Настройка текста задачи
        private fun configureTextView(todoItem: TodoItem) {
            val titleTxt = binding.titleTxt
            titleTxt.text = todoItem.text

            if (todoItem.completed)
                titleTxt.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
        }

        // Настройка иконки
        private fun configureImportanceIcon(todoItem: TodoItem) {
            // Устанавливает иконку в зависимости от важности задачи
            val importanceView = binding.importanceIc
            when (todoItem.importance) {
                Importance.NO_IMPORTANCE -> importanceView.visibility = View.GONE
                Importance.LOW -> importanceView.setImageDrawable(
                    AppCompatResources.getDrawable(itemView.context, R.drawable.ic_low_importance))
                Importance.HIGH -> importanceView.setImageDrawable(
                    AppCompatResources.getDrawable(itemView.context, R.drawable.ic_high_importance))
            }
        }

        // Настройка текста с датой дедлайна
        private fun configureDeadlineText(todoItem: TodoItem) {
            // Отображает дату дедлайна, если у задачи она есть
            val dateTxt = binding.dateTxt
            if (todoItem.deadlineDate != null) {
                val day = todoItem.deadlineDate?.get(Calendar.DAY_OF_MONTH)
                val month = todoItem.deadlineDate?.get(Calendar.MONTH)
                val year = todoItem.deadlineDate?.get(Calendar.YEAR)
                if (day != null && month != null && year != null)
                    dateTxt.text = DateUtils.formatDate(day, month, year)
            } else
                dateTxt.visibility = View.GONE

            if (todoItem.completed)
                dateTxt.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
        }

        private fun configureCheckbox(todoItem: TodoItem) {
            binding.checkBox.setOnCheckedChangeListener {
                buttonView, isChecked ->
                    if (isChecked) {
                        binding.titleTxt.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                        binding.dateTxt.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                        todoItemClickListener.onCheckboxClick(todoItem, isChecked)
                    }
                    else {
                        binding.titleTxt.paintFlags = 0
                        binding.dateTxt.paintFlags = 0
                        todoItemClickListener.onCheckboxClick(todoItem, isChecked)
                    }

            }

            binding.checkBox.isChecked = todoItem.completed
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int): TodoItemViewHolder {
        val binding = ItemTodoBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false)
        return TodoItemViewHolder(binding, onClickListener)
    }

    override fun getItemCount(): Int {
        return todoItemList.size
    }

    override fun onBindViewHolder(
        holder: TodoItemViewHolder,
        position: Int) {
        holder.bind(todoItemList[position])
    }
}