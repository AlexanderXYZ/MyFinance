package com.buslaev.myfinance.di

import android.content.Context
import androidx.room.Room
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.buslaev.myfinance.adapters.AccountsAdapter
import com.buslaev.myfinance.adapters.CategoriesAdapter
import com.buslaev.myfinance.adapters.IconsAdapter
import com.buslaev.myfinance.adapters.MainAdapter
import com.buslaev.myfinance.db.room.DaoHelper
import com.buslaev.myfinance.db.room.FinanceDao
import com.buslaev.myfinance.db.room.FinanceDatabase
import com.buslaev.myfinance.db.room.MIGRATE_3_4
import com.buslaev.myfinance.other.Constants.DATABASE_NAME
import com.buslaev.myfinance.repository.Repository
import com.buslaev.myfinance.sharedPreferences.AccountSharedPreferences
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
    ) = Room.databaseBuilder(context, FinanceDatabase::class.java, DATABASE_NAME)
        .addMigrations(MIGRATE_3_4)
        .build()

    @Provides
    @Singleton
    fun provideDao(database: FinanceDatabase): FinanceDao = database.getDao()

    @Provides
    @Singleton
    fun provideRepository(
        dao: FinanceDao
    ) = Repository(dao) as DaoHelper

    @Provides
    @Singleton
    fun provideGlide(@ApplicationContext context: Context): RequestManager = Glide.with(context)

    @Provides
    @Singleton
    fun provideMainAdapter(glide: RequestManager): MainAdapter = MainAdapter(glide)

    @Provides
    @Singleton
    fun provideCategoriesAdapter(glide: RequestManager): CategoriesAdapter =
        CategoriesAdapter(glide)

    @Provides
    @Singleton
    fun provideIconsViewPagerAdapter(glide: RequestManager): IconsAdapter =
        IconsAdapter(glide)

    @Provides
    @Singleton
    fun provideAccountsAdapter(glide: RequestManager): AccountsAdapter =
        AccountsAdapter(glide)

    @Provides
    @Singleton
    fun provideAccountSharedPreferences(@ApplicationContext context: Context) =
        AccountSharedPreferences(context)
}