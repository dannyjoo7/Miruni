package com.joo.miruni.domain.repository

import com.joo.miruni.data.entities.TaskEntity
import com.joo.miruni.data.entities.TaskItemsEntity
import com.joo.miruni.domain.model.ScheduleEntity
import com.joo.miruni.domain.model.TodoEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.LocalDateTime

interface TaskRepository {

    /*
    * TASK
    * */

    // 날짜 범위로 task 데이터 가져오기
    suspend fun getTasksForDateRange(start: LocalDate, end: LocalDate): List<TaskEntity>

    // 날짜 범위와 알람 표시일을 기준으로 task 데이터 가져오기
    suspend fun getTasksForAlarmDisplayDateRange(start: LocalDate, end: LocalDate): List<TaskEntity>

    // task 삭제
    suspend fun deleteTaskById(id: Long)

    // task 완료
    suspend fun markTaskAsCompleted(id: Long, completionTime: LocalDateTime)

    // task 완료 취소
    suspend fun markTaskAsCancelCompleted(id: Long)

    // task 업데이트
    suspend fun updateTask(taskEntity: TaskEntity)

    // task ID로 찾기
    suspend fun getTaskItemById(taskId: Long): TaskEntity

    // task 선택된 날짜 기준으로 가져오기
    suspend fun getTasksForAlarmByDate(
        selectDate: LocalDateTime,
    ): Flow<TaskItemsEntity>

    // 기한이 지난 task 가져오기
    suspend fun getOverdueTaskEntities(
        currentDateTime: LocalDateTime,
    ): Flow<TaskItemsEntity>

    /*
    * 할 일
    * */

    // 할 일 추가
    suspend fun addTodo(todoEntity: TodoEntity)

    // 할 일 미루기
    suspend fun delayTodoEntity(id: Long, delayDateTime: LocalDateTime)

    // 만료된 할 일 전부 미루기
    suspend fun delayAllTodoEntity(itemIds: List<Long>, delayDateTime: LocalDateTime)

    /*
    * 일정
    * */

    // 일정 추가
    suspend fun addSchedule(scheduleEntity: ScheduleEntity)

    // 일정 선택된 날짜 기준으로 가져오기
    suspend fun getSchedules(
        selectDate: LocalDate,
        lastStartDate: LocalDate?,
    ): Flow<TaskItemsEntity>
}
