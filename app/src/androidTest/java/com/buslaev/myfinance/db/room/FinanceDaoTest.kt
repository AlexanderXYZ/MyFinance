package com.buslaev.myfinance.db.room

import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.buslaev.myfinance.entities.Categories
import com.buslaev.myfinance.entities.Operation
import com.buslaev.myfinance.entities.OperationBySum
import com.buslaev.myfinance.entities.relations.OperationAndCategories
import com.buslaev.myfinance.getOrAwaitValue
import com.buslaev.myfinance.other.Constants.INCOME_BALANCE
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class FinanceDaoTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: FinanceDatabase
    private lateinit var dao: FinanceDao

    private val defaultCategory = Categories(1, "Family", null, "", INCOME_BALANCE)
    private val defaultOperation =
        Operation(1, 1000.0, "", "2021-08-06", INCOME_BALANCE, 1)

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            FinanceDatabase::class.java
        ).allowMainThreadQueries().build()
        dao = database.getDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertOperation() = runBlockingTest {
        val operation = Operation(1, 1000.0, "Main", "", "")
        dao.insertOperation(operation)

        val operations = dao.getOperations().getOrAwaitValue()
        assertThat(operations).contains(operation)
    }

    @Test
    fun deleteOperation() = runBlockingTest {
        val operation = Operation(1, 1000.0, "Main", "", "")
        dao.insertOperation(operation)
        dao.deleteOperation(operation)

        val operations = dao.getOperations().getOrAwaitValue()
        assertThat(operations).doesNotContain(operation)
    }

    @Test
    fun insertCategory() = runBlockingTest {
        val category = Categories(1, "", null, "", INCOME_BALANCE)
        dao.insertCategory(category)

        val result = dao.getCategories(INCOME_BALANCE).getOrAwaitValue()
        Log.d("TAG",result.toString())
        assertThat(result).contains(category)
    }

    @Test
    fun deleteCategory() = runBlockingTest {
        val category = Categories(1, "", null, "", "")
        dao.insertCategory(category)
        dao.deleteCategory(category)

        val result = dao.getCategories(INCOME_BALANCE).getOrAwaitValue()
        assertThat(result).doesNotContain(category)
    }

//    @Test
//    fun getTotalOperationsByPeriod_contain() = runBlockingTest {
//        setupTestingItems()
//        val sumOperations = getSumOperationContains()
//        val resultItem = Operation(null, 2000.0, "Main", "2021-08-06", "income")
//
//        assertThat(sumOperations).contains(resultItem)
//    }
//
//    @Test
//    fun getTotalOperationsByPeriod_NotContain() = runBlockingTest {
//        setupTestingItems()
//        val sumOperations = getSumOperationNotContains()
//        val resultItem = Operation(null, 2000.0, "Main", "2021-08-06", "income")
//
//        assertThat(sumOperations).doesNotContain(resultItem)
//    }
//
//    @Test
//    fun getTotalOperationsByPeriodIncome_contain() = runBlockingTest {
//        setupTestingItems()
//        val sumOperations = getSumOperationContains()
//        val resultItem = Operation(null, 2000.0, "Main", "2021-08-06", "income")
//
//        assertThat(sumOperations).contains(resultItem)
//    }
//
//    fun getTotalOperationsByPeriodIncome_notContain() = runBlockingTest {
//        setupTestingItems()
//        val sumOperations = getSumOperationNotContains()
//        val resultItem = Operation(null, 2000.0, "Main", "2021-08-06", "expenses")
//        assertThat(sumOperations).doesNotContain(resultItem)
//    }
//
//    private suspend fun setupTestingItems() {
//        val testingItem1 = Operation(1, 1000.0, "Main", "2021-08-06", "income")
//        val testingItem2 = Operation(2, 1000.0, "Main", "2021-08-06", "income")
//        dao.insertOperation(testingItem1)
//        dao.insertOperation(testingItem2)
//    }
//
//    private fun getSumOperationContains(): List<Operation> {
//        return dao.getOperationsByPeriod("2021-08-05", "2021-08-07", INCOME_BALANCE)
//            .getOrAwaitValue()
//    }
//
//    private fun getSumOperationNotContains(): List<Operation> {
//        return dao.getOperationsByPeriod("2021-08-01", "2021-08-03", INCOME_BALANCE)
//            .getOrAwaitValue()
//    }

    @Test
    fun getOperationsWithCategories_contain() = runBlockingTest {
        dao.insertCategory(defaultCategory)
        dao.insertOperation(defaultOperation)

        val result = dao.getOperationsWithCategories().getOrAwaitValue()
        val expected = OperationAndCategories(defaultOperation, defaultCategory)
        Log.d("TAG", result.toString())

        assertThat(result).contains(expected)
    }

    @Test
    fun getOperationsWithCategoriesAfterDeletingCategory_doesNotContain() = runBlockingTest {
        dao.insertCategory(defaultCategory)
        dao.insertOperation(defaultOperation)

        dao.deleteCategory(defaultCategory)

        val result = dao.getOperationsWithCategories().getOrAwaitValue()
        val expected = OperationAndCategories(defaultOperation, defaultCategory)
        Log.d("TAG", result.toString())

        assertThat(result).doesNotContain(expected)
    }

//    @Test
//    fun getOperationsWithCategoriesByPeriod_contain() = runBlockingTest {
//        dao.insertCategory(defaultCategory)
//        dao.insertOperation(defaultOperation)
//
//        val result =
//            dao.getOperationsWithCategoriesByPeriod("2021-08-01", "2021-08-20", INCOME_BALANCE)
//                .getOrAwaitValue()
//        val expected = OperationAndCategories(defaultOperation, defaultCategory)
//        Log.d("TAG", result.toString())
//
//        assertThat(result).contains(expected)
//    }
//
//    @Test
//    fun getOperationsWithCategoriesByPeriod_doesNotContain() = runBlockingTest {
//        dao.insertCategory(defaultCategory)
//        dao.insertOperation(defaultOperation)
//
//        val result =
//            dao.getOperationsWithCategoriesByPeriod("2021-08-01", "2021-08-02", INCOME_BALANCE)
//                .getOrAwaitValue()
//
//        val expected = OperationAndCategories(defaultOperation, defaultCategory)
//        Log.d("TAG", result.toString())
//
//        assertThat(result).doesNotContain(expected)
//    }

    @Test
    fun getSumOperationsWithCategoriesByPeriod_contain() = runBlockingTest {
        dao.insertCategory(defaultCategory)
        dao.insertOperation(defaultOperation)
        val defaultOperation2 =
            Operation(2, 1000.0, "", "2021-08-06", INCOME_BALANCE, 1)
        dao.insertOperation(defaultOperation2)

        val result =
            dao.getSumOperationsWithCategoriesByPeriod("2021-08-01", "2021-08-20", INCOME_BALANCE)
                .getOrAwaitValue()

        val expected =
            OperationBySum(2000.0, "", "2021-08-06", INCOME_BALANCE, "Family", null, "")
        Log.d("TAG", result.toString())

        assertThat(result).contains(expected)
    }

    @Test
    fun getSumOperationsWithCategoriesByPeriod_doesNotContain() = runBlockingTest {
        dao.insertCategory(defaultCategory)
        dao.insertOperation(defaultOperation)
        val defaultOperation2 =
            Operation(2, 1000.0, "", "2021-08-06", INCOME_BALANCE, 1)
        dao.insertOperation(defaultOperation2)

        val result =
            dao.getSumOperationsWithCategoriesByPeriod("2021-08-01", "2021-08-02", INCOME_BALANCE)
                .getOrAwaitValue()

        val expected =
            OperationBySum(2000.0, "", "2021-08-06", INCOME_BALANCE, "Family", null, "")
        Log.d("TAG", result.toString())

        assertThat(result).doesNotContain(expected)
    }
}