package com.joo.miruni.domain.repository

import com.joo.miruni.data.entities.TaskEntity
import com.joo.miruni.domain.model.TodoEntity
import java.time.LocalDate

interface TaskRepository {
    suspend fun getTasksForDateRange(start: LocalDate, end: LocalDate): List<TaskEntity>
    suspend fun getTasksForAlarmDisplayDateRange(start: LocalDate, end: LocalDate): List<TaskEntity>
    suspend fun addTask(todoEntity: TodoEntity)
}
