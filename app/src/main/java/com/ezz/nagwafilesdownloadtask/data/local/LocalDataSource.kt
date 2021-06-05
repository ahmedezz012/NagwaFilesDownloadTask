package com.ezz.nagwafilesdownloadtask.data.local

import android.content.Context
import com.ezz.nagwafilesdownloadtask.R
import com.ezz.nagwafilesdownloadtask.utils.Utils
import com.ezz.nagwafilesdownloadtask.data.models.FileItem
import com.google.gson.Gson

class LocalDataSource (private val context: Context, private val gson: Gson) : ILocalDataSource {
    override fun getFilesList(): ArrayList<FileItem> {
        val listOfFilesInputStream =
            context.resources.openRawResource(R.raw.get_list_of_files_response)
        val listOfFilesJson: String? = Utils.inputStreamToString(listOfFilesInputStream)
        return if (!listOfFilesJson.isNullOrEmpty()) {
            ArrayList(gson.fromJson(listOfFilesJson, Array<FileItem>::class.java).toList())
        } else
            arrayListOf()
    }
}