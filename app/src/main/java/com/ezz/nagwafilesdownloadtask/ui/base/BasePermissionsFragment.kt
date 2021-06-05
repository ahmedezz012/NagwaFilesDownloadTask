package com.ezz.nagwafilesdownloadtask.ui.base

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.ezz.nagwafilesdownloadtask.R
import com.ezz.nagwafilesdownloadtask.utils.Utils


abstract class BasePermissionsFragment : Fragment() {

    private var mPermissionsTag: String? = null

    companion object {

        private const val MY_PERMISSIONS_REQUEST = 1
        private const val REQUEST_APP_SETTINGS = 1000
    }

    private var notGrantedPermissions = ArrayList<String>()

    abstract fun onPermissionGranted(permission: String)

    abstract fun onNeverAskAgainChecked(permission: String)

    abstract fun onPermissionDenied(permission: String)

    protected fun checkPermissions(
        context: Context, permissionTag: String? = null, rationaleDialogeMessage: Int,
        vararg permissions: String
    ) {
        if (permissions.isEmpty())
            throw Exception("Check permission called without any permissions")
        mPermissionsTag = permissionTag
        notGrantedPermissions = ArrayList() // reset

        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(
                    context,
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                notGrantedPermissions.add(permission)
            }
        }

        if (notGrantedPermissions.isEmpty()) {
            onPermissionGranted(permissionTag ?: permissions[0])
        } else {
            if (shouldShowRationale(notGrantedPermissions)) {
                showCustomDialog(context,context.getString(rationaleDialogeMessage))
            } else {
                requestPermissions(notGrantedPermissions.toTypedArray(), MY_PERMISSIONS_REQUEST)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_APP_SETTINGS) {
            var allPermissionsGranted = true

            for (permission in notGrantedPermissions) {
                if (!isPermissionGranted(permission)) {
                    allPermissionsGranted = false
                }
            }

            when {
                allPermissionsGranted -> onPermissionGranted(
                    mPermissionsTag
                        ?: notGrantedPermissions[0]
                )
                else -> onPermissionDenied(
                    mPermissionsTag
                        ?: notGrantedPermissions[0]
                )
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    /**
     * Handle permissions requested by looping on the permissions and
     * setting two flags one for when all permissions are granted and one for when never ask again checked in any of the permissions.
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {
            MY_PERMISSIONS_REQUEST -> {
                if (grantResults.isNotEmpty()) {
                    var allPermissionsGranted = true
                    var shouldShowRationale = true

                    for (grantResult in grantResults) {
                        if (grantResult == PackageManager.PERMISSION_DENIED) {
                            allPermissionsGranted = false
                            val showRationale = shouldShowRequestPermissionRationale(
                                permissions[grantResults.indexOf(grantResult)]
                            )
                            if (!showRationale) {
                                shouldShowRationale = false
                                break
                            }
                        }
                    }

                    when {
                        !shouldShowRationale -> onNeverAskAgainChecked(
                            mPermissionsTag
                                ?: permissions[0]
                        )
                        allPermissionsGranted -> onPermissionGranted(
                            mPermissionsTag
                                ?: permissions[0]
                        )
                        else -> onPermissionDenied(
                            mPermissionsTag
                                ?: permissions[0]
                        )
                    }
                }
            }
        }
    }

    private fun isPermissionGranted(permission: String): Boolean {
        return ActivityCompat.checkSelfPermission(
            activity as Context,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun shouldShowRationale(permissions: ArrayList<String>): Boolean {
        var isNeed = false
        for (i in permissions.indices) {
            if (shouldShowRequestPermissionRationale(permissions[i])) {
                isNeed = true
                break
            }
        }
        return isNeed
    }

    private fun showCustomDialog(context: Context, message: String) {
        Utils.showBasicDialog(context, null, message,
            getString(R.string.ok), getString(R.string.cancel),
            DialogInterface.OnClickListener { _, _ ->
                requestPermissions(notGrantedPermissions.toTypedArray(), MY_PERMISSIONS_REQUEST)
            },
            DialogInterface.OnClickListener { _, _ ->
                onPermissionDenied(mPermissionsTag ?: notGrantedPermissions[0])
            })
    }

}
