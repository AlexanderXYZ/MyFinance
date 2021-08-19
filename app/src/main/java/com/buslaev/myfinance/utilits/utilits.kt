package com.buslaev.myfinance.utilits

import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.Menu
import android.view.MenuItem
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

fun removeItemsUnderline(menu: Menu) {
    val size = menu.size()
    for (i in 0 until size) {
        val item = menu.getItem(i)
        item.title = item.title.toString()
    }
}

fun underlineMenuItem(item: MenuItem) {
    val content = SpannableString(item.title)
    content.setSpan(UnderlineSpan(), 0, content.length, 0)
    item.title = content
}