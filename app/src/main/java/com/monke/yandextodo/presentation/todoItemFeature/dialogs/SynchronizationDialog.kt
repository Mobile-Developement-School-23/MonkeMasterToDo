package com.monke.yandextodo.presentation.todoItemFeature.dialogs

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.monke.yandextodo.App
import com.monke.yandextodo.databinding.DialogSynchronizationBinding
import com.monke.yandextodo.presentationState.todoFeature.TodoItemViewModel
import com.monke.yandextodo.presentationState.todoFeature.TodoItemViewModelFactory
import javax.inject.Inject

class SynchronizationDialog: DialogFragment() {

    @Inject
    lateinit var viewModelFactory: TodoItemViewModelFactory
    private val viewModel: TodoItemViewModel by activityViewModels {
        viewModelFactory
    }

    override fun onAttach(context: Context) {
        (activity?.applicationContext as App).applicationComponent.mainTodoActivityComponent()
            .synchronizationComponent().inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = DialogSynchronizationBinding.inflate(layoutInflater)

        binding.mergeFromDbBtn.setOnClickListener {
            viewModel.mergeDataFromDatabase()
            dismiss()
        }

        binding.mergeFromServerBtn.setOnClickListener {
            viewModel.mergeDataFromServer()
            dismiss()
        }


        return binding.root
    }
}