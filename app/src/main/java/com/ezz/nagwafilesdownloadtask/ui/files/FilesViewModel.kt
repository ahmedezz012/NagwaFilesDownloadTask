package com.ezz.nagwafilesdownloadtask.ui.files

import android.content.Context
import androidx.lifecycle.ViewModel
import com.ezz.nagwafilesdownloadtask.R
import com.ezz.nagwafilesdownloadtask.data.models.DownloadFileData
import com.ezz.nagwafilesdownloadtask.data.models.DownloadStatus
import com.ezz.nagwafilesdownloadtask.data.models.FileItem
import com.ezz.nagwafilesdownloadtask.data.models.Status
import com.ezz.nagwafilesdownloadtask.utils.ConnectionUtils
import com.ezz.nagwafilesdownloadtask.domain.use_cases.files.IFilesUseCase
import com.ezz.nagwafilesdownloadtask.utils.Constants.DownloadsConstants.NO_ID
import com.ezz.nagwafilesdownloadtask.utils.Constants.DownloadsConstants.ONE_HUNDRED
import com.ezz.nagwafilesdownloadtask.utils.downloads.FileDownloader
import com.ezz.nagwafilesdownloadtask.utils.Utils
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

class FilesViewModel(val mIFilesUseCase: IFilesUseCase, val connectionUtils: ConnectionUtils) :
    ViewModel() {

    var mIndex: Int = NO_ID.toInt()
    lateinit var mFileItem: FileItem
    val showToastObservable: PublishSubject<Int> = PublishSubject.create()
    var mFilesStatus: Status<ArrayList<FileItem>>? = null
    private var mFilesListObservable =
        BehaviorSubject.create<Status<ArrayList<FileItem>>>()

    val mUpdateItemObservable: PublishSubject<Pair<Int, FileItem>> =
        PublishSubject.create()

    internal fun getFilesList(): Observable<Status<ArrayList<FileItem>>> {
        return if (mFilesStatus == null) {
            mFilesListObservable.mergeWith(getFilesListCompletable())
        } else {
            updateFilesList(mFilesStatus!!)
            mFilesListObservable.hide()
        }
    }

    private fun getFilesListCompletable(): Completable {
        return mIFilesUseCase.getFilesList()
            .map(this::mapFilesResponse)
            .doOnError { throwable -> onGetFilesError(throwable) }
            .onErrorReturn { onGetFilesErrorReturn(it) }
            .ignoreElement()
    }

    private fun mapFilesResponse(filesStatus: Status<ArrayList<FileItem>>) {
        val returnedFilesStatus: Status<ArrayList<FileItem>> =
            when (filesStatus) {
                is Status.Success -> {
                    onFilesListStatusSuccess(filesStatus)
                }
                else -> {
                    Status.CopyStatus(filesStatus, null)
                }
            }

        if (returnedFilesStatus.isSuccess()) {
            updateFilesList(returnedFilesStatus)
        } else {
            showToastMessage(returnedFilesStatus.errorMessage!!)
        }
    }

    private fun onFilesListStatusSuccess(filesStatus: Status.Success<java.util.ArrayList<FileItem>>): Status<java.util.ArrayList<FileItem>> {
        setFilesListStatus(filesStatus)
        return filesStatus
    }


    private fun updateFilesList(status: Status<ArrayList<FileItem>>) {
        mFilesListObservable.onNext(status)
    }

    private fun setFilesListStatus(status: Status<ArrayList<FileItem>>) {
        mFilesStatus = status
    }

    private fun onGetFilesError(throwable: Throwable) {
        throwable.printStackTrace()
    }

    private fun onGetFilesErrorReturn(throwable: Throwable) {
        setFilesListStatus(Status.NoData(errorMessage = R.string.someThingWentWrong))
        updateFilesList(mFilesStatus!!)
        throwable.printStackTrace()
    }

    private fun showToastMessage(message: Int) {
        showToastObservable.onNext(message)
    }

    fun onDownloadClick(
        index: Int,
        fileItem: FileItem
    ) {
        mIndex = index
        mFileItem = fileItem
    }

    private fun changeDownloadStatus(downLoadStatus: DownloadStatus) {
        mFilesStatus?.data?.get(mIndex)?.status = downLoadStatus
        updateFilesList(mFilesStatus!!)
    }


    fun onDownloadClick(
        context: Context
    ): Single<DownloadFileData> {
        var downLoadStatus: DownloadStatus
        if (!connectionUtils.isConnected) {
            showToastMessage(R.string.no_network)
            downLoadStatus = DownloadStatus.NO_NETWORK
            changeDownloadStatus(downLoadStatus)
            return Single.just(DownloadFileData(downLoadStatus, NO_ID, mIndex))
        } else {
            val downloadId = FileDownloader.downloadFile(
                context,
                mFileItem.url!!,
                mFileItem.name!! + "." + Utils.getExtensionFromUri(mFileItem.url!!),
                Utils.getMimeType(mFileItem.url!!)
            )
            if (downloadId == NO_ID) {
                showToastMessage(R.string.someThingWentWrong)
                downLoadStatus = DownloadStatus.ERROR
                mFilesStatus?.data?.get(mIndex)?.increaseNumberOfTries()
                changeDownloadStatus(downLoadStatus)
                return Single.just(DownloadFileData(downLoadStatus, NO_ID, mIndex))
            } else {
                downLoadStatus = DownloadStatus.DOWNLOADING
                return Single.just(DownloadFileData(downLoadStatus, downloadId, mIndex))
            }
        }
    }

    fun updateDownloadProgress(progress: Int, total: Int, index: Int) {
        var downloadProgress =
            ((progress * ONE_HUNDRED).toDouble() / total.toDouble())
        if (downloadProgress < 0)
            downloadProgress = 0.0
        mFilesStatus?.data?.get(index)?.downloadProgress =
            downloadProgress.toInt()
        mFilesStatus?.data?.get(index)?.status = DownloadStatus.DOWNLOADING
        if (downloadProgress >= ONE_HUNDRED)
            mFilesStatus?.data?.get(index)?.status = DownloadStatus.FINISHED
        mUpdateItemObservable.onNext(Pair(index, mFilesStatus?.data?.get(index)!!))
    }

}