package com.monke.yandextodo.presentation

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.work.Configuration
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.monke.yandextodo.App
import com.monke.yandextodo.R
import com.monke.yandextodo.domain.Importance
import com.monke.yandextodo.domain.TodoItem
import com.monke.yandextodo.presentation.todoItemFeature.dialogs.SynchronizationDialog
import com.monke.yandextodo.presentation.todoItemFeature.fragments.TodoItemListFragment
import com.monke.yandextodo.presentationState.TodoItemViewModel
import com.monke.yandextodo.presentationState.TodoItemViewModelFactory
import com.monke.yandextodo.presentationState.UiState
import com.monke.yandextodo.utils.workers.notificationFeature.NotificationWorker
import java.util.Calendar
import java.util.UUID
import java.util.concurrent.TimeUnit
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

        supportFragmentManager.beginTransaction().replace(
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