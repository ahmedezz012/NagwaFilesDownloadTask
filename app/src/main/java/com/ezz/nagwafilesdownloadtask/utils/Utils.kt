package com.ezz.nagwafilesdownloadtask.utils

import android.content.Context
import android.content.DialogInterface
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.ezz.nagwafilesdownloadtask.utils.Constants.DownloadsConstants.MP4
import com.ezz.nagwafilesdownloadtask.utils.Constants.DownloadsConstants.PDF
import java.io.IOException
import java.io.InputStream


object Utils {

    fun inputStreamToString(inputStream: InputStream): String? {
        return try {
            val bytes = ByteArray(inputStream.available())
            inputStream.read(bytes, 0, bytes.size)
            String(bytes)
        } catch (e: IOException) {
            null
        }
    }

    fun getMimeType(url: String?): String {
        var type: String? = ""
        val extension = MimeTypeMap.getFileExtensionFromUrl(url)
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
        }
        return type!!
    }

    fun getExtensionFromUri(url: String): String {
        if (url.endsWith(MP4))
            return MP4
        else
            return PDF
    }

    fun showMessage(mContext: Context, message: Int) {
        Toast.makeText(mContext, message, Toast.LENGTH_LONG).show()
    }
    fun showBasicDialog(
        context: Context, title: String? = null, message: String? = null,
        positiveButton: String, negativeButton: String? = null,
        positiveClickListener: DialogInterface.OnClickListener,
        negativeClickListener: DialogInterface.OnClickListener? = null
    ): AlertDialog {
        return AlertDialog.Builder(context)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(positiveButton, positiveClickListener)
            .setNegativeButton(negativeButton, negativeClickListener)
            .show()
    }
}