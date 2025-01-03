package com.joo.miruni.domain.model

import com.joo.miruni.data.entities.TaskEntity
import com.joo.miruni.data.entities.TaskItemsEntity
import com.joo.miruni.data.entities.TaskType
import com.joo.miruni.presentation.addTask.addTodo.TodoItem
import java.time.LocalDate

/*
* TaskEntity to TodoEntity*/

// TaskItemsEntity to TodoItemsEntity
fun TaskItemsEntity.toTodoItemsModel() = TodoItemsModel(
    todoEntities = taskItemsEntity.map { it.toTodoModel() }
)

// TaskEntity to TodoEntity
fun TaskEntity.toTodoModel() = TodoModel(
    id = id,
    title = title,
    details = details,
    deadLine = deadLine,
    alarmDisplayDate = alarmDisplayDate ?: LocalDate.now(),
    isComplete = isComplete,
    completeDate = completeDate,
    type = type,
    isPinned = isPinned
)

/*
* TodoEntity to TaskEntity
* */

// TodoEntity to TaskEntity
fun TodoModel.toTaskEntity() = TaskEntity(
    id = id,
    title = title,
    details = details,
    startDate = null,
    endDate = null,
    deadLine = deadLine,
    alarmDisplayDate = alarmDisplayDate,
    isComplete = isComplete,
    completeDate = completeDate,
    type = type,
    isPinned = isPinned
)

/*
* TodoItem to TodoEntity
* */

// AddTodo
fun TodoItem.toTodoModel() = TodoModel(
    id = id,
    title = title,
    details = descriptionText,
    deadLine = selectedDate,
    alarmDisplayDate = adjustedDate,
    isComplete = false,
    completeDate = null,
    type = TaskType.TODO,
    isPinned = isPinned
)