package com.monke.yandextodo.presentation

import android.content.DialogInterface
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.monke.yandextodo.App
import com.monke.yandextodo.R
import com.monke.yandextodo.presentation.todoItemFeature.dialogs.SynchronizationDialog
import com.monke.yandextodo.presentation.todoItemFeature.fragments.TodoItemListFragment
import com.monke.yandextodo.presentationState.TodoItemViewModel
import com.monke.yandextodo.presentationState.TodoItemViewModelFactory
import com.monke.yandextodo.presentationState.UiState
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

        viewModel.errorMessage.observe(this) { errorMessage ->
            if (errorMessage != null)
                Toast.makeText(applicationContext, errorMessage, Toast.LENGTH_SHORT).show()
        }

        viewModel.uiState.observe(this) { uiState ->

            when (uiState) {
                is UiState.Error -> Toast.makeText(
                    applicationContext,
                    uiState.message,
                    Toast.LENGTH_SHORT).show()
                UiState.Loading -> Toast.makeText(
                    applicationContext,
                    "Простите. Анимации загрузки пока нет.",
                    Toast.LENGTH_SHORT).show()
                UiState.NeedSync -> showSyncDialog()
                UiState.Success -> {}
            }

        }

    }

    private fun showSyncDialog() {
        val syncDialog = SynchronizationDialog()
        syncDialog.show(supportFragmentManager, syncDialog.tag)
    }


}