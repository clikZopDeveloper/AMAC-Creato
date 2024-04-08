package com.example.amac_creato.Activity


import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.amac_creato.Adapter.BoxesListAdapter
import com.example.amac_creato.Adapter.CustOrderListAdapter
import com.example.amac_creato.ApiHelper.ApiController
import com.example.amac_creato.ApiHelper.ApiResponseListner
import com.example.amac_creato.Model.BaseResponseBean
import com.example.amac_creato.Model.BoxListBean
import com.example.amac_creato.Model.ProfileBean
import com.example.amac_creato.R
import com.example.amac_creato.Utills.*
import com.example.amac_creato.databinding.ActivityCustomerListBinding
import com.example.amac_creato.databinding.ActivitySettingBinding
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.gson.JsonElement
import com.stpl.antimatter.Utils.ApiContants

class SettingActivity : AppCompatActivity(), ApiResponseListner,
    GoogleApiClient.OnConnectionFailedListener,
    ConnectivityListener.ConnectivityReceiverListener {
    private lateinit var binding: ActivitySettingBinding
    private lateinit var apiClient: ApiController
    var myReceiver: ConnectivityListener? = null
    var prodileData: ProfileBean.Data? = null
    var activity: Activity = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_setting)

        if (SalesApp.isEnableScreenshort == true) {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE
            )
        }

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        myReceiver = ConnectivityListener()

        apiClient = ApiController(this, this)

        binding.appbarLayout.ivMenu.setImageDrawable(resources.getDrawable(R.drawable.ic_back_black))

        binding.appbarLayout.ivMenu.setOnClickListener {
            finish()
        }

        binding.appbarLayout.tvTitle.text = "Setting"

        binding.llChangePass.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    PasswordChnageActivity::class.java
                )
            )
        }

        binding.llContactUS.setOnClickListener {

        }

        binding.llLogout.setOnClickListener {
            apiCallLogout()
        }

        binding.llMyProfile.setOnClickListener {
            startActivity(
                Intent(this@SettingActivity, ProfileActivity::class.java)
            )
        }

    }

    fun apiCallLogout() {
        SalesApp.isAddAccessToken = true
        val params = Utility.getParmMap()
        apiClient.progressView.showLoader()
        apiClient.getApiPostCall(ApiContants.logout, params)
    }

    override fun success(tag: String?, jsonElement: JsonElement?) {
        try {
            apiClient.progressView.hideLoader()
            if (tag == ApiContants.logout) {
                val baseResponseBean = apiClient.getConvertIntoModel<BaseResponseBean>(
                    jsonElement.toString(),
                    BaseResponseBean::class.java
                )
                Toast.makeText(this, baseResponseBean.msg, Toast.LENGTH_SHORT).show()
                PrefManager.clear()
                GeneralUtilities.launchActivity(
                    this,
                    LoginActivity::class.java
                )
                finishAffinity()
            }
       /*     if (tag == ApiContants.getProfile) {
                val profileBean = apiClient.getConvertIntoModel<ProfileBean>(
                    jsonElement.toString(),
                    ProfileBean::class.java
                )
                //   Toast.makeText(this, allStatusBean.msg, Toast.LENGTH_SHORT).show()
                if (profileBean.error == false) {
                    binding.apply {
                        tvUserName.setText(profileBean.data.name)
                        tvUserType.setText(profileBean.data.userType)
                        tvMobNo.setText(profileBean.data.phone)
                        prodileData = profileBean.data

                    }
                }

            }*/

        } catch (e: Exception) {
            Log.d("error>>", e.localizedMessage)
        }

    }

    override fun failure(tag: String?, errorMessage: String) {
        apiClient.progressView.hideLoader()
        Utility.showSnackBar(this, errorMessage)
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
        //   startService(Intent(this, LocationService::class.java))
    }
}
