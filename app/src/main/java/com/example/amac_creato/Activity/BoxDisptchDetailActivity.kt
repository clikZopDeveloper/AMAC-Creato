package com.example.amac_creato.Activity

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.amac_creato.Adapter.*
import com.example.amac_creato.ApiHelper.ApiController
import com.example.amac_creato.ApiHelper.ApiResponseListner
import com.example.amac_creato.Model.BoxDetailBean
import com.example.amac_creato.Model.CustOrderDetailBean
import com.example.amac_creato.Model.SlotPackerDetailBean
import com.example.amac_creato.R
import com.example.amac_creato.Utills.*
import com.example.amac_creato.databinding.ActivityBoxDetailBinding

import com.example.amac_creato.databinding.ActivityCustOrderDetailBinding

import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.gson.JsonElement
import com.stpl.antimatter.Utils.ApiContants

class BoxDisptchDetailActivity : AppCompatActivity(), ApiResponseListner,
    GoogleApiClient.OnConnectionFailedListener,
    ConnectivityListener.ConnectivityReceiverListener {
    private lateinit var binding: ActivityBoxDetailBinding
    private lateinit var apiClient: ApiController
    lateinit var mAdapterHardware: HardwareBoxDisProdAdapter
    lateinit var mAdapterProduction: ProductionProdDisBoxAdapter
    var myReceiver: ConnectivityListener? = null
    var activity: Activity = this
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_box_detail)
        if (SalesApp.isEnableScreenshort == true) {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE
            );
        }
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        myReceiver = ConnectivityListener()

        binding.igToolbar.tvTitle.text = "Box Dispatch Detail"
        binding.igToolbar.ivMenu.setImageDrawable(resources.getDrawable(R.drawable.ic_back_black))
        binding.igToolbar.ivMenu.setOnClickListener { finish() }

        intent.getStringExtra("box_id")?.let { apiBoxDetail(it) }
        binding.btnSubmit.visibility = View.GONE
    }

    fun apiBoxDetail(id: String) {
        SalesApp.isAddAccessToken = true
        apiClient = ApiController(this, this)
        val params = Utility.getParmMap()
        params["box_id"] = id
        apiClient.progressView.showLoader()
        apiClient.getApiPostCall(ApiContants.GetBoxesDetail, params)
    }

    override fun success(tag: String?, jsonElement: JsonElement?) {
        try {
            apiClient.progressView.hideLoader()
            if (tag == ApiContants.GetBoxesDetail) {
                val boxDetailBean = apiClient.getConvertIntoModel<BoxDetailBean>(
                    jsonElement.toString(),
                    BoxDetailBean::class.java
                )
                //   Toast.makeText(this, allStatusBean.msg, Toast.LENGTH_SHORT).show()
                if (boxDetailBean.error == false) {
                    handleHardwareList(boxDetailBean.data.hardwareProducts)
                    handleProductList(boxDetailBean.data.productionProducts)
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

    fun handleHardwareList(
        data: List<BoxDetailBean.Data.HardwareProduct>
    ) {
        binding.rcHardware.layoutManager = LinearLayoutManager(this)
        mAdapterHardware = HardwareBoxDisProdAdapter(this, data, object :
            RvListClickListner {
            override fun clickPos(status: MutableList<Any?>?, id: Int) {
            }
        })
        binding.rcHardware.adapter = mAdapterHardware
        // rvMyAcFiled.isNestedScrollingEnabled = false
        binding.rcHardware.isNestedScrollingEnabled = false
        mAdapterHardware.notifyDataSetChanged()


    }

    fun handleProductList(
        data: List<BoxDetailBean.Data.ProductionProduct>
    ) {
        binding.rcProduction.layoutManager = LinearLayoutManager(this)
        mAdapterProduction = ProductionProdDisBoxAdapter(this, data, object :
            RvListClickListner {
            override fun clickPos(status: MutableList<Any?>?, id: Int) {

            }
        })
        binding.rcProduction.adapter = mAdapterProduction
        // rvMyAcFiled.isNestedScrollingEnabled = false
        binding.rcProduction.isNestedScrollingEnabled = false
        mAdapterProduction.notifyDataSetChanged()

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
        //    startService(Intent(this, LocationService::class.java))
    }

}
