package com.buslaev.myfinance.db.room

import androidx.lifecycle.LiveData
import com.buslaev.myfinance.entities.Operation
import javax.inject.Inject

class DaoHelperIml @Inject constructor(
    private val dao: FinanceDao
) : DaoHelper {

    override fun getOperations(): LiveData<List<Operation>> = dao.getOperations()

    override fun getTotalOperationsByPeriod(): LiveData<List<Operation>> =
        dao.getTotalOperationsByPeriod()

    override suspend fun insert(operation: Operation) {
        dao.insert(operation)
    }

    override suspend fun delete(operation: Operation) {
        dao.delete(operation)
    }
}