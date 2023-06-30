package com.monke.yandextodo.presentation.todoItemFeature.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.monke.yandextodo.App
import com.monke.yandextodo.R
import com.monke.yandextodo.databinding.FragmentTasksListBinding
import com.monke.yandextodo.domain.TodoItem
import com.monke.yandextodo.presentation.todoItemFeature.adapters.TodoItemAdapter
import com.monke.yandextodo.presentation.todoItemFeature.viewModels.TodoItemViewModel
import javax.inject.Inject

class TodoItemListFragment : Fragment() {

    @Inject
    lateinit var viewModel: TodoItemViewModel
    private var binding: FragmentTasksListBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentTasksListBinding.inflate(layoutInflater)
        this.binding = binding

        (activity?.applicationContext as App).todoItemsListFragmentComponent.inject(this)

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
        val adapter = TodoItemAdapter(object : TodoItemAdapter.TodoItemClickListener {
            override fun onItemClick(todoItem: TodoItem) {
                parentFragmentManager.beginTransaction().replace(
                    R.id.fragmentContainerView,
                    TodoItemFragment.newInstance(todoItem.id)).
                addToBackStack("").commit()
            }

            override fun onCheckboxClick(todoItem: TodoItem, onChecked: Boolean) {
                todoItem.completed = onChecked
                viewModel.saveTodoItem(todoItem)

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
            R.id.fragmentContainerView, TodoItemFragment.newInstance()).
        addToBackStack("").commit() }
    }



}