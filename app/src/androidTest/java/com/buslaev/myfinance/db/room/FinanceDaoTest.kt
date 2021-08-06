package com.buslaev.myfinance.db.room

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.buslaev.myfinance.entities.Operation
import com.buslaev.myfinance.getOrAwaitValue
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
        val testingItem = Operation(1, "Family", 1000.0, null, "Main", "")
        dao.insert(testingItem)

        val operations = dao.getOperations().getOrAwaitValue()
        assertThat(operations).contains(testingItem)
    }

    @Test
    fun deleteOperation() = runBlockingTest {
        val testingItem = Operation(1, "Family", 1000.0, null, "Main", "")
        dao.insert(testingItem)
        dao.delete(testingItem)

        val operations = dao.getOperations().getOrAwaitValue()
        assertThat(operations).doesNotContain(testingItem)
    }

    @Test
    fun getTotalOperationsByPeriod_contain() = runBlockingTest {
        setupTestingItems()

        val sumOperations =
            dao.getTotalOperationsByPeriod("2021-08-05", "2021-08-07").getOrAwaitValue()

        val resultItem = Operation(null, "Family", 2000.0, null, "Main", "2021-08-06")

        assertThat(sumOperations).contains(resultItem)

    }

    @Test
    fun getTotalOperationsByPeriod_NotContain() = runBlockingTest {
        setupTestingItems()

        val sumOperations =
            dao.getTotalOperationsByPeriod("2021-08-01", "2021-08-03").getOrAwaitValue()

        val resultItem = Operation(null, "Family", 2000.0, null, "Main", "2021-08-06")

        assertThat(sumOperations).doesNotContain(resultItem)

    }
    private suspend fun setupTestingItems(){
        val testingItem1 = Operation(1, "Family", 1000.0, null, "Main", "2021-08-06")
        val testingItem2 = Operation(2, "Family", 1000.0, null, "Main", "2021-08-06")
        dao.insert(testingItem1)
        dao.insert(testingItem2)
    }
}