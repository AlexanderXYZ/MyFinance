package com.buslaev.myfinance.db.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.buslaev.myfinance.entities.Operation

@Dao
interface FinanceDao {

    @Query("SELECT * FROM operations")
    fun getOperations(): LiveData<List<Operation>>

    //@Query("SELECT id,title,SUM(value),icon,account,dateTime FROM operations GROUP BY title")
    @Query("SELECT * FROM operations")
    fun getTotalOperationsByPeriod(): LiveData<List<Operation>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(operation: Operation)

    @Delete
    suspend fun delete(operation: Operation)
}