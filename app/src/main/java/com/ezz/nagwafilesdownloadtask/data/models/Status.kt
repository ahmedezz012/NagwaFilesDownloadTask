package com.ezz.nagwafilesdownloadtask.data.models

sealed class Status<T>(
    val statusCode: StatusCode = StatusCode.SUCCESS,
    val data: T? = null,
    val errorMessage: Int? = null
) {

    class Success<T>(data: T? = null, errorMessage: Int? = null) :
        Status<T>(StatusCode.SUCCESS, data, errorMessage)


    class NoData<T>(data: T? = null, errorMessage: Int? = null) :
        Status<T>(StatusCode.NO_DATA, data, errorMessage)

    class CopyStatus<T, R>(status: Status<T>, newData: R?) :
        Status<R>(status.statusCode, newData, status.errorMessage)


    fun isSuccess(): Boolean {
        return statusCode == StatusCode.SUCCESS
    }

    fun isNoData(): Boolean {
        return statusCode == StatusCode.NO_DATA
    }

}
