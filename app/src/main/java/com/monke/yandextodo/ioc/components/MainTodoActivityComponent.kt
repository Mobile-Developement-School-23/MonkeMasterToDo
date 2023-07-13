package com.monke.yandextodo.ioc.components

import com.monke.yandextodo.ioc.scopes.MainTodoActivityScope
import com.monke.yandextodo.presentation.MainTodoActivity
import com.monke.yandextodo.presentation.settingsFeature.SettingsFragment
import dagger.Subcomponent

@Subcomponent
@MainTodoActivityScope
interface MainTodoActivityComponent {

    fun inject(activity: MainTodoActivity)

    fun inject(settingsFragment: SettingsFragment)

    fun todoItemFragmentComponent(): TodoItemFragmentComponent

    fun todoItemListFragmentComponent(): TodoItemsListFragmentComponent

    fun synchronizationComponent(): SynchronizationDialogComponent
}