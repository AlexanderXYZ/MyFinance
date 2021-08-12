package com.buslaev.myfinance.ui.main.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.buslaev.myfinance.db.room.DaoHelper
import com.buslaev.myfinance.entities.Categories
import com.buslaev.myfinance.entities.Operation
import com.buslaev.myfinance.entities.OperationBySum
import org.joda.time.LocalDate

class FakeRepository : DaoHelper {

    private var operationsItems = mutableListOf<Operation>()
    private val observableOperations = MutableLiveData<List<Operation>>(operationsItems)

    private var categoriesItems = mutableListOf<Categories>()
    private val observableCategories = MutableLiveData<List<Categories>>(categoriesItems)

    private fun refreshLiveData() {
        observableOperations.postValue(operationsItems)
    }

    override fun getOperationsByPeriod(
        startDate: String,
        endDate: String,
        balance: String
    ): LiveData<List<OperationBySum>> {
        setOperationsByPeriod(startDate, endDate)
        //        setOperationsSumByValue()
//        refreshLiveData()
        return getOperationsWithCategory()
    }

    private fun setOperationsByPeriod(startDate: String, endDate: String) {
        val startLocal = LocalDate(startDate)
        val endLocal = LocalDate(endDate)
        val result = mutableListOf<Operation>()
        for (i in operationsItems) {
            val date = LocalDate(i.dateTime)
            if ((!date.isBefore(startLocal) && date.isBefore(endLocal)) || date == startLocal) {
                result.add(i)
            }
        }
        operationsItems = result
    }

    private fun getOperationsWithCategory(): LiveData<List<OperationBySum>> {
        val list = mutableListOf<OperationBySum>()
        val result = MutableLiveData<List<OperationBySum>>()

        for (i in operationsItems) {
//            for (j in categoriesItems) {
//                if (i.idCategory == j.idCategory) {
            val operationBySum = OperationBySum(
                i.value,
                i.account,
                i.dateTime,
                i.balance,
                "Family",
                null,
                ""
            )
            list.add(operationBySum)
//                }
//            }
        }
        result.postValue(list)
        return result
    }

    override suspend fun insertOperation(operation: Operation) {
        operationsItems.add(operation)
        refreshLiveData()
    }

    override suspend fun deleteOperation(operation: Operation) {
        operationsItems.remove(operation)
        refreshLiveData()
    }

//    private fun setOperationsSumByValue() {
//        val result = mutableListOf<Operation>()
//        val value = operationsItems.sumOf { it.value }
//        val dateTime = operationsItems[0].dateTime
//        val operation =
//            Operation(null, "Family", value, null, "", dateTime, "income")
//        result.add(operation)
//        operationsItems = result
//    }


    override fun getCategories(balance: String): LiveData<List<Categories>> {
        return observableCategories
    }

    override suspend fun insertCategory(category: Categories) {
        categoriesItems.add(category)
        refreshCategoriesLiveData()
    }

    override suspend fun deleteCategory(category: Categories) {
        categoriesItems.remove(category)
        refreshCategoriesLiveData()
    }

    private fun refreshCategoriesLiveData() {
        observableCategories.postValue(categoriesItems)
    }
}