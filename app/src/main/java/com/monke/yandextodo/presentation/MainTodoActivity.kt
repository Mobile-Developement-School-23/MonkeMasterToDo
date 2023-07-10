package com.monke.yandextodo.presentation

import android.Manifest
import android.content.DialogInterface
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.monke.yandextodo.App
import com.monke.yandextodo.R
import com.monke.yandextodo.presentation.todoItemFeature.dialogs.SynchronizationDialog
import com.monke.yandextodo.presentation.todoItemFeature.fragments.TodoItemListFragment
import com.monke.yandextodo.presentationState.TodoItemViewModel
import com.monke.yandextodo.presentationState.TodoItemViewModelFactory
import com.monke.yandextodo.presentationState.UiState
import com.monke.yandextodo.utils.workers.NotificationWorker
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
                UiState.Loading -> {}
                UiState.NeedSync -> showSyncDialog()
                UiState.Success -> {}
            }
        }

        ActivityCompat.requestPermissions(this,
            arrayOf(Manifest.permission.POST_NOTIFICATIONS), 200)

        val helpMe = OneTimeWorkRequestBuilder<NotificationWorker>().setInitialDelay(1, TimeUnit.MINUTES).build()
        WorkManager.getInstance(this).enqueue(helpMe)

    }

    private fun showSyncDialog() {
        val syncDialog = SynchronizationDialog()
        syncDialog.show(supportFragmentManager, syncDialog.tag)
    }


}