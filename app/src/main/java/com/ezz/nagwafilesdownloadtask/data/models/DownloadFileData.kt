package com.ezz.nagwafilesdownloadtask.data.models

data class DownloadFileData(
    var downloadStatus: DownloadStatus, var downloadId: Long,
    var index: Int
)
