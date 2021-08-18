package com.buslaev.myfinance.sharedPreferences

import android.content.Context
import com.buslaev.myfinance.utilits.changeMoney


class AccountSharedPreferences(context: Context) {

    private val APP_PREFERENCES = "account"
    private var mAccount = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)

    fun inputIncomeValue(value: String, account: String) {
        val oldValue = getValueDouble(account)
        val result = oldValue + value.toDouble()
        val editor = mAccount.edit()
        editor.putString(account, result.toString())
        editor.apply()
    }

    fun inputExpensesValue(value: String, account: String) {
        val oldValue = getValueDouble(account)
        val result = oldValue - value.toDouble()
        val editor = mAccount.edit()
        editor.putString(account, result.toString())
        editor.apply()
    }

    private fun getValueDouble(account: String): Double {
        var oldValue = getValue(account)
        if (oldValue == "") {
            oldValue = 0.0.toString()
        }
        return oldValue.toDouble()
    }

    fun getValue(account: String): String {
        var result = ""
        if (mAccount.contains(account)) {
            result = mAccount.getString(account, "").toString()
        }
        result = if (result != "") changeMoney(result.toDouble()) else result
        return result
    }

}