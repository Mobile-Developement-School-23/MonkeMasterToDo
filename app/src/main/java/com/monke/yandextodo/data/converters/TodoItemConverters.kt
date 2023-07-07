package com.monke.yandextodo.data.converters

import com.monke.yandextodo.data.localStorage.roomModels.TodoItemRoom
import com.monke.yandextodo.data.networkService.pojo.TodoItemPojo
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
            modifiedDate = calendarFromLong(todoItemRoom.modifiedDate),
            lastUpdatedBy = todoItemRoom.lastUpdatedBy
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
            modifiedDate = calendarToLong(todoItem.modifiedDate),
            lastUpdatedBy = todoItem.lastUpdatedBy
        )
    }

    private fun calendarFromLong(time: Long?): Calendar? {
        if (time == null)
            return null
        return Calendar.getInstance().also { it.timeInMillis = time }
    }

    private fun calendarToLong(calendar: Calendar?): Long? {
        if (calendar == null)
            return null
        return calendar.timeInMillis
    }

    private fun importanceToString(importance: Importance): String {
        if (importance == Importance.NO_IMPORTANCE)
            return "basic"
        if (importance == Importance.LOW)
            return "low"
        return "important"
    }

    private fun importanceFromString(importance: String): Importance {
        if (importance == "basic")
            return Importance.NO_IMPORTANCE
        if (importance == "low")
            return Importance.LOW
        return Importance.HIGH
    }

    fun modelToPojo(todoItem: TodoItem): TodoItemPojo {
        return TodoItemPojo(
            id = todoItem.id,
            text = todoItem.text,
            deadline = calendarToLong(todoItem.deadlineDate),
            done = todoItem.completed,
            created_at = calendarToLong(todoItem.creationDate)!!,
            changed_at = calendarToLong(todoItem.modifiedDate),
            importance = importanceToString(todoItem.importance),
            color = todoItem.color,
            last_updated_by = todoItem.lastUpdatedBy
        )
    }

    fun modelFromPojo(todoItemPojo: TodoItemPojo): TodoItem {

        return TodoItem(
            id = todoItemPojo.id,
            text = todoItemPojo.text,
            importance = importanceFromString(todoItemPojo.importance),
            completed = todoItemPojo.done,
            deadlineDate = calendarFromLong(todoItemPojo.deadline),
            creationDate = calendarFromLong(todoItemPojo.created_at)!!,
            modifiedDate = calendarFromLong(todoItemPojo.changed_at),
            color = todoItemPojo.color,
            lastUpdatedBy = todoItemPojo.last_updated_by
        )
    }
}