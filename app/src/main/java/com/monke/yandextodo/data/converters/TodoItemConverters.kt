package com.monke.yandextodo.data.converters

import androidx.room.TypeConverter
import com.monke.yandextodo.data.localStorage.roomModels.TodoItemRoom
import com.monke.yandextodo.domain.Importance
import com.monke.yandextodo.domain.TodoItem
import java.util.*

object TodoItemConverters {

    fun roomToModel(todoItemRoom: TodoItemRoom): TodoItem {
        return TodoItem(
            id = todoItemRoom.id,
            text = todoItemRoom.text,
            deadlineDate = calendarFromLong(todoItemRoom.deadlineDate),
            importance = todoItemRoom.importance,
            completed = todoItemRoom.completed,
            creationDate = calendarFromLong(todoItemRoom.creationDate)!!,
            modifiedDate = calendarFromLong(todoItemRoom.modifiedDate)
        )
    }

    fun modelToRoom(todoItem: TodoItem): TodoItemRoom {
        return TodoItemRoom(
            id = todoItem.id,
            text = todoItem.text,
            deadlineDate = calendarToLong(todoItem.deadlineDate),
            importance = todoItem.importance,
            completed = todoItem.completed,
            creationDate = calendarToLong(todoItem.creationDate)!!,
            modifiedDate = calendarToLong(todoItem.modifiedDate)
        )
    }

    fun calendarFromLong(time: Long?): Calendar? {
        if (time == null)
            return null
        return Calendar.getInstance().also { it.timeInMillis = time }
    }

    fun calendarToLong(calendar: Calendar?): Long? {
        if (calendar == null)
            return null
        return calendar.timeInMillis
    }

    @TypeConverter
    fun importanceToInt(importance: Importance): Int {
        if (importance == Importance.NO_IMPORTANCE)
            return 0
        else if (importance == Importance.LOW)
            return 1
        return 2
    }

    @TypeConverter
    fun intToImportance(importance: Int): Importance {
        if (importance == 0)
            return Importance.NO_IMPORTANCE
        else if (importance == 1)
            return Importance.LOW
        return Importance.HIGH
    }
}