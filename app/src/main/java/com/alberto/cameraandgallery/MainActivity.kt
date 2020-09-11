package com.alberto.cameraandgallery

import android.Manifest.permission.CAMERA
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.alberto.cameraandgallery.AndroidPermissions.Companion.REQUEST_CODE_CAMERA
import kotlinx.android.synthetic.main.activity_main.*
import java.io.ByteArrayOutputStream

class MainActivity : AppCompatActivity() {

    companion object{

        private const val IMAGE_CAPTURE_CODE = 1001
    }

    private var imageUri: Uri? = null

    private lateinit var bitmapPicture: Bitmap


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnCP.setOnClickListener {

        }
        btnOpenCamera.setOnClickListener {
            setUpPermissions()
        }
    }

    private fun setUpPermissions() {

        if(shouldShowRequestPermissionRationale(CAMERA)){
            AndroidPermissions.showConfigurationPermissions(applicationContext, packageName)
        } else {
            if(askPermissions(this, CAMERA)){
                openCamera()
            }
        }
    }

    private fun askPermissions(activity: Activity, permissionToCheck: String): Boolean {

        var hasPermission = false
        if(ContextCompat.checkSelfPermission(activity, permissionToCheck) != PackageManager.PERMISSION_GRANTED){

            Log.i("logAz", "Permission $permissionToCheck denied")
            ActivityCompat.requestPermissions(activity, arrayOf(CAMERA, WRITE_EXTERNAL_STORAGE),
                    REQUEST_CODE_CAMERA
            )
        } else {
            hasPermission = true
            Log.i("logAz", "Permission granted")
        }
        return hasPermission
    }

    private fun openCamera() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.DISPLAY_NAME, getPictureTitle())
        values.put(MediaStore.Images.Media.DESCRIPTION, getPictureDescription())
        imageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        //camera intent
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
        startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE)
    }

    private fun getPictureDescription(): String {
        return "From the Camera"
    }

    private fun getPictureTitle(): String {
        return "New Picture"
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {

        when (requestCode) {
            REQUEST_CODE_CAMERA -> {

                if(grantResults.size ==2 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    Log.i("logAz", "Permission has been granted by user")
                    openCamera()
                } else {
                    Log.i("logAz", "Permission has been denied by user")
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun getCapturedImage(selectedPhotoUri: Uri): Bitmap {
        return  when {
            Build.VERSION.SDK_INT < 28 -> MediaStore.Images.Media.getBitmap(
                this.contentResolver,
                selectedPhotoUri
            )
            else -> {
                val source = ImageDecoder.createSource(this.contentResolver, selectedPhotoUri)
                ImageDecoder.decodeBitmap(source)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            IMAGE_CAPTURE_CODE -> {

                if (resultCode == Activity.RESULT_OK) {
                    bitmapPicture = imageUri?.let { getCapturedImage(it) }!!
                    imgView.setImageBitmap(bitmapPicture)
                    Log.i("logAz", "Compressed image: ${compressImage(bitmapPicture)}")
                    Log.i("logAz", getAllShownImagesPath(imageUri!!).size.toString())
                }
            }
        }
    }
    private fun getAllShownImagesPath(uriExternal: Uri): MutableList<Uri> {
        val cursor: Cursor?
        val columnIndexID: Int
        val listOfAllImages: MutableList<Uri> = mutableListOf()
        val projection = arrayOf(MediaStore.Images.Media._ID)
        var imageId: Long
        cursor = contentResolver.query(uriExternal, projection, null, null, null)
        if (cursor != null) {
            columnIndexID = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            while (cursor.moveToNext()) {
                imageId = cursor.getLong(columnIndexID)
                val uriImage = Uri.withAppendedPath(uriExternal, "" + imageId)
                listOfAllImages.add(uriImage)
                Log.i("logAz", "Path: ${uriImage.path}")
            }
            cursor.close()
        }
        return listOfAllImages
    }





    private fun reduceQuality(bitmap: Bitmap): Bitmap? {
        val newWidth = bitmap.width / 4
        //        Toast.makeText(this, "width: " +bitmap.getWidth()+"\nnewWidth: "+ newWidth, Toast.LENGTH_LONG).show();
        val newHeight = bitmap.height / 4
        return Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, false)
    }


    private fun compressImage(image: Bitmap): String {
        val byteArrayOS = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOS) //bm = Bitmap
        val b: ByteArray = byteArrayOS.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT)
    }
}