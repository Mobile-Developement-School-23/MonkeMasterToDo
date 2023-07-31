package com.monke.yandextodo.ioc.components

import com.monke.yandextodo.presentation.todoItemFeature.fragments.TodoItemListFragment
import dagger.Subcomponent

@Subcomponent
interface TodoItemsListFragmentComponent {

    fun inject(fragment: TodoItemListFragment)

}