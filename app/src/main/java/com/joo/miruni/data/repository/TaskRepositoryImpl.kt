package com.joo.miruni.data.repository

import com.joo.miruni.data.database.TaskDao
import com.joo.miruni.data.entities.TaskEntity
import com.joo.miruni.data.entities.TaskItemsEntity
import com.joo.miruni.domain.model.TodoEntity
import com.joo.miruni.domain.model.toTaskEntity
import com.joo.miruni.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
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

    // 날짜로 Task 가져오기
    override suspend fun getTasksForAlarmByDate(
        selectDate: LocalDateTime,
        lastDeadLine: LocalDateTime?
    ): Flow<TaskItemsEntity> {
        return taskDao.getTodoTasksPaginated(selectDate, lastDeadLine)
            .map { taskEntities ->
                TaskItemsEntity(taskEntities)
            }
    }


    override suspend fun deleteTaskById(id: Long) {
        taskDao.deleteTaskById(id)
    }

    override suspend fun markTaskAsCompleted(id: Long, completionTime: LocalDateTime) {
        taskDao.updateTaskCompletionStatus(id, true, completionTime)
    }

    override suspend fun markTaskAsCancelCompleted(id: Long) {
        taskDao.updateTaskCompletionStatus(id, false, null)
    }


    override suspend fun getTodoItemById(taskId: Long): TaskEntity {
        return taskDao.getTodoItemById(taskId)
    }

    override suspend fun updateTask(todoEntity: TodoEntity) {
        taskDao.updateTask(todoEntity.toTaskEntity())
    }

    override suspend fun delayTodoEntity(id: Long, delayDateTime: LocalDateTime) {
        taskDao.delayTask(id, delayDateTime)
    }

    override suspend fun getOverdueTaskEntities(date: LocalDateTime): Flow<TaskItemsEntity> {
        return taskDao.getOverdueTasks(currentDateTime = date)
            .map { taskEntities ->
                TaskItemsEntity(taskItemsEntity = taskEntities)
            }
    }

}