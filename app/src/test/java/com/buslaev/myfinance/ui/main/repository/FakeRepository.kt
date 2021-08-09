package com.buslaev.myfinance.ui.main.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.buslaev.myfinance.db.room.DaoHelper
import com.buslaev.myfinance.entities.Operation
import org.joda.time.LocalDate

class FakeRepository : DaoHelper {

    private var operationsItems = mutableListOf<Operation>()

    private val observableOperations = MutableLiveData<List<Operation>>(operationsItems)

    private fun refreshLiveData() {
        observableOperations.postValue(operationsItems)
    }

    override fun getOperationsByPeriod(
        startDate: String,
        endDate: String,
        balance: String
    ): LiveData<List<Operation>> {
        setOperationsByPeriod(startDate, endDate)
//        setOperationsSumByValue()
        refreshLiveData()
        return observableOperations
    }

    private fun setOperationsByPeriod(startDate: String, endDate: String) {
        val startLocal = LocalDate(startDate)
        val endLocal = LocalDate(endDate)
        val result = mutableListOf<Operation>()
        for (i in operationsItems) {
            val date = LocalDate(i.dateTime)
            if ((!date.isBefore(startLocal) && date.isBefore(endLocal)) || date == startLocal) {
                result.add(i)
            }
        }
        operationsItems = result
    }

//    private fun setOperationsSumByValue() {
//        val result = mutableListOf<Operation>()
//        val value = operationsItems.sumOf { it.value }
//        val dateTime = operationsItems[0].dateTime
//        val operation =
//            Operation(null, "Family", value, null, "", dateTime, "income")
//        result.add(operation)
//        operationsItems = result
//    }

    override suspend fun insert(operation: Operation) {
        operationsItems.add(operation)
        refreshLiveData()
    }

    override suspend fun delete(operation: Operation) {
        operationsItems.remove(operation)
        refreshLiveData()
    }
}