package com.joo.miruni.domain.usecase

import com.joo.miruni.domain.repository.TaskRepository
import javax.inject.Inject

class TestUseCaseImpl @Inject constructor(
    private val taskRepository: TaskRepository,
) : TestUseCase {
    override suspend fun invoke(){
        return taskRepository.test()
    }
}