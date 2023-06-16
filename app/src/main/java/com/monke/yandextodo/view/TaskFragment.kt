package com.monke.yandextodo.view

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.google.android.material.button.MaterialButton
import com.monke.yandextodo.R
import com.monke.yandextodo.databinding.FragmentTaskBinding
import com.monke.yandextodo.model.TodoItem
import com.monke.yandextodo.utils.DateUtils
import java.util.Calendar

class TaskFragment(private val todoItem: TodoItem? = null) : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val binding = FragmentTaskBinding.inflate(layoutInflater)

        val importanceMenu = binding.importanceMenu
        val arrayAdapter = ArrayAdapter(context!!, R.layout.item_dropdown,
            resources.getStringArray(R.array.importance_array))
        importanceMenu.setAdapter(arrayAdapter)

        // Если пользователь редактирует todoItem
        if (todoItem != null) {
            // Отображает данные todoItem
            val textEditTxt = binding.textEditTxt
            textEditTxt.setText(todoItem.text)

            importanceMenu.setText(resources.getStringArray(
                R.array.importance_array)[todoItem.importance])

            // Если у задачи есть дедлайн, отображает дату
            if (todoItem.deadlineDate != null) {
                val day = todoItem.deadlineDate!!.get(Calendar.DAY_OF_MONTH)
                val month = todoItem.deadlineDate!!.get(Calendar.MONTH)
                val year = todoItem.deadlineDate!!.get(Calendar.YEAR)
                binding.deadlineTxt.text = DateUtils.formatDate(day, month, year)
                //binding.deadlineTxt.visibility = View.VISIBLE

                binding.deadlineSwitch.isChecked = true
            }
        } else {
            (binding.deleteBtn as MaterialButton).iconTint = context!!.getColorStateList(R.color.disable)
            binding.deleteBtn.setTextColor(context!!.getColorStateList(R.color.disable))
            binding.deleteBtn.isEnabled = false
        }

        binding.closeBtn.setOnClickListener {
            parentFragmentManager.beginTransaction().replace(R.id.fragmentContainerView, ListFragment()).commit()}

        binding.saveBtn.setOnClickListener {
            parentFragmentManager.beginTransaction().replace(R.id.fragmentContainerView, ListFragment()).commit()}

        binding.deleteBtn.setOnClickListener {
            parentFragmentManager.beginTransaction().replace(R.id.fragmentContainerView, ListFragment()).commit()}


        // Выбор даты дедлайна при нажатии на switch
        binding.deadlineSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                // Создание диалога с выбором даты
                val picker = DatePickerDialog(
                    context!!, { datePicker, year, month, day ->
                        // Форматирует и отображает дату после выбора
                        binding.deadlineTxt.visibility = View.VISIBLE
                        binding.deadlineTxt.text = DateUtils.formatDate(day, month + 1, year)
                    },
                    Calendar.getInstance().get(Calendar.YEAR),
                    Calendar.getInstance().get(Calendar.MONTH),
                    Calendar.getInstance().get(Calendar.DAY_OF_MONTH),
                )
                // Установка сегодняшней даты в качестве минимальной даты
                picker.datePicker.minDate = System.currentTimeMillis()
                // Подписывается на отмену выбора даты
                picker.setOnCancelListener { binding.deadlineSwitch.isChecked = false }

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
                binding.deadlineTxt.text = ""
                binding.deadlineTxt.visibility = View.INVISIBLE
            }
         }

        return binding.root
    }


}