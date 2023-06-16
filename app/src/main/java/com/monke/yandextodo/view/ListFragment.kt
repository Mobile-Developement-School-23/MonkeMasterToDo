package com.monke.yandextodo.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.monke.yandextodo.R
import com.monke.yandextodo.domain.TodoItemsRepository
import com.monke.yandextodo.model.TodoItem
import com.monke.yandextodo.view.adapters.TodoItemAdapter

class ListFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_list, container, false)

        // Настройка RecyclerView для списка задач
        val tasksRecycler = view.findViewById<RecyclerView>(R.id.tasks_recycler)
        val adapter = TodoItemAdapter(object : TodoItemAdapter.OnClickListener {
            override fun onClick(todoItem: TodoItem) {
                (activity as MainActivity).setFragment(TaskFragment(todoItem))
            }
        })
        adapter.todoItemList = TodoItemsRepository.todoItemsList
        tasksRecycler.adapter = adapter
        tasksRecycler.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        // Кнопка добавления задачи
        val addTaskButton = view.findViewById<FloatingActionButton>(R.id.add_task_btn)
        addTaskButton.setOnClickListener { (activity as MainActivity).setFragment(TaskFragment()) }

        return view
    }


}