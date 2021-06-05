package com.ezz.nagwafilesdownloadtask.ui.files

import dagger.Subcomponent

@Subcomponent
interface FilesComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): FilesComponent
    }

    fun inject(filesViewModelFactory: FilesViewModelFactory)
}