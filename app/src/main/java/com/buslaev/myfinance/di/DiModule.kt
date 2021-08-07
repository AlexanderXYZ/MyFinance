package com.buslaev.myfinance.di

import com.buslaev.myfinance.db.room.DaoHelper
import com.buslaev.myfinance.repository.Repository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DiModule {


    @Binds
    abstract fun bindDaoHelper(repository: Repository): DaoHelper
}