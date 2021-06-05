package com.ezz.nagwafilesdownloadtask.data.models


import com.google.gson.annotations.SerializedName


data class FileItem(
    @SerializedName("id")
    val id: Int? = null,
    @SerializedName("type")
    val type: String? = null,
    @SerializedName("url")
    val url: String? = null,
    @SerializedName("name")
    val name: String? = null,
    var downloadProgress: Int = 0,
    var numberOfTries: Int = 0,
    var status: DownloadStatus = DownloadStatus.IDLE
) {
    fun increaseNumberOfTries() {
        numberOfTries += 1
    }
}