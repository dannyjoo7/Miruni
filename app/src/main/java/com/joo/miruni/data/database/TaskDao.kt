package com.joo.miruni.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
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

    @Query("DELETE FROM tasks WHERE id = :id")
    suspend fun deleteTaskById(id: Long)

}
