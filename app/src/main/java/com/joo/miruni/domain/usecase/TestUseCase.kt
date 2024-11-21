package com.joo.miruni.domain.usecase

import kotlinx.coroutines.flow.Flow

interface TestUseCase {
    suspend operator fun invoke()
}