package com.alberto.cameraandgallery

import android.Manifest.permission.CAMERA
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.alberto.cameraandgallery.AndroidPermissions.Companion.REQUEST_CODE_CAMERA
import com.alberto.cameraandgallery.AndroidPermissions.Companion.showConfigurationPermissions
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnCP.setOnClickListener {
            setUpPermissions()
        }
        btnOpenCamera.setOnClickListener {
            openCamera()
        }
    }

    private fun setUpPermissions() {

        if(shouldShowRequestPermissionRationale(CAMERA)){
            showConfigurationPermissions(applicationContext, packageName)
        } else {
            askPermissions(this, CAMERA)
        }
    }

    private fun askPermissions(activity: Activity, permissionToCheck: String) {

        if(ContextCompat.checkSelfPermission(activity, permissionToCheck) != PackageManager.PERMISSION_GRANTED){


            Log.i("logAz", "Permission $permissionToCheck denied")
            ActivityCompat.requestPermissions(activity, arrayOf(CAMERA, WRITE_EXTERNAL_STORAGE),
                    REQUEST_CODE_CAMERA
            )
        } else {
            Log.i("logAz", "Permission granted")
        }
    }
    private val IMAGE_CAPTURE_CODE = 1001
    private var imageUri: Uri? = null
    private fun openCamera() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "New Picture")
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera")
        imageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        //camera intent
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
        startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK){
            //set image captured to image view
            imgView.setImageURI(imageUri)
        }
    }
}