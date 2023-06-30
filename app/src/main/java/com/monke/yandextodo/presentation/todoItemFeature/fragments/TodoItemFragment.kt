package com.monke.yandextodo.presentation.todoItemFeature.fragments

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import com.google.android.material.button.MaterialButton
import com.monke.yandextodo.App
import com.monke.yandextodo.R
import com.monke.yandextodo.databinding.FragmentTaskBinding
import com.monke.yandextodo.domain.Importance
import com.monke.yandextodo.domain.TodoItem
import com.monke.yandextodo.presentation.todoItemFeature.viewModels.TodoItemViewModel
import com.monke.yandextodo.utils.DateUtils
import java.util.Calendar
import javax.inject.Inject

class TodoItemFragment: Fragment() {

    @Inject
    lateinit var viewModel: TodoItemViewModel
    private var binding: FragmentTaskBinding? = null
    private var todoItem: TodoItem? = null
    private var deadlineDate: Calendar? = null

    companion object {

        private const val ID_KEY = "id"

        fun newInstance(todoId: String? = null): TodoItemFragment =
            TodoItemFragment().apply {
                arguments = Bundle().apply {
                    putString(ID_KEY, todoId)
                }
            }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentTaskBinding.inflate(layoutInflater)
        this.binding = binding

        (activity?.applicationContext as App).todoItemFragmentComponent.inject(this)

        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        var todoItemId = arguments?.getString(ID_KEY)
        if (todoItemId != null) {
            todoItem = viewModel.getTodoItem(todoItemId)
            deadlineDate = todoItem?.deadlineDate
        }

        configureImportanceMenu(view.context)
        configureDeadlineText()
        configureTextView()
        configureDeleteButton(view.context)
        configureSaveButton()
        configureCloseButton()
        configureDeadlineSwitch()
    }


    // Настройка меню выбора важности
    private fun configureImportanceMenu(context: Context) {
        val importanceMenu = binding?.importanceMenu
        val arrayAdapter = ArrayAdapter(context, R.layout.item_dropdown,
            resources.getStringArray(R.array.importance_array))
        importanceMenu?.setAdapter(arrayAdapter)

        // Если режим редактирования, то показывает важность задачи
        if (todoItem != null) {
            when (todoItem?.importance) {
                Importance.NO_IMPORTANCE -> importanceMenu?.setText(
                    resources.getStringArray(R.array.importance_array)[0], false)
                Importance.LOW -> importanceMenu?.setText(
                    resources.getStringArray(R.array.importance_array)[1], false)
                else -> importanceMenu?.setText(
                    resources.getStringArray(R.array.importance_array)[2], false)
            }
        }

    }

    // Настройка кнопки удаления
    private fun configureDeleteButton(context: Context) {
        binding?.deleteBtn?.setOnClickListener {
            // Удаление задачи
            if (todoItem != null)
                todoItem?.let { it1 -> viewModel.deleteTodoItem(it1) }
            parentFragmentManager.popBackStack()
        }
        // Если режим создания, то отключает кнопку удаления
        if (todoItem == null) {
            (binding?.deleteBtn as MaterialButton).iconTint =
                context.getColorStateList(R.color.disable)
            binding?.deleteBtn?.setTextColor(context.getColorStateList(R.color.disable))
            binding?.deleteBtn?.isEnabled = false
        }

    }

    // Настройка виджетов с датой дедлайна
    private fun configureDeadlineText() {
        // Если у задачи есть дедлайн, отображает дату
        if (todoItem?.deadlineDate != null) {
            val day = todoItem?.deadlineDate!!.get(Calendar.DAY_OF_MONTH)
            val month = todoItem?.deadlineDate!!.get(Calendar.MONTH)
            val year = todoItem?.deadlineDate!!.get(Calendar.YEAR)
            binding?.deadlineTxt?.text = DateUtils.formatDate(day, month, year)
        }
    }

    // Настройка switch для выбора дедлайна
    private fun configureDeadlineSwitch() {
        binding?.deadlineSwitch?.isChecked = todoItem?.deadlineDate != null

        // Выбор даты дедлайна при нажатии на switch
        binding?.deadlineSwitch?.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                // Создание диалога с выбором даты
                val picker = DatePickerDialog(
                    context!!, { datePicker, year, month, day ->
                        // Форматирует и отображает дату после выбора
                        binding?.deadlineTxt?.visibility = View.VISIBLE
                        binding?.deadlineTxt?.text = DateUtils.formatDate(
                            day,
                            month + 1,
                            year)
                        deadlineDate = Calendar.getInstance().apply {
                            this.set(year, month, day)
                        }
                    },
                    Calendar.getInstance().get(Calendar.YEAR),
                    Calendar.getInstance().get(Calendar.MONTH),
                    Calendar.getInstance().get(Calendar.DAY_OF_MONTH),
                )
                // Установка сегодняшней даты в качестве минимальной даты
                picker.datePicker.minDate = System.currentTimeMillis()
                // Подписывается на отмену выбора даты
                picker.setOnCancelListener { binding?.deadlineSwitch?.isChecked = false }

                // Отображение диалога
                picker.show()
                picker.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(
                    context!!.getColor(R.color.red))
                picker.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(
                    context!!.getColor(R.color.blue))
            }
            else {
                // Если пользователь отменил выбор даты дедлайна
                // Скрывает строку с датой
                binding?.deadlineTxt?.text = ""
                binding?.deadlineTxt?.visibility = View.INVISIBLE
            }
        }
    }

    // Настройка кнопки сохранения
    private fun configureSaveButton() {
        // binding?.saveBtn?.isEnabled = false
        binding?.saveBtn?.setOnClickListener {
            // Добавление задания
            todoItem.let {
                if (it == null) {
                    // Получение введенных данных
                    val text = binding?.textEditTxt?.text.toString()
                    val importanceString = binding?.importanceMenu?.text.toString()
                    var importance: Importance?
                    importance = getImportance(importanceString)

                    // Добавление задачи
                    viewModel.addTodoItem(text, deadlineDate, importance)
                } else {
                    // Получение введенных данных
                    val text = binding?.textEditTxt?.text.toString()
                    val importanceString = binding?.importanceMenu?.text.toString()
                    var importance: Importance?
                    importance = getImportance(importanceString)

                    // Обновление данных задачи
                    it.deadlineDate =  deadlineDate
                    it.text = text
                    it.importance = importance

                    // Сохранение задачи
                    viewModel.saveTodoItem(it)
                }
            }

            parentFragmentManager.popBackStack()
        }
    }

    // Настройка кнопки закрытия
    private fun configureCloseButton() {
        binding?.closeBtn?.setOnClickListener { parentFragmentManager.popBackStack() }
    }

    // Настройка TextView с текстом задачи
    private fun configureTextView() {
        // Если режим редактирования, отображает текст задачи
        binding?.textEditTxt?.setText(todoItem?.text)
    }

    private fun getImportance(importanceString: String): Importance {
        return when (importanceString) {
            resources.getStringArray(R.array.importance_array)[0] ->
                Importance.NO_IMPORTANCE
            resources.getStringArray(R.array.importance_array)[1] ->
                Importance.LOW
            else -> Importance.HIGH
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        binding = null
        todoItem = null
    }



}