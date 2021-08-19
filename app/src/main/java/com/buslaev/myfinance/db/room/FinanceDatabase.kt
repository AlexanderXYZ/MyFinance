package com.buslaev.myfinance.db.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.buslaev.myfinance.entities.Account
import com.buslaev.myfinance.entities.Categories
import com.buslaev.myfinance.entities.Operation

@Database(
    entities = [Operation::class, Categories::class, Account::class],
    version = 4,
    exportSchema = true
)
abstract class FinanceDatabase : RoomDatabase() {

    abstract fun getDao(): FinanceDao
}