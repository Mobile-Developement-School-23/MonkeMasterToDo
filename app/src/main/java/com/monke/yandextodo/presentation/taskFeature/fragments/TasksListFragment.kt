package com.monke.yandextodo.presentation.taskFeature.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.monke.yandextodo.R
import com.monke.yandextodo.databinding.FragmentTasksListBinding
import com.monke.yandextodo.domain.TodoItem
import com.monke.yandextodo.presentation.taskFeature.adapters.TodoItemAdapter
import com.monke.yandextodo.presentation.taskFeature.viewmodels.TasksListViewModel

class TasksListFragment : Fragment() {

    private var binding: FragmentTasksListBinding? = null
    private val viewModel: TasksListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentTasksListBinding.inflate(layoutInflater)
        this.binding = binding

        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configureTasksListAdapter()
        configureAddTaskBtn()
    }

    override fun onDestroy() {
        super.onDestroy()

        binding = null
    }

    // Настройка RecyclerView для списка задач
    private fun configureTasksListAdapter() {
        val tasksRecycler = binding?.tasksRecycler
        val adapter = TodoItemAdapter(object : TodoItemAdapter.OnClickListener {
            override fun onClick(todoItem: TodoItem) {
                parentFragmentManager.beginTransaction().replace(
                    R.id.fragmentContainerView,
                    TaskFragment.newInstance(todoItem.id)).
                addToBackStack("").commit()
            }
        })
        viewModel.tasksList.observe(viewLifecycleOwner) {
            val value = viewModel.tasksList.value
            if (value != null)
                adapter.todoItemList = value
        }
        tasksRecycler?.adapter = adapter
        tasksRecycler?.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL,
            false)
    }

    private fun configureAddTaskBtn() {
        // Кнопка добавления задачи
        val addTaskButton = binding?.addTaskBtn
        addTaskButton?.setOnClickListener { parentFragmentManager.beginTransaction().replace(
            R.id.fragmentContainerView, TaskFragment.newInstance()).
        addToBackStack("").commit() }
    }



}