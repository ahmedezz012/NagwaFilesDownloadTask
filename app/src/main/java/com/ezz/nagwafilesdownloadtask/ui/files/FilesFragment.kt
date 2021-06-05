package com.ezz.nagwafilesdownloadtask.ui.files

import android.Manifest
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewbinding.ViewBinding
import com.ezz.nagwafilesdownloadtask.R
import com.ezz.nagwafilesdownloadtask.data.models.DownloadFileData
import com.ezz.nagwafilesdownloadtask.data.models.DownloadStatus
import com.ezz.nagwafilesdownloadtask.data.models.FileItem
import com.ezz.nagwafilesdownloadtask.data.models.Status
import com.ezz.nagwafilesdownloadtask.data.models.StatusCode
import com.ezz.nagwafilesdownloadtask.databinding.FragmentFilesBinding
import com.ezz.nagwafilesdownloadtask.ui.base.BasePermissionsFragment
import com.ezz.nagwafilesdownloadtask.ui.files.adapter.DownloadClickListener
import com.ezz.nagwafilesdownloadtask.ui.files.adapter.FilesListAdapter
import com.ezz.nagwafilesdownloadtask.utils.Constants
import com.ezz.nagwafilesdownloadtask.utils.Utils
import com.ezz.nagwafilesdownloadtask.utils.downloads.DownloadProgressCounter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.*


class FilesFragment : BasePermissionsFragment(), DownloadClickListener {
    private val mCompositeDisposables = CompositeDisposable()
    lateinit var mFilesViewModel: FilesViewModel
    lateinit var mContext: Context
    private var mViewBinding: ViewBinding? = null
    private lateinit var mFilesAdapter: FilesListAdapter


    val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentFilesBinding
        get() = FragmentFilesBinding::inflate


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mViewBinding = bindingInflater.invoke(inflater, container, false)
        return (mViewBinding as FragmentFilesBinding).root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initContext()
        initAdapter()
        initViewModel()
        bindViewModels()
    }

    private fun initAdapter() {
        mFilesAdapter = FilesListAdapter(mContext, this)
        (mViewBinding as FragmentFilesBinding).rvFiles.layoutManager = LinearLayoutManager(mContext)
        (mViewBinding as FragmentFilesBinding).rvFiles.adapter = mFilesAdapter
    }

    private fun bindViewModels() {
        addDisposable(bindFilesList())
        addDisposable(bindToastMessage())
        addDisposable(updateFileItem())
    }

    private fun updateFileItem(): Disposable {
        return mFilesViewModel.mUpdateItemObservable.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ mFilesAdapter.updateItem(it.first, it.second) }, { onError(it) })
    }

    private fun bindToastMessage(): Disposable {
        return mFilesViewModel.showToastObservable.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ Utils.showMessage(mContext, it) }, { onError(it) })
    }

    private fun bindFilesList(): Disposable {
        return mFilesViewModel.getFilesList()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::onFilesListRetrieved, this::onError)
    }

    private fun onFilesListRetrieved(filesListStatus: Status<ArrayList<FileItem>>) {
        when (filesListStatus.statusCode) {
            StatusCode.SUCCESS -> {
                onFilesListSuccess(filesListStatus)
            }
            else -> {
            }
        }
    }

    private fun onFilesListSuccess(filesListStatus: Status<ArrayList<FileItem>>) {
        mFilesAdapter.replaceItems(filesListStatus.data!!)
    }

    private fun onError(exception: Throwable) {
        exception.printStackTrace()
    }

    private fun initViewModel() {
        val filesViewModelFactory =
            FilesViewModelFactory(mContext.applicationContext)

        mFilesViewModel =
            ViewModelProvider(this, filesViewModelFactory)
                .get(FilesViewModel::class.java)
    }

    private fun initContext() {
        if (activity != null)
            mContext = this.activity as Context
    }

    private fun addDisposable(disposable: Disposable): Boolean {
        return mCompositeDisposables.add(disposable)
    }

    private fun clearDisposables() {
        mCompositeDisposables.clear()
    }

    private fun clearViewBinding() {
        mViewBinding = null
    }


    override fun onDestroyView() {
        clearDisposables()
        clearViewBinding()
        super.onDestroyView()
    }

    override fun onPermissionGranted(permission: String) {
        addDisposable(
            mFilesViewModel.onDownloadClick(mContext)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ onDownloadClickSuccess(it) }, { onError(it) })
        )
    }

    private fun onDownloadClickSuccess(downloadFileData: DownloadFileData) {
        when (downloadFileData.downloadStatus) {
            DownloadStatus.DOWNLOADING -> {
                addDisposable(DownloadProgressCounter(
                    mContext,
                    downloadFileData.downloadId
                ).start().subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        Log.d(
                            "TAG",
                            it.first.toString() + " " + it.second + " " + downloadFileData.index
                        )
                        bindUpdateDownloadProgress(it.first, it.second, downloadFileData.index)
                    }, {
                        onError(it)
                    }, {
                        bindUpdateDownloadProgress(1, 1, downloadFileData.index)
                    }))
            }
        }
    }

    private fun bindUpdateDownloadProgress(progress: Int, total: Int, index: Int) {
        mFilesViewModel.updateDownloadProgress(progress, total, index)
    }

    override fun onNeverAskAgainChecked(permission: String) {
        Utils.showMessage(mContext, R.string.openFilesPermissionFromSettings)
    }

    override fun onPermissionDenied(permission: String) {
        Utils.showMessage(mContext, R.string.acceptFilesPermission)
    }


    override fun onDownloadClick(index: Int, fileItem: FileItem) {
        mFilesViewModel.onDownloadClick(index, fileItem)
        checkPermissions(
            mContext,
            Constants.PermissionTags.WRITE_EXTERNAL_STORAGE_TAG,
            R.string.acceptFilesPermission,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    }

}