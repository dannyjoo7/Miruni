package com.joo.miruni.di

import com.joo.miruni.domain.usecase.AddTodoItemUseCase
import com.joo.miruni.domain.usecase.AddTodoItemUseCaseImpl
import com.joo.miruni.domain.usecase.CancelCompleteTaskItemUseCase
import com.joo.miruni.domain.usecase.CancelCompleteTaskItemUseCaseImpl
import com.joo.miruni.domain.usecase.CompleteTaskItemUseCase
import com.joo.miruni.domain.usecase.CompleteTaskItemUseCaseImpl
import com.joo.miruni.domain.usecase.DelayTodoItemUseCase
import com.joo.miruni.domain.usecase.DelayTodoItemUseCaseImpl
import com.joo.miruni.domain.usecase.DeleteTaskItemUseCase
import com.joo.miruni.domain.usecase.DeleteTaskItemUseCaseImpl
import com.joo.miruni.domain.usecase.GetAlarmTimeUseCase
import com.joo.miruni.domain.usecase.GetAlarmTimeUseCaseImpl
import com.joo.miruni.domain.usecase.GetTodoItemByIDUseCase
import com.joo.miruni.domain.usecase.GetTodoItemByIDUseCaseImpl
import com.joo.miruni.domain.usecase.GetTodoItemsForAlarmUseCase
import com.joo.miruni.domain.usecase.GetTodoItemsForAlarmUseCaseImpl
import com.joo.miruni.domain.usecase.SaveAlarmTimeUseCase
import com.joo.miruni.domain.usecase.SaveAlarmTimeUseCaseImpl
import com.joo.miruni.domain.usecase.SettingCompletedItemsVisibilityUseCase
import com.joo.miruni.domain.usecase.SettingCompletedItemsVisibilityUseCaseImpl
import com.joo.miruni.domain.usecase.SettingObserveCompletedItemsVisibilityUseCase
import com.joo.miruni.domain.usecase.SettingObserveCompletedItemsVisibilityUseCaseImpl
import com.joo.miruni.domain.usecase.UpdateTodoItemUseCase
import com.joo.miruni.domain.usecase.UpdateTodoItemUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class UseCaseModule {

    /*
    * SetUpActivity
    * */
    @Binds
    @Singleton
    abstract fun bindSaveAlarmTimeUseCase(
        saveAlarmTimeUseCaseImpl: SaveAlarmTimeUseCaseImpl,
    ): SaveAlarmTimeUseCase

    @Binds
    @Singleton
    abstract fun bindGetAlarmTimeUseCase(
        getAlarmTimeUseCaseImpl: GetAlarmTimeUseCaseImpl,
    ): GetAlarmTimeUseCase

    /*
    * MainActivity
    * */
    @Binds
    @Singleton
    abstract fun bindSettingCompletedItemsVisibilityUseCase(
        settingCompletedItemsVisibilityUseCaseImpl: SettingCompletedItemsVisibilityUseCaseImpl,
    ): SettingCompletedItemsVisibilityUseCase

    /*
    * Add TodoItem
    * */
    @Binds
    @Singleton
    abstract fun bindAddTodoItemUseCase(
        addTodoItemUseCaseImpl: AddTodoItemUseCaseImpl,
    ): AddTodoItemUseCase

    /*
    * GetTaskItem
    * */

    // GetTodoTaskItems
    @Binds
    @Singleton
    abstract fun bindGetTodoItemsForAlarmUseCase(
        getTodoItemsUseCaseImpl: GetTodoItemsForAlarmUseCaseImpl,
    ): GetTodoItemsForAlarmUseCase


    /*
    * HomeViewModel
    * */
    @Binds
    @Singleton
    abstract fun bindDeleteTaskItemUseCase(
        deleteTaskItemUseCaseImpl: DeleteTaskItemUseCaseImpl,
    ): DeleteTaskItemUseCase

    @Binds
    @Singleton
    abstract fun bindCompleteTaskItemUseCase(
        completeTaskItemUseCaseImpl: CompleteTaskItemUseCaseImpl,
    ): CompleteTaskItemUseCase

    @Binds
    @Singleton
    abstract fun bindCancelCompleteTaskItemUseCase(
        ancelcompleteTaskItemUseCaseImpl: CancelCompleteTaskItemUseCaseImpl,
    ): CancelCompleteTaskItemUseCase

    @Binds
    @Singleton
    abstract fun bindDelayTodoItemUseCase(
        delayTodoItemUseCaseImpl: DelayTodoItemUseCaseImpl,
    ): DelayTodoItemUseCase

    @Binds
    @Singleton
    abstract fun bindSettingObserveCompletedItemsVisibilityUseCase(
        settingObserveCompletedItemsVisibilityUseCaseImpl: SettingObserveCompletedItemsVisibilityUseCaseImpl,
    ): SettingObserveCompletedItemsVisibilityUseCase


    /*
    * DetailViewModel
    * */
    @Binds
    @Singleton
    abstract fun bindGetTodoItemByIDUseCase(
        getTodoItemByIDUseCaseImpl: GetTodoItemByIDUseCaseImpl,
    ): GetTodoItemByIDUseCase

    @Binds
    @Singleton
    abstract fun bindUpdateTodoItemUseCase(
        updateTodoItemUseCaseImpl: UpdateTodoItemUseCaseImpl,
    ): UpdateTodoItemUseCase
}

