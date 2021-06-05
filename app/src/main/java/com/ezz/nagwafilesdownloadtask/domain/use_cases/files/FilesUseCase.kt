package com.ezz.nagwafilesdownloadtask.domain.use_cases.files

import com.ezz.nagwafilesdownloadtask.R
import com.ezz.nagwafilesdownloadtask.data.models.FileItem
import com.ezz.nagwafilesdownloadtask.data.models.Status
import com.ezz.nagwafilesdownloadtask.data.models.StatusCode
import com.ezz.nagwafilesdownloadtask.data.repositories.FilesRepository
import io.reactivex.Single
import javax.inject.Inject

class FilesUseCase @Inject constructor(
    private val mFilesRepository: FilesRepository
) : IFilesUseCase {
    override fun getFilesList(): Single<Status<ArrayList<FileItem>>> {
        return mFilesRepository.getFilesList()
            .map(this::mapFilesListResponse)
    }

    private fun mapFilesListResponse(filesListResponseStatus: Status<ArrayList<FileItem>>): Status<ArrayList<FileItem>> {
        return when (filesListResponseStatus.statusCode) {
            StatusCode.SUCCESS -> {
                Status.Success(filesListResponseStatus.data)
            }
            StatusCode.NO_DATA ->
                Status.NoData(errorMessage = R.string.someThingWentWrong)
        }
    }
}