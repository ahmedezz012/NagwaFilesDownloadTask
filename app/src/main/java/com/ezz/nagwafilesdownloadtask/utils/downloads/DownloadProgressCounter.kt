package com.ezz.nagwafilesdownloadtask.utils.downloads

import android.app.DownloadManager
import android.app.DownloadManager.Query
import android.content.Context
import android.database.Cursor
import io.reactivex.Observable
import io.reactivex.ObservableEmitter


class DownloadProgressCounter(
    mContext: Context, private val downloadId: Long
) {
    private val query: Query = Query()
    private var cursor: Cursor? = null
    private var lastBytesDownloadedSoFar = 0
    private var totalBytes = 0
    private val downloadManager =
        mContext.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

    init {
        query.setFilterById(downloadId)
    }

    fun start(): Observable<Pair<Int, Int>> {
        return Observable.create<Pair<Int, Int>> { emitter -> getDownloadProgress(emitter) }
    }

    private fun getDownloadProgress(emitter: ObservableEmitter<Pair<Int, Int>>) {
        while (downloadId > 0) {
            try {
                cursor = downloadManager.query(query)
                if (cursor!!.moveToFirst()) {

                    if (totalBytes <= 0) {
                        totalBytes =
                            cursor!!.getInt(cursor!!.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES))
                    }
                    val bytesDownloadedSoFar =
                        cursor!!.getInt(cursor!!.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR))
                    if (bytesDownloadedSoFar == totalBytes && totalBytes > 0) {
                        emitter.onNext(Pair(bytesDownloadedSoFar, totalBytes))
                        emitter.onComplete()
                    } else {
                        emitter.onNext(Pair(bytesDownloadedSoFar, totalBytes))
                        lastBytesDownloadedSoFar = bytesDownloadedSoFar
                    }
                }
                cursor!!.close()
            } catch (e: Exception) {
                emitter.onError(e)
            }
        }
    }


}