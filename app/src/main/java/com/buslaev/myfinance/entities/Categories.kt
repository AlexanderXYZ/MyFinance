package com.buslaev.myfinance.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categories")
data class Categories(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int? = null,
    @ColumnInfo(name = "title") var title: String = "",
    @ColumnInfo(name = "icon") var icon: Int? = null,
    @ColumnInfo(name = "backgroundColor") var backgroundColor: Int = 0,
    @ColumnInfo(name = "balance") var balance: String = ""
)
