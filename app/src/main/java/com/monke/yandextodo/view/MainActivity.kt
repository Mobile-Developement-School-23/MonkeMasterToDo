package com.monke.yandextodo.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.monke.yandextodo.R
import com.monke.yandextodo.domain.TodoItemsRepository
import com.monke.yandextodo.model.TodoItem
import com.monke.yandextodo.view.adapters.TodoItemAdapter
import java.time.LocalDate
import java.util.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        for (i in 1..20) {
            if (i % 2 == 0)
                TodoItemsRepository.addTodoItem(TodoItem(i.toString(), getString(R.string.big_task_text),
                    i % 3, false, Calendar.getInstance()))
            else
                TodoItemsRepository.addTodoItem(TodoItem(i.toString(), "Поступить в ШМР",
                    i % 3, false, Calendar.getInstance()))
        }

        supportFragmentManager.beginTransaction().replace(R.id.fragmentContainerView,
            ListFragment()).commit()

    }

    fun setFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.fragmentContainerView,
            fragment).addToBackStack(fragment.tag).commit()
    }
}