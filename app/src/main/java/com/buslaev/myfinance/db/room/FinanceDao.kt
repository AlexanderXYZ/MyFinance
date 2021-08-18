package com.buslaev.myfinance.db.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.buslaev.myfinance.entities.Categories
import com.buslaev.myfinance.entities.Operation
import com.buslaev.myfinance.entities.OperationBySum
import com.buslaev.myfinance.entities.relations.OperationAndCategories

@Dao
interface FinanceDao {

    /*
    Operations
     */
    @Query("SELECT * FROM operations")
    fun getOperations(): LiveData<List<Operation>>

//    @Query("SELECT SUM(value) as value,account,dateTime,balance,idCategory FROM operations WHERE balance = :balance AND dateTime BETWEEN :startDate AND :endDate GROUP BY idCategory")
//    fun getOperationsByPeriod(
//        startDate: String,
//        endDate: String,
//        balance: String
//    ): LiveData<List<Operation>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOperation(operation: Operation)

    @Delete
    suspend fun deleteOperation(operation: Operation)

    /*
    Categories
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: Categories)

    @Delete
    suspend fun deleteCategory(category: Categories)

    @Query("SELECT * FROM categories WHERE balance = :balance")
    fun getCategories(balance: String): LiveData<List<Categories>>

//    @Transaction
//    @Query("SELECT * FROM operations WHERE balance = :balance AND dateTime BETWEEN :startDate AND :endDate GROUP BY idCategory")
//    fun getOperationsWithCategoriesByPeriod(
//        startDate: String,
//        endDate: String,
//        balance: String
//    ): LiveData<List<OperationAndCategories>>

    @Transaction
    @Query("SELECT * FROM operations")
    fun getOperationsWithCategories(): LiveData<List<OperationAndCategories>>

    @Query("SELECT SUM(o.value) as value,o.account,o.dateTime,o.balance,c.title as titleCategory,c.icon as iconCategory,c.backgroundColor FROM operations as o INNER JOIN categories as c ON o.idCategory=c.idCategory WHERE o.balance = :balance AND o.dateTime BETWEEN :startDate AND :endDate GROUP BY o.idCategory ")
    fun getSumOperationsWithCategoriesByPeriod(
        startDate: String,
        endDate: String,
        balance: String
    ): LiveData<List<OperationBySum>>

    @Query("SELECT SUM(o.value) as value,o.account,o.dateTime,o.balance,c.title as titleCategory,c.icon as iconCategory,c.backgroundColor FROM operations as o INNER JOIN categories as c ON o.idCategory=c.idCategory WHERE o.balance = :balance GROUP BY o.idCategory AND strftime(:format,o.dateTime) ORDER BY date(o.dateTime) DESC")
    fun getOperationsSortedByDate(
        format: String,
        balance: String
    ): LiveData<List<OperationBySum>>

    @Query("SELECT SUM(o.value) as value,o.account,o.dateTime,o.balance,c.title as titleCategory,c.icon as iconCategory,c.backgroundColor FROM operations as o INNER JOIN categories as c ON o.idCategory=c.idCategory GROUP BY strftime(:format,o.dateTime) ORDER BY date(o.dateTime) DESC")
    fun getAllOperationsSortedByDate(
        format: String
    ):LiveData<List<OperationBySum>>
}