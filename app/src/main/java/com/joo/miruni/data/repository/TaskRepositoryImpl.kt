package com.joo.miruni.data.repository

import com.joo.miruni.data.database.TaskDao
import com.joo.miruni.data.entities.TaskEntity
import com.joo.miruni.data.entities.TaskItemsEntity
import com.joo.miruni.domain.model.ScheduleModel
import com.joo.miruni.domain.model.TodoModel
import com.joo.miruni.domain.model.toTaskEntity
import com.joo.miruni.domain.model.toTodoEntity
import com.joo.miruni.domain.repository.TaskRepository
import com.joo.miruni.service.notification.ReminderManagerUtil
import com.joo.miruni.service.notification.ReminderType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import javax.inject.Inject


class TaskRepositoryImpl @Inject constructor(
    private val taskDao: TaskDao,
    private val reminderManagerUtil: ReminderManagerUtil,
) : TaskRepository {
    companion object {
        const val TAG = "TaskRepositoryImpl"
    }

    // 날짜 범위로 Task 가져오기
    override suspend fun getTasksForDateRange(
        start: LocalDate,
        end: LocalDate,
    ): Flow<TaskItemsEntity> {
        return taskDao.getTasksForDateRange(start, end).map { taskEntities ->
            TaskItemsEntity(taskEntities)
        }
    }

    override suspend fun getTasksForAlarmDisplayDateRange(
        start: LocalDate,
        end: LocalDate,
    ): List<TaskEntity> {
        return taskDao.getTasksForAlarmDisplayDateRange(start, end)
    }

    override suspend fun addTodo(todoModel: TodoModel) {
        val newId = taskDao.insertTask(todoModel.toTaskEntity())
        val updatedTodoEntity = todoModel.copy(id = newId)
        scheduleReminderForTodoItem(
            updatedTodoEntity.id,
            updatedTodoEntity.title,
            updatedTodoEntity.deadLine
        )
    }

    // 날짜로 Task 가져오기
    override suspend fun getTasksForAlarmByDate(
        selectDate: LocalDateTime,
    ): Flow<TaskItemsEntity> {
        return taskDao.getTodoTasksPaginated(selectDate)
            .map { taskEntities ->
                TaskItemsEntity(taskEntities)
            }
    }

    override suspend fun deleteTaskById(id: Long) {
        taskDao.deleteTaskById(id)
        cancelAlarmsForTodoItem(id)
    }

    override suspend fun updateTask(taskEntity: TaskEntity) {
        taskDao.updateTask(taskEntity)
        cancelAlarmsForTodoItem(taskEntity.id)
        scheduleReminderForTodoItem(taskEntity.id, taskEntity.title, taskEntity.deadLine)
    }

    override suspend fun markTaskAsCompleted(id: Long, completionTime: LocalDateTime) {
        taskDao.updateTaskCompletionStatus(id, true, completionTime)
        cancelAlarmsForTodoItem(id)
    }

    override suspend fun markTaskAsCancelCompleted(id: Long) {
        taskDao.updateTaskCompletionStatus(id, false, null)
        val taskEntity = taskDao.getTaskItemById(id).toTodoEntity()
        scheduleReminderForTodoItem(taskEntity.id, taskEntity.title, taskEntity.deadLine)
    }

    override suspend fun getTaskItemById(taskId: Long): TaskEntity {
        return taskDao.getTaskItemById(taskId)
    }

    override suspend fun delayTodoEntity(id: Long, delayDateTime: LocalDateTime) {
        taskDao.delayTask(id, delayDateTime)
        cancelAlarmsForTodoItem(id)
        val taskEntity = taskDao.getTaskItemById(id).toTodoEntity()
        scheduleReminderForTodoItem(taskEntity.id, taskEntity.title, taskEntity.deadLine)
    }

    override suspend fun delayAllTodoEntity(itemIds: List<Long>, delayDateTime: LocalDateTime) {
        itemIds.forEach { itemId ->
            delayTodoEntity(itemId, delayDateTime)
        }
    }

    override suspend fun getOverdueTaskEntities(
        currentDateTime: LocalDateTime,
    ): Flow<TaskItemsEntity> {
        return taskDao.getOverdueTasks(
            currentDateTime = currentDateTime,
        )
            .map { taskEntities ->
                TaskItemsEntity(taskItemsEntity = taskEntities)
            }
    }

    override suspend fun addSchedule(scheduleModel: ScheduleModel) {
        taskDao.insertTask(scheduleModel.toTaskEntity())
    }

    override suspend fun getSchedules(
        selectDate: LocalDate,
        lastStartDate: LocalDate?,
    ): Flow<TaskItemsEntity> {
        return taskDao.getScheduleTasksPaginated(selectDate, lastStartDate)
            .map { taskEntities ->
                TaskItemsEntity(taskItemsEntity = taskEntities)
            }
    }

    // 알람 추가
    private fun scheduleReminderForTodoItem(id: Long, title: String, deadLine: LocalDateTime?) {
        if (deadLine != null) {
            val reminderTimeInMillis =
                deadLine.atZone(ZoneId.of("Asia/Seoul")).toInstant().toEpochMilli()

            // 알람 시간 계산
            val oneHourBeforeInMillis = reminderTimeInMillis - 3600000 // 1시간 전
            val tenMinutesBeforeInMillis = reminderTimeInMillis - 600000 // 10분 전
            val fiveMinutesBeforeInMillis = reminderTimeInMillis - 300000 // 5분 전

            // 현재 시간
            val nowInMillis =
                System.currentTimeMillis() - (System.currentTimeMillis() % (60 * 1000))

            // 알람 설정
            when {
                nowInMillis < reminderTimeInMillis -> {
                    // 현재 시간이 마감 시간 이전인 경우
                    when {
                        nowInMillis < oneHourBeforeInMillis -> {
                            // 1시간 전 알람 설정
                            reminderManagerUtil.setExactAlarm(
                                oneHourBeforeInMillis,
                                id.toInt(),
                                title,
                                ReminderType.ONE_HOUR_BEFORE
                            )
                        }

                        nowInMillis < tenMinutesBeforeInMillis -> {
                            // 10분 전 알람 설정
                            reminderManagerUtil.setExactAlarm(
                                tenMinutesBeforeInMillis,
                                id.toInt(),
                                title,
                                ReminderType.TEN_MINUTES_BEFORE
                            )
                        }

                        nowInMillis < fiveMinutesBeforeInMillis -> {
                            // 5분 전 알람 설정
                            reminderManagerUtil.setExactAlarm(
                                fiveMinutesBeforeInMillis,
                                id.toInt(),
                                title,
                                ReminderType.FIVE_MINUTES_BEFORE
                            )
                        }

                        else -> {
                            reminderManagerUtil.setExactAlarm(
                                reminderTimeInMillis,
                                id.toInt(),
                                title,
                                ReminderType.NOW
                            )
                        }
                    }
                }
            }
        }
    }

    // 알람 취소
    private fun cancelAlarmsForTodoItem(id: Long) {
        reminderManagerUtil.cancelAlarmsForTodoItem(id)
    }

    // TODO 알람 테스트
    override fun test() {

    }
}