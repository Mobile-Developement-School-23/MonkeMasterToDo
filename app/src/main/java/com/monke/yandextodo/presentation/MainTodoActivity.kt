package com.monke.yandextodo.presentation

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.preference.PreferenceManager
import com.monke.yandextodo.App
import com.monke.yandextodo.R
import com.monke.yandextodo.presentation.todoItemFeature.dialogs.SynchronizationDialog
import com.monke.yandextodo.presentationState.todoFeature.TodoItemViewModel
import com.monke.yandextodo.presentationState.todoFeature.TodoItemViewModelFactory
import com.monke.yandextodo.presentationState.todoFeature.UiState
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
                UiState.Loading -> {}
                UiState.NeedSync -> showSyncDialog()
                UiState.Success -> {}
            }
        }

        // Запрашивает разрешение на отправку уведомлений
        // если уровень API >= 33
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS), 200
            )
        }



    }

    private fun showSyncDialog() {
        val syncDialog = SynchronizationDialog()
        syncDialog.show(supportFragmentManager, syncDialog.tag)
    }


}