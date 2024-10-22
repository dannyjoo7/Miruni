package com.joo.miruni.domain.repository

import com.joo.miruni.data.entities.TaskEntity
import com.joo.miruni.data.entities.TaskItemsEntity
import com.joo.miruni.domain.model.ScheduleEntity
import com.joo.miruni.domain.model.TodoEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.LocalDateTime

interface TaskRepository {
    suspend fun getTasksForDateRange(start: LocalDate, end: LocalDate): List<TaskEntity>
    suspend fun getTasksForAlarmDisplayDateRange(start: LocalDate, end: LocalDate): List<TaskEntity>
    suspend fun addTodo(todoEntity: TodoEntity)
    suspend fun getTasksForAlarmByDate(
        selectDate: LocalDateTime,
        lastDeadLine: LocalDateTime? = null,
    ): Flow<TaskItemsEntity>

    suspend fun deleteTaskById(id: Long)
    suspend fun markTaskAsCompleted(id: Long, completionTime: LocalDateTime)
    suspend fun markTaskAsCancelCompleted(id: Long)
    suspend fun getTodoItemById(taskId: Long): TaskEntity
    suspend fun updateTask(todoEntity: TodoEntity)
    suspend fun delayTodoEntity(id: Long, delayDateTime: LocalDateTime)
    suspend fun getOverdueTaskEntities(date: LocalDateTime): Flow<TaskItemsEntity>
    suspend fun addSchedule(scheduleEntity: ScheduleEntity)
    suspend fun getSchedules(selectDate: LocalDate, lastStartDate: LocalDate?): Flow<TaskItemsEntity>
}
