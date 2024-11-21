package com.joo.miruni.data.repository

import com.joo.miruni.data.database.TaskDao
import com.joo.miruni.data.entities.TaskEntity
import com.joo.miruni.data.entities.TaskItemsEntity
import com.joo.miruni.domain.model.ScheduleEntity
import com.joo.miruni.domain.model.TodoEntity
import com.joo.miruni.domain.model.toTaskEntity
import com.joo.miruni.domain.model.toTodoEntity
import com.joo.miruni.domain.repository.TaskRepository
import com.joo.miruni.notifications.AlarmManagerUtil
import com.joo.miruni.notifications.AlarmType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import javax.inject.Inject


class TaskRepositoryImpl @Inject constructor(
    private val taskDao: TaskDao,
    private val alarmManagerUtil: AlarmManagerUtil,
) : TaskRepository {
    companion object {
        const val TAG = "TaskRepositoryImpl"
    }


    override suspend fun getTasksForDateRange(start: LocalDate, end: LocalDate): List<TaskEntity> {
        return taskDao.getTasksForDateRange(start, end)
    }

    override suspend fun getTasksForAlarmDisplayDateRange(
        start: LocalDate,
        end: LocalDate,
    ): List<TaskEntity> {
        return taskDao.getTasksForAlarmDisplayDateRange(start, end)
    }

    override suspend fun addTodo(todoEntity: TodoEntity) {
        taskDao.insertTask(todoEntity.toTaskEntity())
        scheduleAlarmForTodoItem(todoEntity)
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
        cancelAllAlarmsForTodoItem(taskDao.getTaskItemById(id).toTodoEntity())
    }

    override suspend fun updateTask(taskEntity: TaskEntity) {
        taskDao.updateTask(taskEntity)
        cancelAllAlarmsForTodoItem(taskEntity.toTodoEntity())
        scheduleAlarmForTodoItem(taskEntity.toTodoEntity())
    }

    override suspend fun markTaskAsCompleted(id: Long, completionTime: LocalDateTime) {
        taskDao.updateTaskCompletionStatus(id, true, completionTime)
        cancelAllAlarmsForTodoItem(taskDao.getTaskItemById(id).toTodoEntity())
    }

    override suspend fun markTaskAsCancelCompleted(id: Long) {
        taskDao.updateTaskCompletionStatus(id, false, null)
        scheduleAlarmForTodoItem(taskDao.getTaskItemById(id).toTodoEntity())
    }

    override suspend fun getTaskItemById(taskId: Long): TaskEntity {
        return taskDao.getTaskItemById(taskId)
    }

    override suspend fun delayTodoEntity(id: Long, delayDateTime: LocalDateTime) {
        taskDao.delayTask(id, delayDateTime)
        cancelAllAlarmsForTodoItem(taskDao.getTaskItemById(id).toTodoEntity())
        scheduleAlarmForTodoItem(taskDao.getTaskItemById(id).toTodoEntity())
    }

    override suspend fun delayAllTodoEntity(itemIds: List<Long>, delayDateTime: LocalDateTime) {
        itemIds.forEach { itemId ->
            taskDao.delayTask(itemId, delayDateTime)
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

    override suspend fun addSchedule(scheduleEntity: ScheduleEntity) {
        taskDao.insertTask(scheduleEntity.toTaskEntity())
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
    private fun scheduleAlarmForTodoItem(todoEntity: TodoEntity) {
        if (todoEntity.deadLine != null) {

            val alarmTimeInMillis =
                todoEntity.deadLine.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

            val oneHourBeforeInMillis = alarmTimeInMillis - 3600000 // 1시간 전
            val tenMinutesBeforeInMillis = alarmTimeInMillis - 600000 // 10분 전
            val fiveMinutesBeforeInMillis = alarmTimeInMillis - 300000 // 5분 전

            val nowInMillis = System.currentTimeMillis()

            // 알람 설정
            if (nowInMillis < alarmTimeInMillis) {
                if (nowInMillis < oneHourBeforeInMillis) {
                    alarmManagerUtil.setExactAlarm(
                        oneHourBeforeInMillis,
                        todoEntity,
                        AlarmType.ONE_HOUR_BEFORE
                    )
                }
                if (nowInMillis < tenMinutesBeforeInMillis) {
                    alarmManagerUtil.setExactAlarm(
                        tenMinutesBeforeInMillis,
                        todoEntity,
                        AlarmType.TEN_MINUTES_BEFORE
                    )
                }
                if (nowInMillis < fiveMinutesBeforeInMillis) {
                    alarmManagerUtil.setExactAlarm(
                        fiveMinutesBeforeInMillis,
                        todoEntity,
                        AlarmType.FIVE_MINUTES_BEFORE
                    )
                }
                alarmManagerUtil.setExactAlarm(
                    alarmTimeInMillis,
                    todoEntity,
                    AlarmType.NOW
                )
            }
        }
    }

    // 알람 취소
    private fun cancelAllAlarmsForTodoItem(todoEntity: TodoEntity) {
        alarmManagerUtil.cancelAllAlarmsForTodoItem(todoEntity)
    }

    // TODO 알람 테스트
    override fun test() {
        alarmManagerUtil.setTestAlarm()
    }
}