package com.monke.yandextodo.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.monke.yandextodo.R
import com.monke.yandextodo.presentation.taskFeature.fragments.TasksListFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction().add(
            R.id.fragmentContainerView,
            TasksListFragment()
        ).commit()

    }

    fun setFragmentAndAddToStack(fragment: Fragment) {
        supportFragmentManager.beginTransaction().add(
            R.id.fragmentContainerView,
            fragment).addToBackStack(fragment.tag).commit()
    }




}