package com.buslaev.myfinance.db.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.buslaev.myfinance.entities.Operation

@Dao
interface FinanceDao {

    @Query("SELECT * FROM operations")
    fun getOperations(): LiveData<List<Operation>>

    @Query("SELECT title,SUM(value) as value,icon,account,dateTime,balance FROM operations WHERE balance = :balance AND dateTime BETWEEN :startDate AND :endDate GROUP BY title")
    fun getTotalOperationsByPeriod(
        startDate: String,
        endDate: String,
        balance: String
    ): LiveData<List<Operation>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(operation: Operation)

    @Delete
    suspend fun delete(operation: Operation)
}