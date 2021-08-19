package com.buslaev.myfinance.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "accounts")
data class Account(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    var title: String = "",
    var money: Double = 0.0,
    var currency: String = "",
    var icon: Int? = null,
    var backgroundColor: String = "#FFBB86FC"
)
