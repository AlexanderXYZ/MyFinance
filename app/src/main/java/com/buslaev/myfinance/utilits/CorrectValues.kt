package com.buslaev.myfinance.utilits

import com.buslaev.myfinance.entities.Categories
import com.buslaev.myfinance.entities.Operation
import com.buslaev.myfinance.other.Constants.EXPENSES_BALANCE
import com.buslaev.myfinance.other.Constants.INCOME_BALANCE

class CorrectValues {

    private var correctOperation: Operation? = null
    private var correctCategory: Categories? = null

    fun checkOperation(
        value: Double,
        account: String,
        dateTime: String,
        balance: String,
        idCategory: Int
    ): Boolean {
        if (value <= 0.0) {
            return false
        }
        if (dateTime.isEmpty()) {
            return false
        }
        if (balance != INCOME_BALANCE || balance != EXPENSES_BALANCE) {
            return false
        }
        if (idCategory <= 0) {
            return false
        }
        return true
    }

    fun getCorrectOperation(): Operation {
        return correctOperation ?: Operation()
    }

    fun checkCategories(
        titleCategory: String,
        icon: Int,
        backGroundColor: String,
        balance: String
    ): Boolean {
        return false
    }

    fun getCorrectCategory(): Categories {
        return correctCategory ?: Categories()
    }


}