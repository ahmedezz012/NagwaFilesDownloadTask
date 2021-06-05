package com.ezz.nagwafilesdownloadtask.ui.files.adapter

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.ezz.nagwafilesdownloadtask.R
import com.ezz.nagwafilesdownloadtask.data.models.DownloadStatus
import com.ezz.nagwafilesdownloadtask.data.models.FileItem
import com.ezz.nagwafilesdownloadtask.databinding.ItemFileBinding
import com.ezz.nagwafilesdownloadtask.utils.Constants.DownloadsConstants.NUMBER_OF_TRIES

class FilesViewHolder(
    var binding: ItemFileBinding,
    val mContext: Context,
    val downloadClickListener: DownloadClickListener
) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(fileItem: FileItem) {
        bindFileName(fileItem.name)
        bindFileStatus(fileItem.status, fileItem.numberOfTries, fileItem.downloadProgress)
        bindDownloadButtonClickListener(fileItem)
    }

    private fun bindDownloadButtonClickListener(fileItem: FileItem) {
        binding.btnDownload.setOnClickListener {
            downloadClickListener.onDownloadClick(adapterPosition, fileItem)
        }
    }

    private fun bindFileName(name: String?) {
        binding.txtFileName.text = name ?: mContext.getString(R.string.fileName)
    }

    private fun bindFileStatus(status: DownloadStatus, numberOfTries: Int, downloadProgress: Int) {
        when (status) {
            DownloadStatus.FINISHED -> {
                bindFinishedFileStatus()
            }
            DownloadStatus.ERROR -> {
                bindErrorFileStatus(numberOfTries)
            }
            DownloadStatus.DOWNLOADING -> {
                bindDownloadingFileStatus(downloadProgress)
            }
            else -> {
                bindIdleFileStatus()
            }
        }
    }

    private fun bindErrorFileStatus(numberOfTries: Int) {
        binding.txtStatus.visibility = View.VISIBLE
        if (numberOfTries >= NUMBER_OF_TRIES) {
            binding.txtStatus.text =
                mContext.getString(R.string.downloadFailedYouCantDownload)
            binding.btnDownload.visibility = View.GONE
        } else {
            binding.btnDownload.visibility = View.VISIBLE
            binding.txtStatus.text = mContext.getString(R.string.downloadFailedYouCanRetry)
        }
    }

    private fun bindIdleFileStatus() {
        binding.txtStatus.visibility = View.GONE
        binding.btnDownload.visibility = View.VISIBLE
    }

    private fun bindDownloadingFileStatus(downloadProgress: Int) {
        binding.txtStatus.visibility = View.VISIBLE
        binding.btnDownload.visibility = View.GONE
        binding.txtStatus.text =
            String.format(
                mContext.getString(R.string.downloading),
                downloadProgress.toString()
            )
    }

    private fun bindFinishedFileStatus() {
        binding.txtStatus.visibility = View.VISIBLE
        binding.txtStatus.text = mContext.getString(R.string.finished)
        binding.btnDownload.visibility = View.GONE
    }
}