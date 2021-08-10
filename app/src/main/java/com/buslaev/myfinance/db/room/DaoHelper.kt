package com.buslaev.myfinance.db.room

import androidx.lifecycle.LiveData
import com.buslaev.myfinance.entities.Categories
import com.buslaev.myfinance.entities.Operation

interface DaoHelper {

    fun getOperationsByPeriod(
        startDate: String,
        endDate: String,
        balance: String
    ): LiveData<List<Operation>>

    suspend fun insertOperation(operation: Operation)
    suspend fun deleteOperation(operation: Operation)

    suspend fun insertCategory(category: Categories)
    suspend fun deleteCategory(category: Categories)
}