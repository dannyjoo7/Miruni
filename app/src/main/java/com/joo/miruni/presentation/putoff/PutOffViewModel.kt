package com.joo.miruni.presentation.putoff

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
open class PutOffViewModel @Inject constructor() : ViewModel() {
    companion object {
        const val TAG = "PutOffViewModel"
    }

}