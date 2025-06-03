package com.joo.miruni.di

import com.joo.miruni.domain.usecase.task.schedule.AddScheduleItemUseCase
import com.joo.miruni.domain.usecase.task.schedule.AddScheduleItemUseCaseImpl
import com.joo.miruni.domain.usecase.task.todo.AddTodoItemUseCase
import com.joo.miruni.domain.usecase.task.todo.AddTodoItemUseCaseImpl
import com.joo.miruni.domain.usecase.task.CancelCompleteTaskItemUseCase
import com.joo.miruni.domain.usecase.task.CancelCompleteTaskItemUseCaseImpl
import com.joo.miruni.domain.usecase.task.CompleteTaskItemUseCase
import com.joo.miruni.domain.usecase.task.CompleteTaskItemUseCaseImpl
import com.joo.miruni.domain.usecase.task.todo.DelayAllTodoItemUseCase
import com.joo.miruni.domain.usecase.task.todo.DelayAllTodoItemUseCaseImpl
import com.joo.miruni.domain.usecase.task.todo.DelayTodoItemUseCase
import com.joo.miruni.domain.usecase.task.todo.DelayTodoItemUseCaseImpl
import com.joo.miruni.domain.usecase.task.DeleteTaskItemUseCase
import com.joo.miruni.domain.usecase.task.DeleteTaskItemUseCaseImpl
import com.joo.miruni.domain.usecase.GetAlarmTimeUseCase
import com.joo.miruni.domain.usecase.GetAlarmTimeUseCaseImpl
import com.joo.miruni.domain.usecase.task.todo.GetOverDueTodoItemsForAlarmUseCase
import com.joo.miruni.domain.usecase.task.todo.GetOverDueTodoItemsForAlarmUseCaseImpl
import com.joo.miruni.domain.usecase.task.schedule.GetScheduleItemByIDUseCase
import com.joo.miruni.domain.usecase.task.schedule.GetScheduleItemByIDUseCaseImpl
import com.joo.miruni.domain.usecase.task.schedule.GetScheduleItemsUseCase
import com.joo.miruni.domain.usecase.task.schedule.GetScheduleItemsUseCaseImpl
import com.joo.miruni.domain.usecase.task.GetTasksForDateRangeUseCase
import com.joo.miruni.domain.usecase.task.GetTasksForDateRangeUseCaseImpl
import com.joo.miruni.domain.usecase.task.todo.GetTodoItemByIDUseCase
import com.joo.miruni.domain.usecase.task.todo.GetTodoItemByIDUseCaseImpl
import com.joo.miruni.domain.usecase.task.todo.GetTodoItemsForAlarmUseCase
import com.joo.miruni.domain.usecase.task.todo.GetTodoItemsForAlarmUseCaseImpl
import com.joo.miruni.domain.usecase.SaveAlarmTimeUseCase
import com.joo.miruni.domain.usecase.SaveAlarmTimeUseCaseImpl
import com.joo.miruni.domain.usecase.setting.SettingActiveUnlockScreenUseCase
import com.joo.miruni.domain.usecase.setting.SettingActiveUnlockScreenUseCaseImpl
import com.joo.miruni.domain.usecase.setting.SettingCompletedItemsVisibilityUseCase
import com.joo.miruni.domain.usecase.setting.SettingCompletedItemsVisibilityUseCaseImpl
import com.joo.miruni.domain.usecase.setting.SettingGetCompletedItemsVisibilityStateUseCase
import com.joo.miruni.domain.usecase.setting.SettingGetCompletedItemsVisibilityStateUseCaseImpl
import com.joo.miruni.domain.usecase.setting.SettingGetUnlockStateUseCase
import com.joo.miruni.domain.usecase.setting.SettingGetUnlockStateUseCaseImpl
import com.joo.miruni.domain.usecase.setting.SettingObserveCompletedItemsVisibilityUseCase
import com.joo.miruni.domain.usecase.setting.SettingObserveCompletedItemsVisibilityUseCaseImpl
import com.joo.miruni.domain.usecase.setting.SettingObserveUnlockStateUseCase
import com.joo.miruni.domain.usecase.setting.SettingObserveUnlockStateUseCaseImpl
import com.joo.miruni.domain.usecase.TestUseCase
import com.joo.miruni.domain.usecase.TestUseCaseImpl
import com.joo.miruni.domain.usecase.task.TogglePinStatusUseCase
import com.joo.miruni.domain.usecase.task.TogglePinStatusUseCaseImpl
import com.joo.miruni.domain.usecase.task.schedule.UpdateScheduleItemUseCase
import com.joo.miruni.domain.usecase.task.schedule.UpdateScheduleItemUseCaseImpl
import com.joo.miruni.domain.usecase.task.todo.UpdateTodoItemUseCase
import com.joo.miruni.domain.usecase.task.todo.UpdateTodoItemUseCaseImpl
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

