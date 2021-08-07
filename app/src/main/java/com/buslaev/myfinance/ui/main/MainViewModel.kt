package com.buslaev.myfinance.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.buslaev.myfinance.entities.Operation
import com.buslaev.myfinance.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private val calendar = Calendar.getInstance()

    private var _incomeOperations: MutableLiveData<List<Operation>> = MutableLiveData()
    val incomeOperations: LiveData<List<Operation>> get() = _incomeOperations

    private var _expensesOperations: MutableLiveData<List<Operation>> = MutableLiveData()
    val expensesOperations: LiveData<List<Operation>> get() = _expensesOperations

    fun getIncomeOperations() {}
    fun getExpensesOperation() {}

    fun getOperationsByDay(balance: String) {}
    fun getOperationsByWeak(balance: String) {}
    fun getOperationsByMonth(balance: String) {}
    fun getOperationsByYear(balance: String) {}
    fun getOperationsByAllTime(balance: String) {}

}