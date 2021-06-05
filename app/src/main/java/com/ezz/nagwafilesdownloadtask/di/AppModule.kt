package com.ezz.nagwafilesdownloadtask.di

import android.content.Context
import com.ezz.nagwafilesdownloadtask.data.local.ILocalDataSource
import com.ezz.nagwafilesdownloadtask.data.local.LocalDataSource
import com.ezz.nagwafilesdownloadtask.utils.ConnectionUtils
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder().serializeNulls().create()
    }

    @Provides
    @Singleton
    fun provideConnectionUtils(context: Context): ConnectionUtils {
        return ConnectionUtils(context)
    }


    @Provides
    @Singleton
    fun provideLocalDataSource(context: Context, gson: Gson): ILocalDataSource {
        return LocalDataSource(context, gson)
    }
}