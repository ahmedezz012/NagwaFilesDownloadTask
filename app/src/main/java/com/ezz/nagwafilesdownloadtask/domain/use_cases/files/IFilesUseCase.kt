package com.ezz.nagwafilesdownloadtask.domain.use_cases.files

import com.ezz.nagwafilesdownloadtask.data.models.FileItem
import com.ezz.nagwafilesdownloadtask.data.models.Status
import io.reactivex.Single

interface IFilesUseCase {
    fun getFilesList(): Single<Status<ArrayList<FileItem>>>
}