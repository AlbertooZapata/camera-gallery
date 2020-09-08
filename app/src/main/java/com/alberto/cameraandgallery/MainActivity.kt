package com.alberto.cameraandgallery

import android.Manifest
import android.Manifest.permission.CAMERA
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.alberto.cameraandgallery.AndroidPermissions.Companion.REQUEST_CODE_CAMERA
import com.alberto.cameraandgallery.AndroidPermissions.Companion.askPermissions
import com.alberto.cameraandgallery.AndroidPermissions.Companion.showConfigurationPermissions
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnCP.setOnClickListener {
            setUpPermissions()
        }
    }

    private fun setUpPermissions() {

        if(shouldShowRequestPermissionRationale(CAMERA)){
            showConfigurationPermissions(applicationContext, packageName)
        } else {
            askPermissions(this, CAMERA)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {

        when (requestCode) {
            REQUEST_CODE_CAMERA -> {

                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Log.i("logAz", "Permission has been denied by user")
                } else {
                    Log.i("logAz", "Permission has been granted by user")
                }
            }
        }
    }
}