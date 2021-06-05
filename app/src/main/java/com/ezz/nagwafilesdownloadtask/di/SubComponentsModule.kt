package com.ezz.nagwafilesdownloadtask.di

import com.ezz.nagwafilesdownloadtask.ui.files.FilesComponent
import dagger.Module

@Module(
    subcomponents = [FilesComponent::class]
)
class SubComponentsModule
