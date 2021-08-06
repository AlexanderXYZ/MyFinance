package com.buslaev.myfinance.di

import android.content.Context
import androidx.room.Room
import com.buslaev.myfinance.db.room.DaoHelper
import com.buslaev.myfinance.db.room.DaoHelperIml
import com.buslaev.myfinance.db.room.FinanceDao
import com.buslaev.myfinance.db.room.FinanceDatabase
import com.buslaev.myfinance.other.Constants.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFinanceDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(context, FinanceDatabase::class.java, DATABASE_NAME).build()

    @Provides
    @Singleton
    fun provideDao(database: FinanceDatabase): FinanceDao = database.getDao()

    @Provides
    @Singleton
    fun provideDaoHelper(daoHelperIml: DaoHelperIml): DaoHelper = daoHelperIml
}