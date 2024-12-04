package com.joo.miruni.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.joo.miruni.data.entities.TaskEntity
import com.joo.miruni.data.entities.TaskType
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.LocalDateTime

@Dao
interface TaskDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(taskEntity: TaskEntity): Long

    // 날짜 범위로 Task 찾기
    @Query("""
        SELECT * FROM tasks 
        WHERE 
            (type = 'SCHEDULE' AND startDate BETWEEN :start AND :end) OR 
            (type = 'SCHEDULE' AND endDate BETWEEN :start AND :end) OR 
            (type = 'TODO' AND deadLine BETWEEN :start AND :end)
    """)
    fun getTasksForDateRange(
        start: LocalDate,
        end: LocalDate
    ): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE alarmDisplayDate BETWEEN :start AND :end")
    suspend fun getTasksForAlarmDisplayDateRange(start: LocalDate, end: LocalDate): List<TaskEntity>

    @Query("SELECT * FROM tasks")
    suspend fun getAllTasks(): List<TaskEntity>

    // deadLine 넘긴 할 일
    @Query(
        """
    SELECT * FROM tasks 
    WHERE type = :taskType 
    AND deadLine < :currentDateTime 
    AND isComplete = 0
    ORDER BY deadLine ASC 
    """
    )
    fun getOverdueTasks(
        currentDateTime: LocalDateTime,
        taskType: TaskType = TaskType.TODO,
    ): Flow<List<TaskEntity>>

    // TODO목록 페이지로 가져오기
    @Query(
        """
    SELECT * FROM tasks 
    WHERE type = :taskType 
    AND deadLine > :selectDate 
    AND alarmDisplayDate < :selectDate
    ORDER BY deadLine ASC
    """
    )
    fun getTodoTasksPaginated(
        selectDate: LocalDateTime,
        taskType: TaskType = TaskType.TODO,
    ): Flow<List<TaskEntity>>

    // task 삭제
    @Query("DELETE FROM tasks WHERE id = :id")
    suspend fun deleteTaskById(id: Long)

    // task 완료 여부
    @Query("UPDATE tasks SET isComplete = :isComplete, completeDate = :completeDate WHERE id = :taskId")
    suspend fun updateTaskCompletionStatus(
        taskId: Long,
        isComplete: Boolean,
        completeDate: LocalDateTime?,
    )

    // taskId로 task 찾기
    @Query("SELECT * FROM tasks WHERE id = :taskId")
    suspend fun getTaskItemById(taskId: Long): TaskEntity

    // task 업데이트 메소드
    @Update
    suspend fun updateTask(taskEntity: TaskEntity)

    // 일정 미루기 메소드
    @Query("UPDATE tasks SET deadLine = :newDeadline WHERE id = :taskId")
    suspend fun delayTask(taskId: Long, newDeadline: LocalDateTime)

    // 스케줄 목록 페이지로 가져오기
    @Query(
        """
    SELECT * FROM tasks 
    WHERE type = :taskType 
    AND (startDate IS NOT NULL AND startDate >= :selectDate OR 
         (endDate IS NULL OR endDate >= :selectDate) OR 
         (startDate <= :selectDate AND (endDate IS NULL OR endDate >= :selectDate)))
    AND (startDate > :lastStartDate OR :lastStartDate IS NULL) 
    ORDER BY startDate ASC  
    LIMIT :limit
    """
    )
    fun getScheduleTasksPaginated(
        selectDate: LocalDate,
        lastStartDate: LocalDate? = null,
        limit: Int = 12,
        taskType: TaskType = TaskType.SCHEDULE,
    ): Flow<List<TaskEntity>>

}
