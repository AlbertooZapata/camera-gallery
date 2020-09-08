package com.alberto.cameraandgallery

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.content.ContextCompat.checkSelfPermission

class AndroidPermissions {

    companion object {

        const val REQUEST_CODE_CAMERA = 101


        fun askPermissions(activity: Activity, permissionToCheck: String) {

            if(checkSelfPermission(activity, permissionToCheck) != PackageManager.PERMISSION_GRANTED){

                Log.i("logAz", "Permission $permissionToCheck denied")
                requestPermissions(activity, arrayOf(Manifest.permission.CAMERA),
                    REQUEST_CODE_CAMERA
                )
            } else {
                Log.i("logAz", "Permission granted")
            }
        }

        fun showConfigurationPermissions(context: Context, packageName: String) {
            Toast.makeText(context, "Debe habilitar los permisos necesarios.", Toast.LENGTH_SHORT)
                .show()
            val i = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            i.addCategory(Intent.CATEGORY_DEFAULT)
            i.data = Uri.parse("package:$packageName")
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
            i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
            context.startActivity(i)
        }

    }
}