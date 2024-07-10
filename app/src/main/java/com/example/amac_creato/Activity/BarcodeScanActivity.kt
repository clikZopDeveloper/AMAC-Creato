package com.example.amac_creato.Activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.SurfaceHolder
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.amac_creato.ApiHelper.ApiController
import com.example.amac_creato.ApiHelper.ApiResponseListner
import com.example.amac_creato.Model.CustOrderListBean
import com.example.amac_creato.R
import com.example.amac_creato.Utills.SalesApp
import com.example.amac_creato.Utills.Utility
import com.example.amac_creato.databinding.ActivityBarcodeScanBinding
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import com.google.gson.JsonElement
import com.stpl.antimatter.Utils.ApiContants
import java.io.IOException

class BarcodeScanActivity : AppCompatActivity() {
    private val requestCodeCameraPermission = 1001
    private var requestCamera: ActivityResultLauncher<String>? = null
    private lateinit var cameraSource: CameraSource
    private lateinit var barcodeDetector: BarcodeDetector

    private var scannedValue = ""
    private var orderProductID = ""
    private var variant = ""
    private lateinit var binding: ActivityBarcodeScanBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // setContentView(R.layout.activity_barcode_scan)
        binding = ActivityBarcodeScanBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


         orderProductID= intent.getStringExtra("orderProductID").toString()
        if (intent.hasExtra("variant")){
            variant= intent.getStringExtra("variant").toString()
        }

        requestCamera = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
             //   iniBC()
                setupControls()
            } else {
                Toast.makeText(this, "Permission Not granted", Toast.LENGTH_SHORT).show()
            }
        }

        requestCamera?.launch(android.Manifest.permission.CAMERA)

        val aniSlide: Animation =
            AnimationUtils.loadAnimation(this@BarcodeScanActivity, R.anim.scanner_animation)
        binding.barcodeLine.startAnimation(aniSlide)

/*
        if (ContextCompat.checkSelfPermission(
                this@BarcodeScanActivity, android.Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            askForCameraPermission()
        } else {
            setupControls()
        }

    */

        /*    val options = BarcodeScannerOptions.Builder()
                .setBarcodeFormats(
                    com.google.mlkit.vision.barcode.common.Barcode.FORMAT_QR_CODE,
                    com.google.mlkit.vision.barcode.common.Barcode.FORMAT_AZTEC)
                .build()*/

        /*    val options = BarcodeScannerOptions.Builder()
                .setBarcodeFormats(...)
            .setZoomSuggestionOptions(
                new ZoomSuggestionOptions.Builder(zoomCallback)
                    .setMaxSupportedZoomRatio(maxSupportedZoomRatio)
                    .build()) // Optional
                .build()*/

    }

    private fun iniBC() {
        barcodeDetector = BarcodeDetector.Builder(this)
            .setBarcodeFormats(Barcode.ALL_FORMATS).build()
        cameraSource = CameraSource.Builder(this, barcodeDetector)
            .setAutoFocusEnabled(true)
            .setRequestedPreviewSize(1920, 1080)
            .build()

        binding.cameraSurfaceView.holder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceCreated(holder: SurfaceHolder) {
                try {
                    if (ActivityCompat.checkSelfPermission(
                            this@BarcodeScanActivity,
                            Manifest.permission.CAMERA
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {

                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return
                    }
                    cameraSource.start(binding.cameraSurfaceView!!.holder)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun surfaceChanged(
                holder: SurfaceHolder,
                format: Int,
                width: Int,
                height: Int
            ) {

            }

            override fun surfaceDestroyed(holder: SurfaceHolder) {
                cameraSource.stop()
            }

        })

        barcodeDetector.setProcessor(object : Detector.Processor<Barcode> {
            override fun release() {
          /*      Toast.makeText(
                    applicationContext,
                    "Barcode Scanner has been closed",
                    Toast.LENGTH_SHORT
                ).show()*/
            }

            override fun receiveDetections(detections: Detector.Detections<Barcode>) {
                val barcodes = detections.detectedItems
                if (barcodes.size() != 0) {
               val  scannedValue = barcodes.valueAt(0).displayValue
                    Log.d("dataBarCode", scannedValue)

                    val intent = Intent()
                    intent.putExtra("barCodeValue", scannedValue)
                    intent.putExtra("orderProductID", orderProductID)
                    intent.putExtra("variantValue", variant)
                    this@BarcodeScanActivity.setResult(RESULT_OK, intent)
                    this@BarcodeScanActivity.finish()
                    //Don't forget to add this line printing value or finishing activity must run on main thread
                } else {
                    Toast.makeText(this@BarcodeScanActivity, "value- else", Toast.LENGTH_SHORT)
                        .show()

                }
            }
        })
    }

    private fun setupControls() {
        barcodeDetector =
            BarcodeDetector.Builder(this).setBarcodeFormats(Barcode.ALL_FORMATS).build()

        cameraSource = CameraSource.Builder(this, barcodeDetector)
            .setRequestedPreviewSize(1920, 1080)
            .setAutoFocusEnabled(true) //you should add this feature
            .build()

        binding.cameraSurfaceView.getHolder().addCallback(object : SurfaceHolder.Callback {
            @SuppressLint("MissingPermission")
            override fun surfaceCreated(holder: SurfaceHolder) {
                try {
                    //Start preview after 1s delay
                    cameraSource.start(holder)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }

            @SuppressLint("MissingPermission")
            override fun surfaceChanged(
                holder: SurfaceHolder,
                format: Int,
                width: Int,
                height: Int
            ) {
                try {
                    cameraSource.start(holder)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }

            override fun surfaceDestroyed(holder: SurfaceHolder) {
                cameraSource.stop()
            }
        })


        barcodeDetector.setProcessor(object : Detector.Processor<Barcode> {
            override fun release() {
              /*  Toast.makeText(applicationContext, "Scanner has been closed", Toast.LENGTH_SHORT)
                    .show()*/
            }

            override fun receiveDetections(detections: Detector.Detections<Barcode>) {
                val barcodes = detections.detectedItems
                if (barcodes.size() == 1) {
                    scannedValue = barcodes.valueAt(0).rawValue

                    Log.d("dataBarCode", scannedValue)
                    //Don't forget to add this line printing value or finishing activity must run on main thread
                    runOnUiThread {
                        cameraSource.stop()
                        Toast.makeText(
                            this@BarcodeScanActivity,
                            "value- $scannedValue",
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
                    }
                } else {
                    Toast.makeText(this@BarcodeScanActivity, "value- else", Toast.LENGTH_SHORT)
                        .show()

                }
            }
        })

    }

    private fun askForCameraPermission() {
        ActivityCompat.requestPermissions(
            this@BarcodeScanActivity,
            arrayOf(android.Manifest.permission.CAMERA),
            requestCodeCameraPermission
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == requestCodeCameraPermission && grantResults.isNotEmpty()) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setupControls()
            } else {
                Toast.makeText(applicationContext, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        iniBC()
    }

    override fun onPause() {
        super.onPause()
        cameraSource!!.release();
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraSource.stop()
    }

}
