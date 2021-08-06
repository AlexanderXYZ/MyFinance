package com.buslaev.myfinance.repository

import androidx.lifecycle.LiveData
import com.buslaev.myfinance.db.room.DaoHelper
import com.buslaev.myfinance.entities.Operation
import javax.inject.Inject

class Repository @Inject constructor(
    private val daoHelper: DaoHelper
) {
    fun getOperations(): LiveData<List<Operation>> = daoHelper.getOperations()
    fun getTotalOperationsByDate(startDate: String, endDate: String): LiveData<List<Operation>> =
        daoHelper.getTotalOperationsByPeriod(startDate, endDate)

    suspend fun insert(operation: Operation) {
        daoHelper.insert(operation)
    }

    suspend fun delete(operation: Operation) {
        daoHelper.delete(operation)
    }
}