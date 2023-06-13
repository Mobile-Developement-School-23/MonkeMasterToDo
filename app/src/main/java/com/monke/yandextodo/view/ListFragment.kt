package com.monke.yandextodo.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.monke.yandextodo.R
import com.monke.yandextodo.model.TodoItem
import com.monke.yandextodo.view.adapters.TodoItemAdapter

class ListFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_list, container, false)

        val tasksRecycler = view.findViewById<RecyclerView>(R.id.tasks_recycler)
        val mockList = ArrayList<TodoItem>()

        for (i in 1..20) {
            mockList.add(TodoItem(i.toString(), "Поступить в ШМР", i % 3, false))
        }

        val adapter = TodoItemAdapter(mockList, object : TodoItemAdapter.OnClickListener {
            override fun OnClick(todoItem: TodoItem) {
                (activity as MainActivity).setFragment(TaskFragment(todoItem))
            }
        })
        tasksRecycler.adapter = adapter
        tasksRecycler.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        return view
    }


}