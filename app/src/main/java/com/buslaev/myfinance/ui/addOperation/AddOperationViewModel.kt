package com.buslaev.myfinance.ui.addOperation

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.buslaev.myfinance.db.room.DaoHelper
import com.buslaev.myfinance.entities.Categories
import com.buslaev.myfinance.entities.Operation
import com.buslaev.myfinance.other.Constants.EXPENSES_BALANCE
import com.buslaev.myfinance.other.Constants.INCOME_BALANCE
import com.buslaev.myfinance.sharedPreferences.AccountSharedPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddOperationViewModel @Inject constructor(
    private val repository: DaoHelper,
    private val accountSharedPreferences: AccountSharedPreferences
) : ViewModel() {

    val categoriesIncome: LiveData<List<Categories>> = repository.getCategories(INCOME_BALANCE)
    val categoriesExpenses: LiveData<List<Categories>> = repository.getCategories(EXPENSES_BALANCE)

    fun addOperation(operation: Operation) {
        addOperationToDb(operation)
    }

    private fun addOperationToDb(operation: Operation) = viewModelScope.launch {
        repository.insertOperation(operation)
    }

    fun changeValueOfAccountIncome(value: Double, account: String) {
        accountSharedPreferences.inputIncomeValue(value = value.toString(), account = account)
    }

    fun changeValueOfAccountExpenses(value: Double, account: String) {
        accountSharedPreferences.inputExpensesValue(value = value.toString(), account = account)
    }
}