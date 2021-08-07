package com.buslaev.myfinance.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "operations")
data class Operation(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int? = null,
    @ColumnInfo(name = "title") var title: String = "",
    @ColumnInfo(name = "value") var value: Double = 0.0,
    @ColumnInfo(name = "icon") var icon: Int? = null,
    @ColumnInfo(name = "account") var account: String = "",
    @ColumnInfo(name = "dateTime") var dateTime: String = "",
    @ColumnInfo(name = "balance") var balance: String = ""
)
