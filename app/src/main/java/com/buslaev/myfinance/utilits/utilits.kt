package com.buslaev.myfinance.utilits

import kotlin.math.roundToInt

fun changeMoney(value: Double): String {
    var valueString = value.toString()
    if (value >= 1_000) {
        valueString = value.roundToInt().toString()
        val firstPart = valueString.substring(0, valueString.length - 3)
        val secondPart = valueString.substring(valueString.length - 3)
        return "$firstPart $secondPart"
    }
    return valueString
}