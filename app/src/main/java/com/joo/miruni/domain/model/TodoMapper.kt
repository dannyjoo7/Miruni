package com.joo.miruni.domain.model

import com.joo.miruni.data.entities.TaskEntity
import com.joo.miruni.data.entities.TaskItemsEntity
import com.joo.miruni.data.entities.TaskType
import com.joo.miruni.presentation.addTask.addTodo.TodoItem
import java.time.LocalDate

/*
* TaskEntity to TodoEntity*/

// TaskItemsEntity to TodoItemsEntity
fun TaskItemsEntity.toTodoItemsEntity() = TodoItemsModel(
    todoEntities = taskItemsEntity.map { it.toTodoEntity() } // 각 TaskEntity를 TodoEntity로 변환
)

// TaskEntity to TodoEntity
fun TaskEntity.toTodoEntity() = TodoModel(
    id = id,
    title = title,
    details = details,
    deadLine = deadLine,
    alarmDisplayDate = alarmDisplayDate ?: LocalDate.now(),
    isComplete = isComplete,
    completeDate = completeDate,
    type = type,
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
    type = type
)

/*
* TodoItem to TodoEntity
* */

// AddTodo
fun TodoItem.toTodoEntity() = TodoModel(
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
fun com.joo.miruni.presentation.detail.detailTodo.TodoItem.toTodoEntity() = TodoModel(
    id = id!!,
    title = todoText,
    details = descriptionText,
    deadLine = selectedDate,
    alarmDisplayDate = adjustedDate!!,
    isComplete = isComplete,
    completeDate = null,
    type = TaskType.TODO,
)