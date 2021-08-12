package com.buslaev.myfinance.utilits

import com.buslaev.myfinance.entities.Operation
import com.buslaev.myfinance.other.Constants.INCOME_BALANCE
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class CorrectValuesTest {

    private val correctValues = CorrectValues()

    @Test
    fun `check correct operation, return true`() {
        //val operation = Operation(1, 1000.0, "main", "2021-08-06", INCOME_BALANCE, 1)
        val result = correctValues.checkOperation(1000.0, "main", "2021-08-06", INCOME_BALANCE, 1)
        assertThat(result).isTrue()
    }

    @Test
    fun `check correct category, return true`() {
        val result = correctValues.checkCategories("Family", 312312, "", INCOME_BALANCE)
        assertThat(result).isTrue()
    }
}