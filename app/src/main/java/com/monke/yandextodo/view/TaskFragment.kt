package com.monke.yandextodo.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.TextView
import com.google.android.material.textfield.TextInputLayout
import com.monke.yandextodo.R

class TaskFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_task, container, false)

        val importanceMenu = view.findViewById<AutoCompleteTextView>(R.id.importance_menu)
        val arrayAdapter = ArrayAdapter(context!!, R.layout.item_dropdown,
            resources.getStringArray(R.array.importance_array))
        importanceMenu.setAdapter(arrayAdapter)

        return view
    }


}