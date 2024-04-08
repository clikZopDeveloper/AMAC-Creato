package com.example.amac_creato.Activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Notification
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.IBinder
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import com.example.amac_creato.ApiHelper.ApiController
import com.example.amac_creato.ApiHelper.ApiResponseListner
import com.example.amac_creato.Utills.SalesApp
import com.example.amac_creato.Utills.Utility
import com.google.android.gms.location.*
import com.google.gson.JsonElement
import com.stpl.antimatter.Utils.ApiContants

class LocationService : Service() , ApiResponseListner {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var apiClient: ApiController
    var location: Location?=null
    override fun onCreate() {
        super.onCreate()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
       // startForeground(1, createNotification())

        requestLocationUpdates()

        return START_STICKY
    }

    private fun requestLocationUpdates() {
        val locationRequest = LocationRequest.create().apply {
            interval = 3 * 60 * 1000 // 15 minutes
          //  interval = 1 * 60 * 1000 // 2 minutes

            fastestInterval = 3 * 60 * 1000 // 5 minutes
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        }

        if (ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    super.onLocationResult(locationResult)
                     location = locationResult.lastLocation
                    Log.d("LocationService", "Latitude: ${location?.latitude}, Longitude: ${location?.longitude}")
                //    apiUpdateLoction()
                }
            },
            null
        )
    }

    @SuppressLint("SuspiciousIndentation")
    override fun success(tag: String?, jsonElement: JsonElement?) {
        try {
        //    apiClient.progressView.hideLoader()


        }catch (e:Exception){
            Log.d("error>>",e.localizedMessage)
        }
    }

    override fun failure(tag: String?, errorMessage: String) {
        apiClient.progressView.hideLoader()
        Utility.showSnackBar(context = Activity(), errorMessage)
    }

    private fun createNotification(): Notification {
        // Create a notification for the foreground service
        return NotificationCompat.Builder(this, "channelId")
            .setContentTitle("Location Service")
            .setContentText("Updating location in the background")
            .setSmallIcon(com.example.amac_creato.R.drawable.install_icon)
            .build()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}