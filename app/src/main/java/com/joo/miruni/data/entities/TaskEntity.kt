package com.joo.miruni.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.LocalDateTime

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String, // 제목
    val details: String?, // 세부사항

    /*
    * 일정
    * */
    val startDate: LocalDate?, // 시작 날짜
    val endDate: LocalDate?, // 종료 날짜

    /*
    * 할 일
    * */
    val deadLine: LocalDateTime?, // 마감 날짜 및 시간

    val alarmDisplayDate: LocalDate?, // 선택된 알람 표시 시간
    val isComplete: Boolean, // 완료 여부
    val completeDate: LocalDateTime?, // 완료 시간
    val type: TaskType, // "SCHEDULE" 또는 "TODO"로 구분할 수 있는 enum
    val isPinned: Boolean, // 고정 여부
)

data class TaskItemsEntity(
    val taskItemsEntity: List<TaskEntity>,
)



