package com.joo.miruni.presentation.calendar

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
open class CalendarViewModel @Inject constructor() : ViewModel() {
    companion object {
        const val TAG = "CalendarViewModel"
    }

}