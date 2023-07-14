package com.monke.yandextodo.presentation.todoItemFeature.fragments


import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.monke.yandextodo.App
import com.monke.yandextodo.R
import com.monke.yandextodo.databinding.FragmentTasksListBinding
import com.monke.yandextodo.domain.Constants
import com.monke.yandextodo.domain.TodoItem
import com.monke.yandextodo.presentation.todoItemFeature.adapters.TodoItemAdapter
import com.monke.yandextodo.presentationState.todoFeature.TodoItemViewModel
import com.monke.yandextodo.presentationState.todoFeature.TodoItemViewModelFactory
import kotlinx.coroutines.launch
import javax.inject.Inject

// Фрагмент со списком задач
class TodoItemListFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: TodoItemViewModelFactory
    private var binding: FragmentTasksListBinding? = null
    private val viewModel: TodoItemViewModel by activityViewModels {
        viewModelFactory
    }

    override fun onAttach(context: Context) {
        (activity?.applicationContext as App).applicationComponent.mainTodoActivityComponent().
            todoItemListFragmentComponent().inject(this)
        super.onAttach(context)
    }

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
        configureSettingsBtn()
    }

    override fun onDestroy() {
        super.onDestroy()

        binding = null
    }

    // Настройка RecyclerView для списка задач
    private fun configureTasksListAdapter() {
        val tasksRecycler = binding?.tasksRecycler
        // Настройка адаптера
        val adapter = TodoItemAdapter(object : TodoItemAdapter.TodoItemClickListener {
            // Callback для нажатия на item
            override fun onItemClick(todoItem: TodoItem) {
                viewModel.clearNewTodoItemFields()
                findNavController().navigate(
                    R.id.from_list_to_item,
                    bundleOf(TodoItemFragment.ID_KEY to todoItem.id))
            }

            // Callback для нажатия на checkbox
            override fun onCheckboxClick(todoItem: TodoItem, onChecked: Boolean) {
                if (todoItem.completed != onChecked) {
                    todoItem.completed = onChecked
                    viewModel.saveTodoItem(todoItem)
                }

            }
        })

        tasksRecycler?.adapter = adapter
        tasksRecycler?.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL,
            false)

        // Подписывается на изменение списка задач
        viewModel.tasksList.observe(viewLifecycleOwner) {
            adapter.todoItemList = it
        }



        // Подписывается на удаленную задачу
        viewModel.deletingItem.observe(viewLifecycleOwner) {todoItem ->
            if (todoItem != null)
                showCancelDeleteSnackBar(todoItem)
        }

    }

    // Настройка кнопки добавления задачи
    private fun configureAddTaskBtn() {
        // Кнопка добавления задачи
        val addTaskButton = binding?.addTaskBtn
        addTaskButton?.setOnClickListener {
            viewModel.clearNewTodoItemFields()
            findNavController().navigate(R.id.from_list_to_item)

        }
    }

    private fun configureSettingsBtn() {
        binding?.settingsBtn?.setOnClickListener {
            findNavController().navigate(R.id.from_list_to_settings)
        }
    }

    private fun showCancelDeleteSnackBar(todoItem: TodoItem) {
        val snackbarMessage = "${getString(R.string.delete)} ${todoItem.text}?"
        val snackbar = Snackbar.make(binding!!.tasksRecycler,
            snackbarMessage,
            Constants.SNACKBAR_LENGTH).apply {
                setAction(R.string.cancel) {
                    viewModel.cancelDelete(todoItem)
                }
            addCallback(object : Snackbar.Callback() {
                override fun onShown(sb: Snackbar?) {
                    super.onShown(sb)
                }

                override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                    super.onDismissed(transientBottomBar, event)

                    if (event == DISMISS_EVENT_TIMEOUT) {
                        viewModel.confirmDelete()
                    }
                }
            })
        }

        snackbar.show()
    }

}