package com.ezz.nagwafilesdownloadtask.utils.downloads

import android.app.DownloadManager
import android.content.Context
import android.content.Context.DOWNLOAD_SERVICE
import android.net.Uri
import android.os.Environment
import com.ezz.nagwafilesdownloadtask.utils.Constants
import com.ezz.nagwafilesdownloadtask.utils.Constants.DownloadsConstants.NO_ID

object FileDownloader {

    fun downloadFile(
        context: Context, fileUrl: String, fileName: String, mimeType: String
    ): Long {
        try {
            val request = DownloadManager.Request(Uri.parse(fileUrl))
                .setTitle(fileName)
                .setMimeType(mimeType)
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setDestinationInExternalPublicDir(
                    Environment.DIRECTORY_DOWNLOADS,
                    "${Constants.DownloadsConstants.FOLDER_NAME}/$fileName"
                )
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)
            val downloadManager = context.getSystemService(DOWNLOAD_SERVICE) as DownloadManager
            val downloadID = downloadManager.enqueue(request)
            return downloadID
        } catch (exc: Exception) {
            return NO_ID
        }
    }
}