package com.example.ugd9_d_0713

import android.annotation.SuppressLint
import android.hardware.Camera
import android.hardware.Camera.CameraInfo.*
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import android.content.Context
import java.io.IOException

lateinit var sensorStatusTV: TextView

lateinit var proximitySensor : Sensor

lateinit var sensorManager : SensorManager


class MainActivity : AppCompatActivity() {
    private var mCamera: Camera? = null
    private var mCameraView: CameraView? = null
    private var mCameraDepan: Int = CAMERA_FACING_BACK
    private val mCameraBelakang: Int = CAMERA_FACING_BACK

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY)
        if (proximitySensor == null) {
            Toast.makeText(this, "No proximity sensor found in device..", Toast.LENGTH_SHORT)
                .show()
            finish()
        } else {
            sensorManager.registerListener(
                proximitySensorEventListener,
                proximitySensor,
                SensorManager.SENSOR_DELAY_NORMAL
            )

        }
        try {
            mCamera = Camera.open(mCameraDepan)
        } catch (e: IOException) {
            Log.d("Error", "Failed to Get Camera" + e.message)
        }
        if (mCamera != null) {
            mCameraView = CameraView(this@MainActivity, mCamera!!)
            val camera_view = findViewById<View>(R.id.FLCamera) as FrameLayout
            camera_view.addView(mCameraView)
        }
        @SuppressLint("MissingInflatedId", "LocalSuppress") val imageClose =
            findViewById<View>(R.id.imgClose) as ImageButton
        imageClose.setOnClickListener { view: View? ->
            System.exit(0)

        }
        if (proximitySensor == null) {
            Toast.makeText(this, "No proximity sensor found in device..", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            sensorManager.registerListener(
                proximitySensorEventListener,
                proximitySensor,
                SensorManager.SENSOR_DELAY_NORMAL
            )

        }
    }

    var proximitySensorEventListener : SensorEventListener? = object : SensorEventListener{

        override fun onAccuracyChanged(sensor: Sensor, accuracy:Int) {

        }

        override fun onSensorChanged(event: SensorEvent) {
            if (event.sensor.type == Sensor.TYPE_PROXIMITY) {
                if (event.values[0] == 0f) {
                    if (mCameraDepan == mCameraBelakang) {
                        mCamera?.stopPreview()
                    }
//NB: if you don't release the current camera before switching, you app will crash
                    mCamera?.release();

//swap the id of the camera to be used
                    if(mCameraDepan == Camera.CameraInfo.CAMERA_FACING_BACK){
                        mCameraDepan = Camera.CameraInfo.CAMERA_FACING_FRONT
                    }
                    else {
                        mCameraDepan = Camera.CameraInfo.CAMERA_FACING_BACK
                    }
                    try {
                        mCamera = Camera.open(mCameraDepan)
                    }catch (e: IOException){
                        Log.d("Error", "Failed to get Camera" + e.message)
                    }


                    if (mCamera != null) {
                        mCameraView = CameraView(this@MainActivity, mCamera!!)
                        val camera_view = findViewById<View>(R.id.FLCamera) as FrameLayout
                        camera_view.addView(mCameraView)
                    }
                    @SuppressLint("MissingInflatedId", "LocalSuppress") val imageClose =
                        findViewById<View>(R.id.imgClose) as ImageButton
                    imageClose.setOnClickListener { view: View? ->
                        System.exit(0)

                    }
                }

            } else {

            }
        }
    }
}
