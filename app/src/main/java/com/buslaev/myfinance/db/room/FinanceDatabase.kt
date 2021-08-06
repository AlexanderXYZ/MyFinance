package com.buslaev.myfinance.db.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.buslaev.myfinance.entities.Operation

@Database(entities = [Operation::class], version = 1)
abstract class FinanceDatabase : RoomDatabase() {

    abstract fun getDao(): FinanceDao
}