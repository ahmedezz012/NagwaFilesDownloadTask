package com.ezz.nagwafilesdownloadtask.di

import android.content.Context
import com.ezz.nagwafilesdownloadtask.ui.files.FilesComponent
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [AppModule::class, SubComponentsModule::class]
)
interface AppComponent {

    @Component.Factory
    interface Factory {

        fun create(@BindsInstance applicationContext: Context): AppComponent
    }

    fun fileComponentComponent(): FilesComponent.Factory

}