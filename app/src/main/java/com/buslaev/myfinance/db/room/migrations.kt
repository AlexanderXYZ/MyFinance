package com.buslaev.myfinance.db.room

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATE_3_4 = object : Migration(3, 4) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("CREATE TABLE `accounts` (`id` INTEGER,`title` TEXT NOT NULL,`money` REAL NOT NULL,`currency` TEXT NOT NULL, `icon` INTEGER,`backgroundColor` TEXT NOT NULL, PRIMARY KEY(`id`))")
    }
}