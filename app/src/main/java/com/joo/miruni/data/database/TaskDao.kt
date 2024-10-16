package com.joo.miruni.data.database

import androidx.room.Dao
import androidx.room.Delete
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
    suspend fun insertTask(taskEntity: TaskEntity)

    @Query("SELECT * FROM tasks WHERE startDate BETWEEN :start AND :end")
    suspend fun getTasksForDateRange(start: LocalDate, end: LocalDate): List<TaskEntity>

    @Query("SELECT * FROM tasks WHERE alarmDisplayDate BETWEEN :start AND :end")
    suspend fun getTasksForAlarmDisplayDateRange(start: LocalDate, end: LocalDate): List<TaskEntity>

    @Query("SELECT * FROM tasks")
    suspend fun getAllTasks(): List<TaskEntity>

    // deadLine 넘긴 할 일
    @Query("SELECT * FROM tasks WHERE type = :taskType AND deadLine < :currentDateTime")
    fun getOverdueTasks(
        currentDateTime: LocalDateTime,
        taskType: TaskType = TaskType.TODO
    ): Flow<List<TaskEntity>>

    @Query(
        """
    SELECT * FROM tasks 
    WHERE type = :taskType 
    AND deadLine >= :selectDate 
    AND alarmDisplayDate <= :selectDate
    AND (deadLine > :lastDeadLine OR :lastDeadLine IS NULL)
    ORDER BY deadLine ASC
    LIMIT :limit
    """
    )
    fun getTodoTasksPaginated(
        selectDate: LocalDateTime,
        lastDeadLine: LocalDateTime? = null,
        limit: Int = 20,
        taskType: TaskType = TaskType.TODO,
    ): Flow<List<TaskEntity>>

    // task 삭제
    @Query("DELETE FROM tasks WHERE id = :id")
    suspend fun deleteTaskById(id: Long)

    // task 완료
    @Query("UPDATE tasks SET isComplete = :isComplete, completeDate = :completeDate WHERE id = :taskId")
    suspend fun updateTaskCompletionStatus(
        taskId: Long,
        isComplete: Boolean,
        completeDate: LocalDateTime?,
    )

    // taskId로 task 찾기
    @Query("SELECT * FROM tasks WHERE id = :taskId")
    suspend fun getTodoItemById(taskId: Long): TaskEntity

    // task 업데이트 메소드
    @Update
    suspend fun updateTask(taskEntity: TaskEntity)

    // 일정 미루기 메소드
    @Query("UPDATE tasks SET deadLine = :newDeadline WHERE id = :taskId")
    suspend fun delayTask(taskId: Long, newDeadline: LocalDateTime)
}
