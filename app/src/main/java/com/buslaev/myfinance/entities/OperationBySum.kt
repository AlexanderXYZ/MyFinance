package com.buslaev.myfinance.entities


data class OperationBySum(
    var value: Double = 0.0,
    var account: String = "",
    var dateTime: String = "",
    var balance: String = "",
    var titleCategory: String = "",
    var iconCategory: Int? = null,
    var backgroundColor: String = "#FFBB86FC",
)
