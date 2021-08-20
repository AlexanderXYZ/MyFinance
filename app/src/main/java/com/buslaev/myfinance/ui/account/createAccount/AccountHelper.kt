package com.buslaev.myfinance.ui.account.createAccount

import com.buslaev.myfinance.entities.Account

class AccountHelper {

    private var title: String = ""
    private var money: Double = 0.0
    private var currency: String = ""
    private var icon: Int? = null
    private var backgroundColor: String = ""

    fun setTitle(title: String) {
        this.title = title
    }

    fun setMoney(money: Double) {
        this.money = money
    }

    fun setCurrency(currency: String) {
        this.currency = currency
    }

    fun setIcon(icon: Int) {
        this.icon = icon
    }

    fun setBackgroundColor(backgroundColor: String) {
        this.backgroundColor = backgroundColor
    }

    fun isCorrectAttributes(): Boolean {
        if (title == "") {
            return false
        }
        if (currency == "") {
            return false
        }
        if (icon == null) {
            return false
        }
        if (backgroundColor == "") {
            return false
        }
        return true
    }

    fun getAccount(): Account {
        return Account(
            title = title,
            money = money,
            currency = currency,
            icon = icon,
            backgroundColor = backgroundColor
        )
    }
}