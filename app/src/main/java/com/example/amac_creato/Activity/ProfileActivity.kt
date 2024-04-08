package com.example.amac_creato.Activity

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.amac_creato.ApiHelper.ApiController
import com.example.amac_creato.ApiHelper.ApiResponseListner
import com.example.amac_creato.Model.BaseResponseBean
import com.example.amac_creato.Model.ProfileBean
import com.example.amac_creato.R
import com.example.amac_creato.Utills.*

import com.example.amac_creato.databinding.ActivityProfileBinding

import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.stpl.antimatter.Utils.ApiContants
import java.io.Serializable

class ProfileActivity : AppCompatActivity(), ApiResponseListner,
    GoogleApiClient.OnConnectionFailedListener,
    ConnectivityListener.ConnectivityReceiverListener {
    private lateinit var binding: ActivityProfileBinding
    var myReceiver: ConnectivityListener? = null
    var activity: Activity = this
    private lateinit var apiClient: ApiController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile)

        if (SalesApp.isEnableScreenshort==true){
            window.setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        myReceiver = ConnectivityListener()

        binding.igToolbar.tvTitle.text = "My Profile"
        binding.igToolbar.ivMenu.setImageDrawable(resources.getDrawable(R.drawable.ic_back_black))
        binding.igToolbar.ivMenu.setOnClickListener { finish() }
        apiClient = ApiController(this, this)
        apiProfile()

//val profile=intent.getSerializableExtra("profileData") as ProfileBean.Data
    //    Log.d("sdfsd",Gson().toJson(profile))
        // intent.getStringExtra("Status")?.let { apiAllCompaints(it) }

    }

    override fun success(tag: String?, jsonElement: JsonElement?) {
        try {
            apiClient.progressView.hideLoader()

            if (tag == ApiContants.getProfile) {
                val profileBean = apiClient.getConvertIntoModel<ProfileBean>(
                    jsonElement.toString(),
                    ProfileBean::class.java
                )
                //   Toast.makeText(this, allStatusBean.msg, Toast.LENGTH_SHORT).show()

                if (profileBean.error == false) {
                    handleProfileData(profileBean.data)
                }
            }

        } catch (e: Exception) {
            Log.d("error>>", e.localizedMessage)
        }

    }

    override fun failure(tag: String?, errorMessage: String) {
        apiClient.progressView.hideLoader()
        Utility.showSnackBar(this, errorMessage)
    }

    private fun handleProfileData(profile: ProfileBean.Data) {
        binding.editName.setText(profile.name)
        binding.editMobNo.setText(profile.mobile)
        binding.editEmailID.setText(profile.userType)

      /*binding.editName.setText(PrefManager.getString(ApiContants.userName,""))
        binding.editMobNo.setText(PrefManager.getString(ApiContants.mobileNumber,""))
        binding.editEmailID.setText(PrefManager.getString(ApiContants.EmailAddress,"")) */
    }

    fun apiProfile() {
        SalesApp.isAddAccessToken = true
        val params = Utility.getParmMap()
        apiClient.progressView.showLoader()
        apiClient.getApiPostCall(ApiContants.getProfile, params)
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onPause() {
        super.onPause()
        GeneralUtilities.unregisterBroadCastReceiver(this, myReceiver)
    }

    override fun onResume() {
        GeneralUtilities.registerBroadCastReceiver(this, myReceiver)
        SalesApp.setConnectivityListener(this)
        super.onResume()
    }

    override fun onNetworkConnectionChange(isconnected: Boolean) {
        ApiContants.isconnectedtonetwork = isconnected
        GeneralUtilities.internetConnectivityAction(this, isconnected)
    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {}

    override fun onDestroy() {
        super.onDestroy()
        // Start the LocationService when the app is closed
      //  startService(Intent(this, LocationService::class.java))
    }
}
