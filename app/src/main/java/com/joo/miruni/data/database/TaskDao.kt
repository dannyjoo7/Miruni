package com.joo.miruni.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.joo.miruni.data.entities.TaskEntity
import java.time.LocalDate

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

}
