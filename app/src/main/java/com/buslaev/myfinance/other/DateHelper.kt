package com.buslaev.myfinance.other

import org.joda.time.LocalDate
import org.joda.time.format.DateTimeFormat
import java.util.*
import kotlin.collections.HashMap

class DateHelper {

    private val localDate = LocalDate()
    //private val format = DateTimeFormat.forPattern("yyyy-MM-dd")

    private var startDate: String = ""
    private var endDate: String = ""

    fun getStartDate(): String = startDate
    fun getEndDate(): String = endDate

    fun setDatesByToday() {
        val currentDate = localDate.toString()
        setDates(currentDate, currentDate)
    }

    fun setDatesByWeek() {
        val startOfCurrentWeek = localDate.withDayOfWeek(1).toString()
        val endOfCurrentWeek = localDate.dayOfWeek().withMaximumValue().toString()
        setDates(startOfCurrentWeek, endOfCurrentWeek)
    }

    fun setDatesByMonth() {
        val startOfCurrentMonth = localDate.withDayOfMonth(1).toString()
        val endOfCurrentMonth = localDate.dayOfMonth().withMaximumValue().toString()
        setDates(startOfCurrentMonth, endOfCurrentMonth)
    }

    fun setDatesByYear() {
        val startOfCurrentYear = localDate.withDayOfYear(1).toString()
        val endOfCurrentYear = localDate.dayOfYear().withMaximumValue().toString()
        setDates(startOfCurrentYear, endOfCurrentYear)
    }

    //Need to remake
    fun setDatesByAllTime() {
        val startOfCurrentYear = localDate.withDayOfYear(1).toString()
        val endOfCurrentYear = localDate.dayOfYear().withMaximumValue().toString()
        setDates(startOfCurrentYear, endOfCurrentYear)
    }

    private fun setDates(startDate: String, endDate: String) {
        this.startDate = startDate
        this.endDate = endDate
    }

}