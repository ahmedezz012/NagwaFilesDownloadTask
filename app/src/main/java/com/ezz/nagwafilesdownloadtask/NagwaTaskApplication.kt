package com.ezz.nagwafilesdownloadtask

import android.app.Application
import com.ezz.nagwafilesdownloadtask.di.AppComponent
import com.ezz.nagwafilesdownloadtask.di.DaggerAppComponent

class NagwaTaskApplication : Application() {

    lateinit var appComponent: AppComponent
        private set

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.factory()
            .create(this)

    }
}