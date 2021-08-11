package com.buslaev.myfinance.ui.categories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.buslaev.myfinance.db.room.DaoHelper
import com.buslaev.myfinance.entities.Categories
import com.buslaev.myfinance.other.Constants.EXPENSES_BALANCE
import com.buslaev.myfinance.other.Constants.INCOME_BALANCE
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CategoriesViewModel @Inject constructor(
    private val repository: DaoHelper
) : ViewModel() {

    val categoriesIncome: LiveData<List<Categories>> = repository.getCategories(INCOME_BALANCE)
    val categoriesExpenses: LiveData<List<Categories>> = repository.getCategories(EXPENSES_BALANCE)

}