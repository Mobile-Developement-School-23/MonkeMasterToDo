package com.monke.yandextodo.view

import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.TextView
import com.google.android.material.textfield.TextInputLayout
import com.monke.yandextodo.R
import com.monke.yandextodo.model.TodoItem

class TaskFragment(todoItem: TodoItem? = null) : Fragment() {

    private val todoItem = todoItem

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_task, container, false)

        val importanceMenu = view.findViewById<AutoCompleteTextView>(R.id.importance_menu)
        val arrayAdapter = ArrayAdapter(context!!, R.layout.item_dropdown,
            resources.getStringArray(R.array.importance_array))
        importanceMenu.setAdapter(arrayAdapter)

        if (todoItem != null) {
            val textEditTxt = view.findViewById<EditText>(R.id.text_edit_txt)
            textEditTxt.setText(todoItem.text)
            importanceMenu.setText(resources.getStringArray(R.array.importance_array)[todoItem.importance])
        }

        return view
    }


}