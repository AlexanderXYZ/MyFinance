package com.buslaev.myfinance.db.room

import androidx.lifecycle.LiveData
import com.buslaev.myfinance.entities.Account
import com.buslaev.myfinance.entities.Categories
import com.buslaev.myfinance.entities.Operation
import com.buslaev.myfinance.entities.OperationBySum

interface DaoHelper {

    fun getOperationsByPeriod(
        startDate: String,
        endDate: String,
        balance: String
    ): LiveData<List<OperationBySum>>

    fun getCategories(balance: String): LiveData<List<Categories>>

    fun getOperationsSortedByDate(format: String, balance: String): LiveData<List<OperationBySum>>

    fun getAllOperationsSortedByDate(format: String): LiveData<List<OperationBySum>>

    fun getAccounts(): LiveData<List<Account>>

    suspend fun insertOperation(operation: Operation)
    suspend fun deleteOperation(operation: Operation)

    suspend fun insertCategory(category: Categories)
    suspend fun deleteCategory(category: Categories)

    suspend fun insertAccount(account: Account)
    suspend fun deleteAccount(account: Account)
}