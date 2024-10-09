package com.joo.miruni.domain.usecase


interface DeleteTaskItemUseCase {
    suspend operator fun invoke(id: Long)
}
