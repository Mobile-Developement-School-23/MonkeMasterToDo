package com.monke.yandextodo.ioc.components

import com.monke.yandextodo.presentation.todoItemFeature.fragments.TodoItemFragment
import dagger.Subcomponent

@Subcomponent
interface TodoItemFragmentComponent {

    fun inject(fragment: TodoItemFragment)


}