package com.joo.miruni.domain.usecase.task


interface DeleteTaskItemUseCase {
    suspend operator fun invoke(id: Long)
}
