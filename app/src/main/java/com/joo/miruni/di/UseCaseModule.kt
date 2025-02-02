package com.joo.miruni.di

import com.joo.miruni.domain.usecase.AddScheduleItemUseCase
import com.joo.miruni.domain.usecase.AddScheduleItemUseCaseImpl
import com.joo.miruni.domain.usecase.AddTodoItemUseCase
import com.joo.miruni.domain.usecase.AddTodoItemUseCaseImpl
import com.joo.miruni.domain.usecase.CancelCompleteTaskItemUseCase
import com.joo.miruni.domain.usecase.CancelCompleteTaskItemUseCaseImpl
import com.joo.miruni.domain.usecase.CompleteTaskItemUseCase
import com.joo.miruni.domain.usecase.CompleteTaskItemUseCaseImpl
import com.joo.miruni.domain.usecase.DelayAllTodoItemUseCase
import com.joo.miruni.domain.usecase.DelayAllTodoItemUseCaseImpl
import com.joo.miruni.domain.usecase.DelayTodoItemUseCase
import com.joo.miruni.domain.usecase.DelayTodoItemUseCaseImpl
import com.joo.miruni.domain.usecase.DeleteTaskItemUseCase
import com.joo.miruni.domain.usecase.DeleteTaskItemUseCaseImpl
import com.joo.miruni.domain.usecase.GetAlarmTimeUseCase
import com.joo.miruni.domain.usecase.GetAlarmTimeUseCaseImpl
import com.joo.miruni.domain.usecase.GetOverDueTodoItemsForAlarmUseCase
import com.joo.miruni.domain.usecase.GetOverDueTodoItemsForAlarmUseCaseImpl
import com.joo.miruni.domain.usecase.GetScheduleItemByIDUseCase
import com.joo.miruni.domain.usecase.GetScheduleItemByIDUseCaseImpl
import com.joo.miruni.domain.usecase.GetScheduleItemsUseCase
import com.joo.miruni.domain.usecase.GetScheduleItemsUseCaseImpl
import com.joo.miruni.domain.usecase.GetTasksForDateRangeUseCase
import com.joo.miruni.domain.usecase.GetTasksForDateRangeUseCaseImpl
import com.joo.miruni.domain.usecase.GetTodoItemByIDUseCase
import com.joo.miruni.domain.usecase.GetTodoItemByIDUseCaseImpl
import com.joo.miruni.domain.usecase.GetTodoItemsForAlarmUseCase
import com.joo.miruni.domain.usecase.GetTodoItemsForAlarmUseCaseImpl
import com.joo.miruni.domain.usecase.SaveAlarmTimeUseCase
import com.joo.miruni.domain.usecase.SaveAlarmTimeUseCaseImpl
import com.joo.miruni.domain.usecase.SettingActiveUnlockScreenUseCase
import com.joo.miruni.domain.usecase.SettingActiveUnlockScreenUseCaseImpl
import com.joo.miruni.domain.usecase.SettingCompletedItemsVisibilityUseCase
import com.joo.miruni.domain.usecase.SettingCompletedItemsVisibilityUseCaseImpl
import com.joo.miruni.domain.usecase.SettingGetCompletedItemsVisibilityStateUseCase
import com.joo.miruni.domain.usecase.SettingGetCompletedItemsVisibilityStateUseCaseImpl
import com.joo.miruni.domain.usecase.SettingGetUnlockStateUseCase
import com.joo.miruni.domain.usecase.SettingGetUnlockStateUseCaseImpl
import com.joo.miruni.domain.usecase.SettingObserveCompletedItemsVisibilityUseCase
import com.joo.miruni.domain.usecase.SettingObserveCompletedItemsVisibilityUseCaseImpl
import com.joo.miruni.domain.usecase.SettingObserveUnlockStateUseCase
import com.joo.miruni.domain.usecase.SettingObserveUnlockStateUseCaseImpl
import com.joo.miruni.domain.usecase.TestUseCase
import com.joo.miruni.domain.usecase.TestUseCaseImpl
import com.joo.miruni.domain.usecase.TogglePinStatusUseCase
import com.joo.miruni.domain.usecase.TogglePinStatusUseCaseImpl
import com.joo.miruni.domain.usecase.UpdateScheduleItemUseCase
import com.joo.miruni.domain.usecase.UpdateScheduleItemUseCaseImpl
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

    @Binds
    @Singleton
    abstract fun bindSettingActiveUnlockScreenUseCase(
        settingActiveUnlockScreenUseCaseImpl: SettingActiveUnlockScreenUseCaseImpl,
    ): SettingActiveUnlockScreenUseCase

    @Binds
    @Singleton
    abstract fun bindSettingGetCompletedItemsVisibilityStateUseCaseImpl(
        settingGetCompletedItemsVisibilityStateUseCaseImpl: SettingGetCompletedItemsVisibilityStateUseCaseImpl,
    ): SettingGetCompletedItemsVisibilityStateUseCase

    /*
    * Add TodoItem
    * */
    @Binds
    @Singleton
    abstract fun bindAddTodoItemUseCase(
        addTodoItemUseCaseImpl: AddTodoItemUseCaseImpl,
    ): AddTodoItemUseCase


    /*
    * Add ScheduleItem
    * */
    @Binds
    @Singleton
    abstract fun bindAddScheduleItemUseCase(
        addScheduleItemUseCaseImpl: AddScheduleItemUseCaseImpl,
    ): AddScheduleItemUseCase

    /*
    * HomeViewModel
    * */
    @Binds
    @Singleton
    abstract fun bindGetTodoItemsForAlarmUseCase(
        getTodoItemsUseCaseImpl: GetTodoItemsForAlarmUseCaseImpl,
    ): GetTodoItemsForAlarmUseCase

    @Binds
    @Singleton
    abstract fun bindGetScheduleItemsUseCase(
        getScheduleItemsUseCaseImpl: GetScheduleItemsUseCaseImpl,
    ): GetScheduleItemsUseCase


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

    @Binds
    @Singleton
    abstract fun bindSettingObserveUnlockStateUseCase(
        settingObserveUnlockStateUseCaseImpl: SettingObserveUnlockStateUseCaseImpl,
    ): SettingObserveUnlockStateUseCase

    @Binds
    @Singleton
    abstract fun bindGetOverDueTodoItemsForAlarmUseCase(
        getOverDueTodoItemsForAlarmUseCaseImpl: GetOverDueTodoItemsForAlarmUseCaseImpl,
    ): GetOverDueTodoItemsForAlarmUseCase

    @Binds
    @Singleton
    abstract fun bindTogglePinStatusUseCase(
        togglePinStatusUseCaseImpl: TogglePinStatusUseCaseImpl,
    ): TogglePinStatusUseCase


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

    @Binds
    @Singleton
    abstract fun bindGetScheduleItemByIDUseCase(
        getScheduleItemByIDUseCaseImpl: GetScheduleItemByIDUseCaseImpl,
    ): GetScheduleItemByIDUseCase

    @Binds
    @Singleton
    abstract fun bindUpdateScheduleItemUseCase(
        updateScheduleItemUseCaseImpl: UpdateScheduleItemUseCaseImpl,
    ): UpdateScheduleItemUseCase

    /*
    * OverdueViewModel
    * */
    @Binds
    @Singleton
    abstract fun bindDelayAllTodoItemUseCase(
        delayAllTodoItemUseCaseImpl: DelayAllTodoItemUseCaseImpl,
    ): DelayAllTodoItemUseCase

    /*
    * CalendarViewModel
    * */
    @Binds
    @Singleton
    abstract fun bindGetTasksForDateRangeUseCase(
        getTasksForDateRangeUseCaseImpl: GetTasksForDateRangeUseCaseImpl,
    ): GetTasksForDateRangeUseCase

    /*
    * UnlockReceiver
    * */
    @Binds
    @Singleton
    abstract fun bindSettingGetUnlockStateUseCaseImpl(
        settingGetUnlockStateUseCaseImpl: SettingGetUnlockStateUseCaseImpl,
    ): SettingGetUnlockStateUseCase

    /*
    * TEST
    * */
    @Binds
    @Singleton
    abstract fun bindTestUseCase(
        testUseCaseImpl: TestUseCaseImpl,
    ): TestUseCase
}

