package com.monke.yandextodo.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import com.monke.yandextodo.App
import com.monke.yandextodo.R
import com.monke.yandextodo.presentation.todoItemFeature.fragments.TodoItemListFragment
import com.monke.yandextodo.viewModels.TodoItemViewModel
import com.monke.yandextodo.viewModels.TodoItemViewModelFactory
import javax.inject.Inject

class MainTodoActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: TodoItemViewModelFactory
    private val viewModel: TodoItemViewModel by viewModels {
        viewModelFactory
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        (applicationContext as App).applicationComponent
            .mainTodoActivityComponent().inject(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_todo)

        supportFragmentManager.beginTransaction().add(
            R.id.fragmentContainerView,
            TodoItemListFragment()
        ).commit()

    }



}