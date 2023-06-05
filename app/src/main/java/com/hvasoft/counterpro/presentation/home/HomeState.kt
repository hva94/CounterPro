package com.hvasoft.counterpro.presentation.home

import com.hvasoft.counterpro.domain.model.Counter
import com.hvasoft.counterpro.core.error_handling.ErrorState

sealed class HomeState {
    object Loading : HomeState()
    object Empty : HomeState()
    data class Success(val counters: List<Counter>) : HomeState()
    data class Failure(val error: ErrorState? = null) : HomeState()
}