package com.buslaev.myfinance.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "operations")
data class Operation(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int? = null,
    @ColumnInfo(name = "value") var value: Double = 0.0,
    @ColumnInfo(name = "account") var account: String = "",
    @ColumnInfo(name = "dateTime") var dateTime: String = "",
    @ColumnInfo(name = "balance") var balance: String = "",
    @ColumnInfo(name = "idCategory") var idCategory: Int? = null
)
