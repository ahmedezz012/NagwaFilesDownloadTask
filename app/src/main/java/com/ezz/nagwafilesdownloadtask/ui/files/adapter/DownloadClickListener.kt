package com.ezz.nagwafilesdownloadtask.ui.files.adapter

import com.ezz.nagwafilesdownloadtask.data.models.FileItem


interface DownloadClickListener {
    fun onDownloadClick(index: Int, fileItem: FileItem)
}