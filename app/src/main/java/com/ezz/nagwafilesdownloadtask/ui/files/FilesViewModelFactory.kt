package com.ezz.nagwafilesdownloadtask.ui.files

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ezz.nagwafilesdownloadtask.NagwaTaskApplication
import com.ezz.nagwafilesdownloadtask.domain.use_cases.files.FilesUseCase
import com.ezz.nagwafilesdownloadtask.utils.ConnectionUtils
import javax.inject.Inject

class FilesViewModelFactory(val context: Context) : ViewModelProvider.Factory {


    @Inject
    lateinit var mFilesUseCase: FilesUseCase

    @Inject
    lateinit var mConnectionUtils: ConnectionUtils

    init {
        injectDependencies()
    }

    private fun injectDependencies() {
        (context.applicationContext as NagwaTaskApplication).appComponent.fileComponentComponent()
            .create().inject(this)
    }


    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return FilesViewModel(mFilesUseCase, mConnectionUtils) as T
    }
}