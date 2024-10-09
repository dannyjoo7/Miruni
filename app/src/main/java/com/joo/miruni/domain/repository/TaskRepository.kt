package com.joo.miruni.domain.repository

import com.joo.miruni.data.entities.TaskEntity
import com.joo.miruni.data.entities.TaskItemsEntity
import com.joo.miruni.domain.model.TodoEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.LocalDateTime

interface TaskRepository {
    suspend fun getTasksForDateRange(start: LocalDate, end: LocalDate): List<TaskEntity>
    suspend fun getTasksForAlarmDisplayDateRange(start: LocalDate, end: LocalDate): List<TaskEntity>
    suspend fun addTask(todoEntity: TodoEntity)
    suspend fun getTasksForAlarmByDate(
        selectDate: LocalDateTime,
        lastDeadLine: LocalDateTime? = null,
    ): Flow<TaskItemsEntity>

    suspend fun deleteTaskById(id: Long)
}
