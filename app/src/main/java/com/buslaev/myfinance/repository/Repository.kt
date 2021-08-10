package com.buslaev.myfinance.repository

import androidx.lifecycle.LiveData
import com.buslaev.myfinance.db.room.DaoHelper
import com.buslaev.myfinance.db.room.FinanceDao
import com.buslaev.myfinance.entities.Categories
import com.buslaev.myfinance.entities.Operation
import javax.inject.Inject

class Repository @Inject constructor(
    private val dao: FinanceDao
):DaoHelper {
    override fun getOperationsByPeriod(
        startDate: String,
        endDate: String,
        balance: String
    ): LiveData<List<Operation>> = dao.getOperationsByPeriod(startDate, endDate, balance)

    override suspend fun insertOperation(operation: Operation) {
        dao.insert(operation)
    }

    override suspend fun deleteOperation(operation: Operation) {
        dao.delete(operation)
    }

    override suspend fun insertCategory(category: Categories) {

    }

    override suspend fun deleteCategory(category: Categories) {

    }
}