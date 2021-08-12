package com.buslaev.myfinance.ui.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.buslaev.myfinance.entities.Operation
import com.buslaev.myfinance.entities.OperationBySum
import com.buslaev.myfinance.other.Constants.INCOME_BALANCE
import com.buslaev.myfinance.ui.main.repository.FakeRepository
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.joda.time.LocalDate
import org.joda.time.LocalDateTime
import org.junit.Before
import org.junit.Rule
import org.junit.Test


@ExperimentalCoroutinesApi
class MainViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: MainViewModel
    private val localDate = LocalDate()
    private val expectedOperation =
        OperationBySum(1000.0, "", localDate.toString(), INCOME_BALANCE, "Family", null, "")

    @Before
    fun setup() {
        viewModel = MainViewModel(FakeRepository())
    }

    private fun setupTestingValues(day: String) {
        val operation1 =
            Operation(null, 1000.0, "", day, INCOME_BALANCE)
        viewModel.insertOperation(operation1)
    }

    @Test
    fun `get operations by day, contained`() {
        val testingDate = localDate.toString()
        setupTestingValues(testingDate)

        viewModel.getOperationsByDay(INCOME_BALANCE)
        val value = viewModel.incomeOperations.getOrAwaitValueTest()

        assertThat(value).contains(expectedOperation)
    }

    @Test
    fun `get operations by day, not contained`() {
        val testingDate = localDate.minusDays(2).toString()
        setupTestingValues(testingDate)
        viewModel.getOperationsByDay(INCOME_BALANCE)
        val value = viewModel.incomeOperations.getOrAwaitValueTest()

        assertThat(value).doesNotContain(expectedOperation)
    }

    @Test
    fun `get operations by weak, contained`() {
        val testingDate = localDate.toString()
        setupTestingValues(testingDate)
        viewModel.getOperationsByWeek(INCOME_BALANCE)
        val value = viewModel.incomeOperations.getOrAwaitValueTest()

        assertThat(value).contains(expectedOperation)
    }

    @Test
    fun `get operations by weak, not contained`() {
        val testingDate = localDate.minusWeeks(2).toString()
        setupTestingValues(testingDate)
        viewModel.getOperationsByWeek(INCOME_BALANCE)
        val value = viewModel.incomeOperations.getOrAwaitValueTest()


        assertThat(value).doesNotContain(expectedOperation)
    }

    @Test
    fun `get operations by month, contained`() {
        val testingDate = localDate.toString()
        setupTestingValues(testingDate)

        viewModel.getOperationsByMonth(INCOME_BALANCE)
        val value = viewModel.incomeOperations.getOrAwaitValueTest()

        assertThat(value).contains(expectedOperation)
    }

    @Test
    fun `get operations by month, not contained`() {
        val testingDate = localDate.minusMonths(2).toString()
        setupTestingValues(testingDate)

        viewModel.getOperationsByMonth(INCOME_BALANCE)
        val value = viewModel.incomeOperations.getOrAwaitValueTest()

        assertThat(value).doesNotContain(expectedOperation)
    }

    @Test
    fun `get operations by year, contained`() {
        val testingDate = localDate.toString()
        setupTestingValues(testingDate)

        viewModel.getOperationsByMonth(INCOME_BALANCE)
        val value = viewModel.incomeOperations.getOrAwaitValueTest()

        assertThat(value).contains(expectedOperation)
    }

    @Test
    fun `get operations by year, not contained`() {
        val testingDate = localDate.minusYears(2).toString()
        setupTestingValues(testingDate)

        viewModel.getOperationsByMonth(INCOME_BALANCE)
        val value = viewModel.incomeOperations.getOrAwaitValueTest()

        assertThat(value).doesNotContain(expectedOperation)
    }

    @Test
    fun `get operations by all time, contained`() {
        val testingDate = localDate.toString()
        setupTestingValues(testingDate)

        viewModel.getOperationsByAllTime(INCOME_BALANCE)
        val value = viewModel.incomeOperations.getOrAwaitValueTest()

        assertThat(value).contains(expectedOperation)
    }
}