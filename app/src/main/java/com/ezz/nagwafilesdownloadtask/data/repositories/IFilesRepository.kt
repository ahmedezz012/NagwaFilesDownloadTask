package com.ezz.nagwafilesdownloadtask.data.repositories

import com.ezz.nagwafilesdownloadtask.data.models.FileItem
import com.ezz.nagwafilesdownloadtask.data.models.Status
import io.reactivex.Single

interface IFilesRepository {
    fun getFilesList(): Single<Status<ArrayList<FileItem>>>
}