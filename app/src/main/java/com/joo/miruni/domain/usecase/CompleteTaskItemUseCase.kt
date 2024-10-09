package com.joo.miruni.domain.usecase

import java.time.LocalDateTime


interface CompleteTaskItemUseCase {
    suspend operator fun invoke(id: Long, completionTime: LocalDateTime)
}
