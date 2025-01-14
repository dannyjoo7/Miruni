package com.joo.miruni.presentation.detail.detailSchedule

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joo.miruni.domain.usecase.CancelCompleteTaskItemUseCase
import com.joo.miruni.domain.usecase.CompleteTaskItemUseCase
import com.joo.miruni.domain.usecase.DeleteTaskItemUseCase
import com.joo.miruni.domain.usecase.GetScheduleItemByIDUseCase
import com.joo.miruni.domain.usecase.UpdateScheduleItemUseCase
import com.joo.miruni.presentation.addTask.addSchedule.AddScheduleViewModel
import com.joo.miruni.presentation.addTask.addSchedule.ScheduleItem
import com.joo.miruni.presentation.detail.detailTodo.DetailTodoViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class DetailScheduleViewModel @Inject constructor(
    private val getScheduleItemByIDUseCase: GetScheduleItemByIDUseCase,
    private val updateScheduleItemUseCase: UpdateScheduleItemUseCase,
    private val deleteTaskItemUseCase: DeleteTaskItemUseCase,
    private val completeTaskItemUseCase: CompleteTaskItemUseCase,
    private val cancelCompleteTaskItemUseCase: CancelCompleteTaskItemUseCase,
) : ViewModel() {
    companion object {
        const val TAG = "DetailScheduleViewModel"

        const val MAX_TODO_LENGTH = 20
        const val MAX_DESCRIPTION_LENGTH = 100
    }

    /*
    * 변수
    * */

    // ScheduleItem
    private val _scheduleItem = MutableLiveData<ScheduleItem>()
    val scheduleItem: LiveData<ScheduleItem> get() = _scheduleItem

    // 수정 됐는지 판단 변수
    private val _isModified = MutableLiveData(false)
    val isModified: LiveData<Boolean> get() = _isModified

    // 일정 텍스트
    private val _titleText = MutableLiveData("")
    val titleText: LiveData<String> get() = _titleText

    // 세부사항 텍스트
    private val _descriptionText = MutableLiveData("")
    val descriptionText: LiveData<String> get() = _descriptionText


    // 선택된 시작 날짜
    private val _selectedStartDate = MutableLiveData<LocalDate?>(null)
    val selectedStartDate: LiveData<LocalDate?> get() = _selectedStartDate

    // 선택된 종료 날짜
    private val _selectedEndDate = MutableLiveData<LocalDate?>(null)
    val selectedEndDate: LiveData<LocalDate?> get() = _selectedEndDate


    // Bool 시작 날짜 선택 진행 유뮤
    private val _showDateRangePicker = MutableLiveData(false)
    val showDateRangePicker: LiveData<Boolean> get() = _showDateRangePicker


    // TodoTextEmpty 무결성 검사
    private val _isTitleTextEmpty = MutableLiveData(false)
    val isTitleTextEmpty: LiveData<Boolean> get() = _isTitleTextEmpty

    // 날짜 무결성 검사
    private val _isDateEmpty = MutableLiveData(false)
    val isDateEmpty: LiveData<Boolean> get() = _isDateEmpty


    // AddSchedule 성공 여부
    private val _isScheduleAdded = MutableLiveData<Boolean>(false)
    val isScheduleAdded: LiveData<Boolean> get() = _isScheduleAdded

    /*
    * UI
    * */

    // 일정 item 초기화
    fun loadScheduleDetails(scheduleId: Long) {
        viewModelScope.launch {
            runCatching {
                getScheduleItemByIDUseCase(scheduleId)
            }.onSuccess { scheduleModel ->
                // ScheduleItem으로 변환
                _scheduleItem.value = ScheduleItem(
                    id = scheduleModel.id,
                    title = scheduleModel.title,
                    descriptionText = scheduleModel.details ?: "",
                    startDate = scheduleModel.startDate,
                    endDate = scheduleModel.endDate,
                    isComplete = scheduleModel.isComplete,
                    isPinned = scheduleModel.isPinned
                )

                val curScheduleItem = _scheduleItem.value

                _titleText.value = curScheduleItem?.title ?: ""
                _descriptionText.value = curScheduleItem?.descriptionText ?: ""
                _selectedStartDate.value = curScheduleItem?.startDate
                _selectedEndDate.value = curScheduleItem?.endDate

            }.onFailure { exception ->
                Log.e(DetailTodoViewModel.TAG, exception.toString())
            }
        }
    }

    // 할 일 텍스트 업데이트
    fun updateTitleText(newValue: String) {
        _titleText.value = newValue.take(AddScheduleViewModel.MAX_TODO_LENGTH)
        isModify()
    }

    // 세부사항 텍스트 업데이트
    fun updateDescriptionText(newValue: String) {
        _descriptionText.value = newValue.take(AddScheduleViewModel.MAX_DESCRIPTION_LENGTH)
        isModify()
    }

    // StartDatePicker 가시성 on/off
    fun clickedDateRangePickerBtn() {
        _showDateRangePicker.value = _showDateRangePicker.value?.not()
    }

    // 애니메이션 종료
    fun finishAnimation() {
        _isTitleTextEmpty.value = false
        _isDateEmpty.value = false
    }

    // 항목이 수정됨
    private fun isModify() {
        _isModified.value = true
    }

    /*
    * DatePicker
    * */

    // 날짜 선택 초기화
    fun initSelectedDate() {
        _selectedStartDate.value = null
        _selectedEndDate.value = null
        isModify()
    }

    // 시작 날짜 선택 메소드
    fun selectStartDate(date: LocalDate) {
        // 종료일이 선택되지 않은 경우
        if (_selectedEndDate.value == null) {
            _selectedStartDate.value = date
        }
        // 종료일이 선택된 경우
        else {
            // 종료일이 시작일보다 이전인 경우 종료일을 초기화
            if (_selectedEndDate.value?.isBefore(date) == true) {
                _selectedEndDate.value = null
            } else {
                // 정상적으로 시작일 설정
                _selectedStartDate.value = date
            }
        }
        isModify()
    }

    // 종료 날짜 선택 메소드
    fun selectEndDate(date: LocalDate) {
        // 시작일이 선택되지 않은 경우
        if (_selectedStartDate.value == null) {
            return
        }
        // 종료일이 시작일보다 이전인 경우
        else if (_selectedStartDate.value!!.isAfter(date)) {
            // 종료일을 선택할 수 없으므로 아무 것도 하지 않음
            return
        } else {
            // 정상적으로 종료일 설정
            _selectedEndDate.value = date
        }
        isModify()
    }

    // date MM월 yyyy 변환 메소드
    fun formatSelectedDateForCalendar(selectDate: LocalDate?): String {
        return selectDate?.let {
            val month = it.monthValue
            val year = it.year
            val date = it.dayOfMonth
            "${month}월 ${date}일 $year"
        } ?: "날짜 선택"
    }

    /*
    * Top Bar
    * */

    // 수정 버튼 클릭 시
    fun updateScheduleItem() {
        if (validateScheduleItem()) {
            viewModelScope.launch {
                val scheduleItem = ScheduleItem(
                    id = _scheduleItem.value?.id ?: 0,
                    title = _titleText.value ?: "",
                    descriptionText = _descriptionText.value ?: "",
                    startDate = _selectedStartDate.value ?: LocalDate.now().plusDays(1),
                    endDate = _selectedEndDate.value ?: LocalDate.now().plusDays(1),
                    isComplete = _scheduleItem.value?.isComplete ?: false,
                    isPinned = _scheduleItem.value?.isPinned ?: false

                )
                runCatching {
                    updateScheduleItemUseCase(scheduleItem)
                }.onSuccess {
                    _isScheduleAdded.value = true
                }.onFailure { exception ->
                    _isScheduleAdded.value = false
                    Log.e(AddScheduleViewModel.TAG, exception.message.toString())
                }
            }
        } else {
            return
        }
    }

    // 무결성 검사
    private fun validateScheduleItem(): Boolean {
        // 제목이 비어있는지 체크
        if (_titleText.value.isNullOrEmpty()) {
            _isTitleTextEmpty.value = true
            return false
        }

        // 시작일과 종료일이 유효한지 체크
        if (_selectedStartDate.value == null || _selectedEndDate.value == null) {
            _isDateEmpty.value = true
            return false
        }

        return true
    }

    /*
   * Bottom Bar
   * */

    // 일정 삭제
    fun deleteScheduleItem(scheduleId: Long) {
        viewModelScope.launch {
            runCatching {
                deleteTaskItemUseCase.invoke(scheduleId)
            }.onSuccess {

            }.onFailure {

            }
        }
    }

    // 일정 완료
    fun completeScheduleItem(scheduleId: Long) {
        viewModelScope.launch {
            runCatching {
                completeTaskItemUseCase.invoke(scheduleId, LocalDateTime.now())
            }.onSuccess {

            }.onFailure {

            }
        }
    }

    // 일정 완료 취소
    fun completeCancelScheduleItem(scheduleId: Long) {
        viewModelScope.launch {
            runCatching {
                cancelCompleteTaskItemUseCase.invoke(scheduleId)
            }.onSuccess {

            }.onFailure {

            }
        }
    }
}


