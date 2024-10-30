package com.joo.miruni.presentation.overdue

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joo.miruni.domain.usecase.CompleteTaskItemUseCase
import com.joo.miruni.domain.usecase.DelayTodoItemUseCase
import com.joo.miruni.domain.usecase.DeleteTaskItemUseCase
import com.joo.miruni.domain.usecase.GetOverDueTodoItemsForAlarmUseCase
import com.joo.miruni.presentation.home.ThingsTodo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class OverdueViewModel @Inject constructor(
    private val getOverDueTodoItemsForAlarmUseCase: GetOverDueTodoItemsForAlarmUseCase,
    private val deleteTaskItemUseCase: DeleteTaskItemUseCase,
    private val completeTaskItemUseCase: CompleteTaskItemUseCase,
    private val delayTodoItemUseCase: DelayTodoItemUseCase,
) : ViewModel() {

    companion object {
        const val TAG = "ExpirationViewModel"
    }

    /*
    * LiveData
    * */

    // 기한이 지난 할 일 Item list
    private val _overdueTodoItems = MutableLiveData<List<ThingsTodo>>(emptyList())
    val overdueTodoItems: LiveData<List<ThingsTodo>> get() = _overdueTodoItems

    // overdueTodoList 로딩 중인지 판단하는 변수
    private val _isOverdueTodoListLoading = MutableLiveData(false)
    val isOverdueTodoListLoading: LiveData<Boolean> get() = _isOverdueTodoListLoading

    // 삭제됐는지 여부를 판단하는 변수
    private val _deletedItems = MutableLiveData<Set<Long>>(emptySet())
    val deletedItems: LiveData<Set<Long>> get() = _deletedItems

    // 확장 여부를 판단하는 변수
    private val _expandedItems = mutableStateOf<Set<Long>>(emptySet())
    val expandedItems: State<Set<Long>> = _expandedItems

    // todoList 페이징을 위한 마지막 데이터의 deadLine
    private var lastDataDeadLine: LocalDateTime? = null

    // getThings 코루틴
    private var getOverdueTodoItemsJob: Job? = null

    /*
    * 메소드
    * */

    init {
        loadInitOverdueTasks()
    }

    // 기한이 지난 할 일 init load
    private fun loadInitOverdueTasks() {
        getOverdueTodoItemsJob?.cancel()
        getOverdueTodoItemsJob = viewModelScope.launch {
            _isOverdueTodoListLoading.value = true
            runCatching {
                getOverDueTodoItemsForAlarmUseCase.invoke(
                    LocalDateTime.now()
                )
            }.onSuccess { flow ->
                flow.collect { overdueTodoItems ->
                    _overdueTodoItems.value = overdueTodoItems.todoEntities.map {
                        ThingsTodo(
                            id = it.id,
                            title = it.title,
                            deadline = it.deadLine ?: LocalDateTime.now(),
                            description = it.details ?: "",
                            isCompleted = it.isComplete,
                            completeDate = it.completeDate
                        )
                    }
                    lastDataDeadLine = _overdueTodoItems.value?.lastOrNull()?.deadline
                    _isOverdueTodoListLoading.value = false
                }
            }.onFailure { exception ->
                exception.printStackTrace()
                _isOverdueTodoListLoading.value = false
            }
        }
    }

    // 할 일 미루기 메소드
    fun delayTodoItem(thingsTodo: ThingsTodo) {
        viewModelScope.launch {
            runCatching {
                // TODO deadLine 미룰 때 기기에 저장된 유저가 설정한 값으로 대체 -> 현재 1에서 userSetting 값으로...
                delayTodoItemUseCase.invoke(thingsTodo.id, LocalDateTime.now().plusDays(1))
            }.onSuccess {

            }.onFailure {

            }
        }
    }

    // Task 삭제 메소드
    fun deleteTaskItem(id: Long) {
        viewModelScope.launch {
            _deletedItems.value = _deletedItems.value?.plus(id) ?: setOf(id)
            runCatching {
                // 애니매이션을 위한 딜레이
                delay(1000)
                deleteTaskItemUseCase.invoke(id)
            }.onSuccess {
                _expandedItems.value = _expandedItems.value.filterNot { it == id }.toSet()
            }.onFailure {
                _deletedItems.value = _deletedItems.value?.minus(id)
            }
        }
    }

    // Task 완료 시
    fun completeTask(taskId: Long) {
        viewModelScope.launch {
            runCatching {
                completeTaskItemUseCase.invoke(taskId, LocalDateTime.now())
            }.onSuccess {

            }.onFailure {

            }
        }
    }

    /*
    * UI
    * */

    // TodoTings 확장 토글 메소드
    fun toggleItemExpansion(id: Long) {
        _expandedItems.value = if (_expandedItems.value.contains(id)) {
            emptySet()
        } else {
            setOf(id)
        }
    }

    // TodoTings가 모든 확장 해제 메소드
    fun collapseAllItems() {
        _expandedItems.value = emptySet()
    }
}