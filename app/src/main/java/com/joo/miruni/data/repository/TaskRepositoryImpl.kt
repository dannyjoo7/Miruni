package com.joo.miruni.data.repository

import com.joo.miruni.data.database.TaskDao
import com.joo.miruni.data.entities.TaskEntity
import com.joo.miruni.data.entities.TaskItemsEntity
import com.joo.miruni.domain.model.TodoEntity
import com.joo.miruni.domain.model.toTaskEntity
import com.joo.miruni.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.LocalDate
import java.time.LocalDateTime
import javax.inject.Inject


class TaskRepositoryImpl @Inject constructor(private val taskDao: TaskDao) : TaskRepository {

    override suspend fun getTasksForDateRange(start: LocalDate, end: LocalDate): List<TaskEntity> {
        return taskDao.getTasksForDateRange(start, end)
    }

    override suspend fun getTasksForAlarmDisplayDateRange(
        start: LocalDate,
        end: LocalDate,
    ): List<TaskEntity> {
        return taskDao.getTasksForAlarmDisplayDateRange(start, end)
    }

    override suspend fun addTask(todoEntity: TodoEntity) {
        taskDao.insertTask(todoEntity.toTaskEntity())
    }

    override suspend fun getTasksForAlarmByDate(
        selectDate: LocalDateTime,
        lastDeadLine: LocalDateTime?,
    ): Flow<TaskItemsEntity> {
        return flow {
            try {
                taskDao.getTodoTasksPaginated(selectDate, lastDeadLine)
                    .collect { taskEntities ->
                        emit(TaskItemsEntity(taskEntities))
                    }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override suspend fun deleteTaskById(id: Long) {
        taskDao.deleteTaskById(id)
    }

    override suspend fun markTaskAsCompleted(id: Long, completionTime: LocalDateTime) {
        taskDao.updateTaskCompletionStatus(id, true, completionTime)
    }


    override suspend fun getTodoItemById(taskId: Long): TaskEntity {
        return taskDao.getTodoItemById(taskId)
    }

}