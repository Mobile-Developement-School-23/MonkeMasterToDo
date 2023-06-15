package com.monke.yandextodo.view

import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout
import com.monke.yandextodo.R
import com.monke.yandextodo.databinding.FragmentTaskBinding
import com.monke.yandextodo.model.TodoItem

class TaskFragment(todoItem: TodoItem? = null) : Fragment() {

    private val todoItem = todoItem

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentTaskBinding.inflate(layoutInflater)

        val importanceMenu = binding.importanceMenu
        val arrayAdapter = ArrayAdapter(context!!, R.layout.item_dropdown,
            resources.getStringArray(R.array.importance_array))
        importanceMenu.setAdapter(arrayAdapter)

        if (todoItem != null) {
            val textEditTxt = binding.textEditTxt
            textEditTxt.setText(todoItem.text)
            importanceMenu.setText(resources.getStringArray(R.array.importance_array)[todoItem.importance])
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

        return binding.root
    }


}