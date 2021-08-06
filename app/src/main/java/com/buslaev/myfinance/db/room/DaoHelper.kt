package com.buslaev.myfinance.db.room

import androidx.lifecycle.LiveData
import com.buslaev.myfinance.entities.Operation

interface DaoHelper {

    fun getOperations(): LiveData<List<Operation>>
    fun getTotalOperationsByPeriod(): LiveData<List<Operation>>

    suspend fun insert(operation: Operation)
    suspend fun delete(operation: Operation)
}