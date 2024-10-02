package com.joo.miruni.domain.model

import com.joo.miruni.data.entities.TaskEntity
import com.joo.miruni.data.entities.TaskItemsEntity
import com.joo.miruni.data.entities.TaskType
import com.joo.miruni.presentation.addTodo.TodoItem

/*
* TaskEntity to TodoEntity*/

// TaskItemsEntity to TodoItemsEntity
fun TaskItemsEntity.toTodoItemsEntity() = TodoItemsEntity(
    todoEntities = taskItemsEntity.map { it.toTodoEntity() } // 각 TaskEntity를 TodoEntity로 변환
)

// TaskEntity to TodoEntity
fun TaskEntity.toTodoEntity() = TodoEntity(
    id = id,
    title = title,
    details = details,
    startDate = startDate,
    endDate = endDate,
    deadLine = deadLine,
    deadLineTime = deadLineTime,
    alarmDisplayDate = alarmDisplayDate,
    type = type
)

/*
* TodoEntity to TaskEntity
* */

// TodoEntity to TaskEntity
fun TodoEntity.toTaskEntity() = TaskEntity(
    id = id,
    title = title,
    details = details,
    startDate = startDate,
    endDate = endDate,
    deadLine = deadLine,
    deadLineTime = deadLineTime,
    alarmDisplayDate = alarmDisplayDate,
    type = type
)

/*
* TodoItem to TodoEntity
* */

fun TodoItem.toTodoEntity() = TodoEntity(
    id = 0,
    title = todoText,
    details = descriptionText,
    startDate = null,
    endDate = null,
    deadLine = selectedDate,
    deadLineTime = selectedTime,
    alarmDisplayDate = adjustedDate,
    type = TaskType.TODO,
)