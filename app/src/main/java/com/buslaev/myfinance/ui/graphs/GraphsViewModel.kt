package com.buslaev.myfinance.ui.graphs

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.buslaev.myfinance.db.room.DaoHelper
import com.buslaev.myfinance.entities.OperationBySum
import com.buslaev.myfinance.other.Constants.INCOME_BALANCE
import com.buslaev.myfinance.other.DateHelper
import com.buslaev.myfinance.other.EnumHelper.BarDates
import com.buslaev.myfinance.other.EnumHelper.BarDates.*
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GraphsViewModel @Inject constructor(
    private val repository: DaoHelper
) : ViewModel() {

    private val dateHelper = DateHelper()

    lateinit var operations: LiveData<List<OperationBySum>>

    init {
        getOperations(ON_DAYS, INCOME_BALANCE)
    }

    fun getOperations(dates: BarDates, balance: String) {
        val format = when (dates) {
            ON_DAYS -> {
                dateHelper.getDayFormat()
            }
            ON_WEEK -> {
                dateHelper.getWeekFormat()
            }
            ON_MONTH -> {
                dateHelper.getMonthFormat()
            }
            ON_YEARS -> {
                dateHelper.getYearFormat()
            }
        }
        operations = repository.getOperationsSortedByDate(format, balance)
    }
}