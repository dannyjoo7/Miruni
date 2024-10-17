package com.joo.miruni.domain.model

import com.joo.miruni.data.entities.TaskEntity
import com.joo.miruni.data.entities.TaskItemsEntity
import com.joo.miruni.data.entities.TaskType
import com.joo.miruni.presentation.addTask.addTodo.TodoItem

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
    deadLine = deadLine,
    alarmDisplayDate = alarmDisplayDate,
    isComplete = isComplete,
    completeDate = completeDate,
    type = type,
)

/*
* TodoEntity to TaskEntity
* */

// TodoEntity to TaskEntity
fun TodoEntity.toTaskEntity() = TaskEntity(
    id = id,
    title = title,
    details = details,
    startDate = null,
    endDate = null,
    deadLine = deadLine,
    alarmDisplayDate = alarmDisplayDate,
    isComplete = isComplete,
    completeDate = completeDate,
    type = type
)

/*
* TodoItem to TodoEntity
* */

// AddTodo
fun TodoItem.toTodoEntity() = TodoEntity(
    id = 0,
    title = todoText,
    details = descriptionText,
    deadLine = selectedDate,
    alarmDisplayDate = adjustedDate,
    isComplete = false,
    completeDate = null,
    type = TaskType.TODO,
)

// Modify
fun com.joo.miruni.presentation.detailPage.TodoItem.toTodoEntity() = TodoEntity(
    id = id!!,
    title = todoText,
    details = descriptionText,
    deadLine = selectedDate,
    alarmDisplayDate = adjustedDate!!,
    isComplete = isComplete,
    completeDate = null,
    type = TaskType.TODO,
)