package com.hvasoft.counterpro.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hvasoft.counterpro.core.common.Result
import com.hvasoft.counterpro.core.common.fold
import com.hvasoft.counterpro.core.common.toError
import com.hvasoft.counterpro.core.common.validateErrorCode
import com.hvasoft.counterpro.core.error_handling.ErrorResolver.handleError
import com.hvasoft.counterpro.di.IoDispatcher
import com.hvasoft.counterpro.domain.model.Counter
import com.hvasoft.counterpro.domain.use_case.CounterUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val useCases: CounterUseCases,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _countersState = MutableStateFlow<HomeState>(HomeState.Loading)
    val countersState = _countersState.asStateFlow()

    init {
        getCounters()
    }

    private fun getCounters() {
        _countersState.value = HomeState.Loading
        viewModelScope.launch(dispatcher) {
            handleUseCaseResult(useCases.getCounters())
        }
    }

    fun createCounter(title: String) {
        _countersState.value = HomeState.Loading
        viewModelScope.launch(dispatcher) {
            handleUseCaseResult(useCases.createCounter(title))
        }
    }

    fun incrementCounter(counter: Counter) {
        _countersState.value = HomeState.Loading
        viewModelScope.launch(dispatcher) {
            handleUseCaseResult(useCases.incrementCounter(counter))
        }
    }

    fun decrementCounter(counter: Counter) {
        _countersState.value = HomeState.Loading
        viewModelScope.launch(dispatcher) {
            handleUseCaseResult(useCases.decrementCounter(counter))
        }
    }

    fun deleteCounters(counters: List<Counter>) {
        _countersState.value = HomeState.Loading
        viewModelScope.launch(dispatcher) {
            handleUseCaseResult(useCases.deleteCounters(counters))
        }
    }

    private fun handleUseCaseResult(result: Result<List<Counter>>) {
        try {
            result.fold(
                onSuccess = { counters ->
                    if (counters.isEmpty()) _countersState.value = HomeState.Empty
                    else _countersState.update { HomeState.Success(counters) }
                },
                onError = { code ->
                    val error = handleError(code.validateErrorCode())
                    _countersState.update { HomeState.Failure(error) }
                },
                onException = {
                    val error = handleError(it.toError())
                    _countersState.update { HomeState.Failure(error) }
                }
            )
        } catch (e: Exception) {
            val error = handleError(e.toError())
            _countersState.update { HomeState.Failure(error) }
        }
    }
}