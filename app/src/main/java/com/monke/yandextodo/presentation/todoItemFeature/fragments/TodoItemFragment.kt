package com.monke.yandextodo.presentation.todoItemFeature.fragments

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.monke.yandextodo.App
import com.monke.yandextodo.R
import com.monke.yandextodo.domain.Importance
import com.monke.yandextodo.domain.TodoItem
import com.monke.yandextodo.presentation.theme.AppTheme
import com.monke.yandextodo.presentation.theme.Blue
import com.monke.yandextodo.presentation.theme.Red
import com.monke.yandextodo.presentationState.todoFeature.TodoItemViewModel
import com.monke.yandextodo.presentationState.todoFeature.TodoItemViewModelFactory
import com.monke.yandextodo.utils.DateUtils
import java.util.Calendar
import javax.inject.Inject

class TodoItemFragment: Fragment() {

    @Inject
    lateinit var viewModelFactory: TodoItemViewModelFactory

    private var todoItem: TodoItem? = null
    private val viewModel: TodoItemViewModel by activityViewModels {
        viewModelFactory
    }

    companion object {

        const val ID_KEY = "todoItemId"
    }

    override fun onAttach(context: Context) {
        (activity?.applicationContext as App).applicationComponent.mainTodoActivityComponent().
            todoItemFragmentComponent().inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val todoItemId = arguments?.getString(ID_KEY)
        if (todoItemId != null) {
            todoItem = viewModel.getTodoItem(todoItemId)
            viewModel.setNewTodoItemDeadline(todoItem?.deadlineDate)
            viewModel.setNewTodoItemText(todoItem?.text ?: "")
            viewModel.setNewTodoItemImportance(todoItem?.importance)
        }

        return ComposeView(requireContext()).apply {
            setContent {
                AppTheme {
                    TodoItemScreen(
                        todoItemText = viewModel.newTodoItemText.collectAsState(),
                        todoItemImportance = viewModel.newTodoItemImportance.collectAsState(),
                        todoItemDeadline = viewModel.newTodoItemDeadline.collectAsState()
                    )
                }
            }
        }
    }

    @Composable
    private fun TodoItemScreen(
        todoItemText: State<String>,
        todoItemImportance: State<Importance?>,
        todoItemDeadline: State<Calendar?>
    ) {
        return Scaffold(
            topBar = { TopAppBar() }
        ) { padding ->
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .verticalScroll(rememberScrollState()),

            ) {
                TodoTextInput(todoItemText)
                ImportanceMenu(todoItemImportance)
                DeadlineGroup(todoItemDeadline)
                DeleteButton()
            }
        }
    }

    @Preview
    @Composable
    private fun TodoItemScreenLightPreview() {
        val todoItemText = remember { mutableStateOf("") }
        val todoItemImportance = remember { mutableStateOf(null) }
        val todoItemDeadline = remember { mutableStateOf(null) }
        return AppTheme(darkTheme = false) {
            TodoItemScreen(todoItemText, todoItemImportance, todoItemDeadline)
        }
    }

    @Preview
    @Composable
    private fun TodoItemScreenDarkPreview() {
        val todoItemText = remember { mutableStateOf("") }
        val todoItemImportance = remember { mutableStateOf(null) }
        val todoItemDeadline = remember { mutableStateOf(null) }
        return AppTheme(darkTheme = true) {
            TodoItemScreen(todoItemText, todoItemImportance, todoItemDeadline)
        }
    }


    @Composable
    private fun TopAppBar() {
        return TopAppBar(
            backgroundColor = MaterialTheme.colors.primary
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Icon(
                    Icons.Default.Close,
                    contentDescription = null,
                    tint = MaterialTheme.colors.onPrimary
                )
                TextButton(
                    onClick = {
                        if (todoItem == null) {
                            viewModel.addTodoItem(
                                text = viewModel.newTodoItemText.value,
                                deadline = viewModel.newTodoItemDeadline.value,
                                importance = viewModel.newTodoItemImportance.value ?:
                                                Importance.NO_IMPORTANCE
                            )
                        } else {
                            todoItem?.text = viewModel.newTodoItemText.value
                            todoItem?.deadlineDate = viewModel.newTodoItemDeadline.value
                            todoItem?.importance = viewModel.newTodoItemImportance.value ?:
                                    Importance.NO_IMPORTANCE
                            viewModel.saveTodoItem(todoItem!!)
                        }
                        findNavController().popBackStack()
                    },
                ) {
                    Text(
                        text = stringResource(R.string.save).uppercase(),
                        style = MaterialTheme.typography.button,
                        color = Blue,
                    )
                }
            }
        }
    }

    @Composable
    private fun TodoTextInput(
        todoItemText: State<String>
    ) {
        return OutlinedTextField(
            value = todoItemText.value,
            onValueChange = { value -> viewModel.setNewTodoItemText(value) },
            label = { Text(stringResource(R.string.task_hint)) },
            modifier = Modifier
                .fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Blue,
                cursorColor = Blue,
                focusedLabelColor = Blue
            ),
            minLines = 5
        )
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    private fun ImportanceMenu(
        todoItemImportance: State<Importance?>
    ) {
        val importanceStrings = stringArrayResource(R.array.importance_array)
        val importanceMap = Importance.values().zip(importanceStrings).toMap()
        var expanded by remember { mutableStateOf(false) }

        return ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {
                expanded = it
            },
            modifier = Modifier
                .padding(top = 16.dp)
        ) {

            TextField(
                value = importanceMap.getOrDefault(
                    todoItemImportance.value,
                    ""),
                label = { Text(stringResource(R.string.importance)) },
                onValueChange = {},
                readOnly = true,
                enabled = false,
                colors = TextFieldDefaults.textFieldColors(
                    disabledTextColor = MaterialTheme.colors.onPrimary
                ),
                modifier = Modifier
                    .fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                Importance.values().forEach { item ->
                    DropdownMenuItem(
                        onClick = {
                            viewModel.setNewTodoItemImportance(item)
                            expanded = false
                        }
                    ) {
                        Text(importanceMap[item]!!)
                    }
                }
            }
        }
    }

    @Composable
    private fun DeadlineGroup(
        todoDeadline: State<Calendar?>
    ) {
        return Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        ) {
            DeadlineDate(todoDeadline = todoDeadline)
            DeadlineSwitch(todoDeadline = todoDeadline)
        }
    }

    @Composable
    private fun DeadlineDate(
        todoDeadline: State<Calendar?>
    ) {
        return Column {
            Text(
                stringResource(R.string.deadline),
                style = MaterialTheme.typography.body1
            )
            if (todoDeadline.value != null)
                Text(
                    text = DateUtils.formatDate(todoDeadline.value!!),
                    style = MaterialTheme.typography.subtitle1,
                    color = Blue
                )
        }
    }

    @Composable
    private fun DeadlineSwitch(
        todoDeadline: State<Calendar?>
    ) {
        var checked by remember { mutableStateOf(todoDeadline.value != null) }
        return Switch(
            checked = checked,
            onCheckedChange = {
                checked = it
                if (it)
                    showDateDialog()
                else
                    viewModel.setNewTodoItemDeadline(null)
            },
            colors = SwitchDefaults.colors()
        )
    }

    @Composable
    private fun DeleteButton() {
        return TextButton(
            enabled = todoItem != null,
            onClick = {
                viewModel.deleteTodoItem(todoItem!!)
                findNavController().popBackStack()
            }
        ) {
            Icon(
                Icons.Default.Delete,
                contentDescription = null,
                tint = Red
            )
            Text(
                text = stringResource(R.string.delete),
                style = MaterialTheme.typography.button,
                color = Red
            )
        }
    }

    private fun showDateDialog() {
        val picker = DatePickerDialog(
                requireContext(), { datePicker, year, month, day ->
                    viewModel.setNewTodoItemDeadline(
                        Calendar.getInstance().apply {
                            this.set(year, month, day)
                    })
                },
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH),
            )
            // Установка сегодняшней даты в качестве минимальной даты
            picker.datePicker.minDate = System.currentTimeMillis()
            // Подписывается на отмену выбора даты
            picker.setOnCancelListener { viewModel.setNewTodoItemDeadline(null) }

            // Отображение диалога
            picker.show()
            picker.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(
                requireContext().getColor(R.color.red))
            picker.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(
                requireContext().getColor(R.color.blue))
    }

    override fun onDestroy() {
        super.onDestroy()

        todoItem = null
    }


}