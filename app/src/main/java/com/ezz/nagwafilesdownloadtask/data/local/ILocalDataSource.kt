package com.ezz.nagwafilesdownloadtask.data.local

import com.ezz.nagwafilesdownloadtask.data.models.FileItem
import java.util.*

interface ILocalDataSource {
    fun getFilesList(): ArrayList<FileItem>
}