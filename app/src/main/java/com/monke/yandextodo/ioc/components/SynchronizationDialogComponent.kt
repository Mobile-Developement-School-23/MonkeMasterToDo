package com.monke.yandextodo.ioc.components

import com.monke.yandextodo.presentation.todoItemFeature.dialogs.SynchronizationDialog
import dagger.Subcomponent

@Subcomponent
interface SynchronizationDialogComponent {

    fun inject(dialog: SynchronizationDialog)
}