package com.joo.miruni.domain.usecase.task

import java.time.LocalDateTime


interface CompleteTaskItemUseCase {
    suspend operator fun invoke(id: Long, completionTime: LocalDateTime)
}
