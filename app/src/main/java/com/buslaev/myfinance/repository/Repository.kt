package com.buslaev.myfinance.repository

import androidx.lifecycle.LiveData
import com.buslaev.myfinance.db.room.DaoHelper
import com.buslaev.myfinance.db.room.FinanceDao
import com.buslaev.myfinance.entities.Categories
import com.buslaev.myfinance.entities.Operation
import com.buslaev.myfinance.entities.OperationBySum
import javax.inject.Inject

class Repository @Inject constructor(
    private val dao: FinanceDao
) : DaoHelper {
    override fun getOperationsByPeriod(
        startDate: String,
        endDate: String,
        balance: String
    ): LiveData<List<OperationBySum>> =
        dao.getSumOperationsWithCategoriesByPeriod(startDate, endDate, balance)

    override fun getCategories(balance: String): LiveData<List<Categories>> =
        dao.getCategories(balance)

    override fun getOperationsSortedByDate(
        format: String,
        balance: String
    ): LiveData<List<OperationBySum>> =
        dao.getOperationsSortedByDate(format, balance)

    override suspend fun insertOperation(operation: Operation) {
        dao.insertOperation(operation)
    }

    override suspend fun deleteOperation(operation: Operation) {
        dao.deleteOperation(operation)
    }

    override suspend fun insertCategory(category: Categories) {
        dao.insertCategory(category)
    }

    override suspend fun deleteCategory(category: Categories) {
        dao.deleteCategory(category)
    }

}