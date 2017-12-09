package com.example.jake21x.kotlinbasic

import android.os.Bundle
import android.widget.TextView
import android.app.Activity
import android.hardware.Camera
import android.hardware.Camera.*
import android.util.Log
import kotlinx.android.synthetic.main.activity_preview.*
import org.jetbrains.anko.onClick
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import com.example.jake21x.kotlinbasic.utils.CameraView
import android.widget.ImageButton
import android.widget.FrameLayout
import android.view.Surface.ROTATION_270
import android.view.Surface.ROTATION_180
import android.view.Surface.ROTATION_90
import android.view.Surface.ROTATION_0
import android.content.Intent
import android.view.*
import android.widget.Toast
import android.view.Window.FEATURE_NO_TITLE


class PreviewActivity : Activity(), SurfaceHolder.Callback, Camera.ShutterCallback, Camera.PictureCallback {

   
    companion object {

        var mCamera: Camera?=null;
        var mPreview: SurfaceView?=null;
        var filePath: String?="";
        internal var currentCameraId = 0
        
    }
    
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState) 
        setContentView(R.layout.activity_preview)
        mPreview = findViewById<SurfaceView>(R.id.camera_view)
        mPreview!!.holder.addCallback(this)
        mPreview!!.holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS)
        mCamera  = Camera.open(currentCameraId)
    }

    public override fun onPause() {
        super.onPause()
        mCamera!!.stopPreview()
    }

    public override fun onDestroy() {
        super.onDestroy()
        mCamera!!.release()
        Log.d("CAMERA", "Destroy")
    }

    fun close(v:View){
        mCamera!!.release()
        mCamera!!.stopPreview()
        finish()
    }

    fun onCancelClick(v:View) {

        mCamera!!.stopPreview()
        mCamera!!.release()
        if (currentCameraId == Camera.CameraInfo.CAMERA_FACING_BACK) {
            currentCameraId = Camera.CameraInfo.CAMERA_FACING_FRONT
        } else {
            currentCameraId = Camera.CameraInfo.CAMERA_FACING_BACK
        }
        mCamera = Camera.open(currentCameraId)
        val info = Camera.CameraInfo()
        Camera.getCameraInfo(Camera.CameraInfo.CAMERA_FACING_FRONT, info)
        val rotation = this.windowManager.defaultDisplay.rotation
        var degrees = 0
        when (rotation) {
            Surface.ROTATION_0 -> degrees = 0
            Surface.ROTATION_90 -> degrees = 90
            Surface.ROTATION_180 -> degrees = 180
            Surface.ROTATION_270 -> degrees = 270
        }//Natural orientation
        //Landscape left
        //Upside down
        //Landscape right
        val rotate = (info.orientation - degrees + 360) % 360

        //STEP #2: Set the 'rotation' parameter
        val params = mCamera!!.parameters
        params.setRotation(rotate)
        try {
            mCamera!!.setPreviewDisplay(mPreview!!.holder)
        } catch (e: IOException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        }

        mCamera!!.parameters = params
        mCamera!!.setDisplayOrientation(90)
        mCamera!!.startPreview()
    }

    fun onSnapClick(v: View) {
        mCamera!!.takePicture(this, null, null, this)
    }

    override fun onShutter() {
        Toast.makeText(this, "Click!", Toast.LENGTH_SHORT).show()
    }

    override fun onPictureTaken(data: ByteArray, camera: Camera) {
        //Here, we chose internal storage
        var fos: FileOutputStream? = null
        try {
            filePath = "/sdcard/test.jpg"
            fos = FileOutputStream(
                    filePath)
            fos.write(data)
            fos.close()
            //Log.d("Log", "onPictureTaken - wrote bytes: " + data.length);
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            Log.d("Log", "onPictureTaken - wrote bytes: " + data.size)
        } catch (e: IOException) {
            e.printStackTrace()
            Log.d("Log", "onPictureTaken - wrote bytes: " + data.size)
        } finally {
            val i = intent
            i.putExtra("Path", filePath)
            setResult(Activity.RESULT_OK, i)
            finish()
        }
        camera.startPreview()
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        val info = Camera.CameraInfo()
        Camera.getCameraInfo(Camera.CameraInfo.CAMERA_FACING_BACK, info)
        val rotation = this.windowManager.defaultDisplay.rotation
        var degrees = 0
        when (rotation) {
            Surface.ROTATION_0 -> degrees = 0
            Surface.ROTATION_90 -> degrees = 90
            Surface.ROTATION_180 -> degrees = 180
            Surface.ROTATION_270 -> degrees = 270
        }//Natural orientation
        //Landscape left
        //Upside down
        //Landscape right
        val rotate = (info.orientation - degrees + 360) % 360

        //STEP #2: Set the 'rotation' parameter
        val params = mCamera!!.parameters
        params.setRotation(rotate)
        mCamera!!.parameters = params
        mCamera!!.setDisplayOrientation(90)
        mCamera!!.startPreview()
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        try {
            mCamera!!.setPreviewDisplay(mPreview!!.holder)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        Log.i("PREVIEW", "surfaceDestroyed")
    }
}