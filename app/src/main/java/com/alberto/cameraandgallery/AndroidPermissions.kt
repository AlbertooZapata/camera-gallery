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


//        fun askPermissions(activity: Activity, permissionToCheck: String) {
//
//            var allPermissionAreOK = true
//            if(checkSelfPermission(activity, permissionToCheck) != PackageManager.PERMISSION_GRANTED){
//
//
//                Log.i("logAz", "Permission $permissionToCheck denied")
//                requestPermissions(activity, arrayOf(Manifest.permission.CAMERA),
//                    REQUEST_CODE_CAMERA
//                )
//            } else {
//                Log.i("logAz", "Permission granted")
//            }
//        }
//
        fun showConfigurationPermissions(context: Context, packageName: String) {
            Toast.makeText(context, "You must enable the permissions.", Toast.LENGTH_SHORT)
                .show()
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            intent.addCategory(Intent.CATEGORY_DEFAULT)
            intent.data = Uri.parse("package:$packageName")
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
            intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
            context.startActivity(intent)
        }
    }
}