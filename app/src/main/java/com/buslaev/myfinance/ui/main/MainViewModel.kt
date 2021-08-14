package com.buslaev.myfinance.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.buslaev.myfinance.db.room.DaoHelper
import com.buslaev.myfinance.entities.Operation
import com.buslaev.myfinance.entities.OperationBySum
import com.buslaev.myfinance.other.Constants.EXPENSES_BALANCE
import com.buslaev.myfinance.other.Constants.INCOME_BALANCE
import com.buslaev.myfinance.other.DateHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: DaoHelper
) : ViewModel() {

    private val dateHelper = DateHelper()

    //private var _incomeOperations: MutableLiveData<List<OperationBySum>> = MutableLiveData()
    lateinit var incomeOperations: LiveData<List<OperationBySum>>

    //private var _expensesOperations: MutableLiveData<List<OperationBySum>> = MutableLiveData()
    lateinit var expensesOperations: LiveData<List<OperationBySum>>

    init {
        getDefaultOperation()
    }

    private fun getDefaultOperation() {
        dateHelper.setDatesByToday()
        getOperations(INCOME_BALANCE)
        getOperations(EXPENSES_BALANCE)
    }

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
        if (balance == INCOME_BALANCE) {
            incomeOperations = values
        } else {
            expensesOperations = values
        }

    }

    fun insertOperation(operation: Operation) {
        insertOperationIntoDb(operation)
    }

    private fun insertOperationIntoDb(operation: Operation) = viewModelScope.launch {
        repository.insertOperation(operation)
    }

}