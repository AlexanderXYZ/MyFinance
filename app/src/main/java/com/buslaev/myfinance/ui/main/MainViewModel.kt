package com.buslaev.myfinance.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.buslaev.myfinance.db.room.DaoHelper
import com.buslaev.myfinance.entities.Operation
import com.buslaev.myfinance.other.DateHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: DaoHelper
) : ViewModel() {

    private val dateHelper = DateHelper()

    private var _incomeOperations: MutableLiveData<List<Operation>> = MutableLiveData()
    val incomeOperations: LiveData<List<Operation>> get() = _incomeOperations

    private var _expensesOperations: MutableLiveData<List<Operation>> = MutableLiveData()
    val expensesOperations: LiveData<List<Operation>> get() = _expensesOperations

    fun getIncomeOperations() {}
    fun getExpensesOperation() {}

    fun getOperationsByDay(balance: String) {
        dateHelper.setDatesByToday()
        getOperations(balance)
    }

    fun getOperationsByWeek(balance: String) {
        dateHelper.setDatesByWeek()
        getOperations(balance)
    }

    fun getOperationsByMonth(balance: String) {
        dateHelper.setDatesByMonth()
        getOperations(balance)
    }

    fun getOperationsByYear(balance: String) {
        dateHelper.setDatesByYear()
        getOperations(balance)
    }

    fun getOperationsByAllTime(balance: String) {
        dateHelper.setDatesByAllTime()
        getOperations(balance)
    }

    private fun getOperations(balance: String) {
        val startDate = dateHelper.getStartDate()
        val endDate = dateHelper.getEndDate()
        val values = repository.getOperationsByPeriod(startDate, endDate, balance)
        _incomeOperations.postValue(values.value)
    }

    fun insertOperation(operation: Operation) {
        insertOperationIntoDb(operation)
    }

    private fun insertOperationIntoDb(operation: Operation) = viewModelScope.launch {
        repository.insertOperation(operation)
    }

}