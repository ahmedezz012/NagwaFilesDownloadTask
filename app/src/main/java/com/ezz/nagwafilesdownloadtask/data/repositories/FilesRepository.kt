package com.ezz.nagwafilesdownloadtask.data.repositories

import com.ezz.nagwafilesdownloadtask.data.local.ILocalDataSource
import com.ezz.nagwafilesdownloadtask.data.models.FileItem
import com.ezz.nagwafilesdownloadtask.data.models.Status
import io.reactivex.Single
import javax.inject.Inject

class FilesRepository @Inject constructor(
    private val mILocalDataSource: ILocalDataSource
) : IFilesRepository {

    override fun getFilesList(): Single<Status<ArrayList<FileItem>>> {
        val filesList = mILocalDataSource.getFilesList()
        return if (filesList.isEmpty()) {
            Single.just(Status.NoData())
        } else
            Single.just(Status.Success(filesList))
    }


}