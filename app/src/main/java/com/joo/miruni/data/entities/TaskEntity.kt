package com.joo.miruni.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.LocalTime

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0, // 자동 생성되는 기본 키
    val title: String, // 제목
    val details: String?, // 세부사항 (null 가능)
    val startDate: LocalDate?, // 시작 날짜 (일정의 경우)
    val endDate: LocalDate?, // 종료 날짜 (일정의 경우)
    val deadLine: LocalDate?, // 마감 날짜 (할 일의 경우)
    val deadLineTime: LocalTime?, // 마감 시간 (할 일의 경우)
    val alarmDisplayDate: LocalDate, // 선택된 알람 표시 시간
    val type: TaskType, // "SCHEDULE" 또는 "TODO"로 구분할 수 있는 enum
)

data class TaskItemsEntity(
    val taskItemsEntity: List<TaskEntity>,
)



