package com.buslaev.myfinance.ui.main.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.buslaev.myfinance.db.room.DaoHelper
import com.buslaev.myfinance.entities.Operation

class FakeRepository : DaoHelper {

    private val operationsItems = mutableListOf<Operation>()

    private val observableOperations = MutableLiveData<List<Operation>>(operationsItems)

    private fun refreshLiveData() {
        observableOperations.postValue(getOperationsSumByValue())
    }

    private fun getOperationsSumByValue(): List<Operation> {
        val result = mutableListOf<Operation>()
        val value = operationsItems.sumOf { it.value }
        val operation = Operation(null, "Family", value, null, "", "", "income")
        result.add(operation)
        return result
    }

    override fun getOperationsByPeriod(
        startDate: String,
        endDate: String,
        balance: String
    ): LiveData<List<Operation>> {
        return observableOperations
    }

    override suspend fun insert(operation: Operation) {
        operationsItems.add(operation)
        refreshLiveData()
    }

    override suspend fun delete(operation: Operation) {
        operationsItems.remove(operation)
        refreshLiveData()
    }
}