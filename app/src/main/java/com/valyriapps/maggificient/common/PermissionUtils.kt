package com.valyriapps.maggificient.common

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.PermissionChecker
import androidx.fragment.app.Fragment

object PermissionsUtils {
    enum class Permission {
        LOCATION, CAMERA, WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE, READ_CONTACT;

        val manifestString: Array<String>
            get() {
                return when (this) {
                    LOCATION -> arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
                    CAMERA -> arrayOf(Manifest.permission.CAMERA)
                    WRITE_EXTERNAL_STORAGE -> arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    READ_EXTERNAL_STORAGE -> arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                    READ_CONTACT -> arrayOf(Manifest.permission.READ_CONTACTS)
                }
            }

        val requestCode: Int
            get() {
                return when (this) {
                    LOCATION -> 100
                    CAMERA -> 101
                    WRITE_EXTERNAL_STORAGE -> 102
                    READ_EXTERNAL_STORAGE -> 103
                    READ_CONTACT -> 104
                }
            }
    }

    enum class PermissionStatus {
        ALLOW, DENY, NEVER_ASK_AGAIN
    }

    // İlgili iznin verilmesi durumunda çalışması gereken komutları ayarlar
    fun <T : View.OnCreateContextMenuListener> isPermissionGranted(
        fragmentOrActivity: T,
        type: Permission
    ): Boolean {
        for (permission in type.manifestString) {
            when (fragmentOrActivity) {
                is Fragment -> {
                    if ((fragmentOrActivity as Fragment).context == null) {
                        return false
                    } else if (PermissionChecker.checkSelfPermission(
                            (fragmentOrActivity as Fragment).context!!,
                            permission
                        ) != PermissionChecker.PERMISSION_GRANTED
                    ) {
                        return false
                    }
                }

                is Activity -> if (PermissionChecker.checkSelfPermission(
                        fragmentOrActivity as Activity,
                        permission
                    ) != PermissionChecker.PERMISSION_GRANTED
                ) {
                    return false
                }
            }
        }

        return true
    }

    // İlgili iznin verilmesi için kullanıcıya sorgu gönderir
    fun <T : View.OnCreateContextMenuListener> requestPermission(
        fragmentOrActivity: T,
        type: Permission
    ) {
        if (fragmentOrActivity is Fragment) {
            (fragmentOrActivity as Fragment).requestPermissions(
                type.manifestString,
                type.requestCode
            )
        } else if (fragmentOrActivity is Activity) {
            ActivityCompat.requestPermissions(
                fragmentOrActivity as Activity,
                type.manifestString,
                type.requestCode
            )
        }
    }

    // Sorgunun gönderilme durumunu sorgular
    fun <T : View.OnCreateContextMenuListener> isRequestGranted(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
        fragmentOrActivity: T,
        type: Permission
    ): PermissionStatus {
        if (requestCode == type.requestCode) {
            repeat(permissions.indices.count()) {
                repeat(
                    grantResults
                        .filter { it != PackageManager.PERMISSION_GRANTED }.size
                ) {
                    when (fragmentOrActivity) {
                        is Fragment ->
                            return if ((fragmentOrActivity as Fragment).shouldShowRequestPermissionRationale(
                                    permissions[0]
                                )
                            ) {
                                PermissionStatus.DENY
                            } else {
                                PermissionStatus.NEVER_ASK_AGAIN
                            }

                        is Activity ->
                            return if (ActivityCompat.shouldShowRequestPermissionRationale(
                                    fragmentOrActivity as Activity,
                                    permissions[0]
                                )
                            ) {
                                PermissionStatus.DENY
                            } else {
                                PermissionStatus.NEVER_ASK_AGAIN
                            }
                    }
                }
            }

            return PermissionStatus.ALLOW
        }

        return PermissionStatus.DENY
    }
}