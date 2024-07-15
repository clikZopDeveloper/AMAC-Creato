package com.example.amac_creato.Activity

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.amac_creato.Adapter.CustOrderDetailAdapter
import com.example.amac_creato.Adapter.CustOrderListAdapter
import com.example.amac_creato.Adapter.CustomerInterstedAdapter
import com.example.amac_creato.ApiHelper.ApiController
import com.example.amac_creato.ApiHelper.ApiResponseListner
import com.example.amac_creato.Model.BarcodeDetailBean
import com.example.amac_creato.Model.CustOrderDetailBean
import com.example.amac_creato.Model.CustOrderListBean
import com.example.amac_creato.Model.CustomerDetailBean
import com.example.amac_creato.R
import com.example.amac_creato.Utills.*

import com.example.amac_creato.databinding.ActivityCustOrderDetailBinding

import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.JsonElement
import com.stpl.antimatter.Utils.ApiContants

class CustOrderDetailActivity : AppCompatActivity(), ApiResponseListner,
    GoogleApiClient.OnConnectionFailedListener,
    ConnectivityListener.ConnectivityReceiverListener {
    private lateinit var binding: ActivityCustOrderDetailBinding
    private lateinit var apiClient: ApiController
    var myReceiver: ConnectivityListener? = null
    var activity: Activity = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_cust_order_detail)
        if (SalesApp.isEnableScreenshort==true){
            window.setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        myReceiver = ConnectivityListener()

        binding.igToolbar.tvTitle.text = "Customer Order Detail"
        binding.igToolbar.ivMenu.setImageDrawable(resources.getDrawable(R.drawable.ic_back_black))
        binding.igToolbar.ivMenu.setOnClickListener { finish() }

        intent.getStringExtra("id")?.let { apiCustomerDetail(it) }

    }

    fun apiBarcode(scannedValue: String, orderProductID: String) {
        SalesApp.isAddAccessToken = true
        val params = Utility.getParmMap()
        params["id"] = orderProductID.toString()
        params["barcode"] =scannedValue.toString()
        apiClient.progressView.showLoader()
        apiClient.getApiPostCall(ApiContants.getBarcode, params)
    }

    fun apiCustomerDetail(id: String) {
        SalesApp.isAddAccessToken = true
        apiClient = ApiController(this, this)
        val params = Utility.getParmMap()
        params["id"] = id
        apiClient.progressView.showLoader()
        apiClient.getApiPostCall(ApiContants.getCustomerOrderDetail, params)
    }

    override fun success(tag: String?, jsonElement: JsonElement?) {
        try {
            apiClient.progressView.hideLoader()
            if (tag == ApiContants.getCustomerOrderDetail) {
                val custOrderDetailBean = apiClient.getConvertIntoModel<CustOrderDetailBean>(
                    jsonElement.toString(),
                    CustOrderDetailBean::class.java
                )
                //   Toast.makeText(this, allStatusBean.msg, Toast.LENGTH_SHORT).show()
                if (custOrderDetailBean.error==false) {
                    handleCustOrderList(custOrderDetailBean.data)
                }
            }

            if (tag == ApiContants.getBarcode) {
                val custOrderListBean = apiClient.getConvertIntoModel<BarcodeDetailBean>(
                    jsonElement.toString(),
                    BarcodeDetailBean::class.java
                )

                if (custOrderListBean.error == false) {
                    apiCustomerDetail(custOrderListBean.orderId)
                    //Toast.makeText(this, custOrderListBean.msg, Toast.LENGTH_SHORT).show()
                    // apiTeamContactList(intent.getStringExtra("customer_id")!!)
                }

            }
        }catch (e:Exception){
            Log.d("error>>",e.localizedMessage)
        }
    }

    fun handleCustOrderList(data: List<CustOrderDetailBean.Data>) {
        binding.rcTeamContactList.layoutManager = LinearLayoutManager(this)
       val mAllAdapter = CustOrderDetailAdapter(this, data, object :
            RvStatusClickListner {
            override fun clickPos(status: String, id: Int) {
                //  apiCustomerDetail(id)
                val intent = Intent(this@CustOrderDetailActivity, BarcodeScanActivity::class.java)
                intent.putExtra("orderProductID",id.toString())
                startActivityForResult(intent, 101)
            }
        })
        binding.rcTeamContactList.adapter = mAllAdapter
        // rvMyAcFiled.isNestedScrollingEnabled = false
        binding.rcTeamContactList.isNestedScrollingEnabled = false
        mAllAdapter.notifyDataSetChanged()



    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 101 && resultCode == RESULT_OK) {
            Bundle()
            val bundle = data!!.extras
            if (bundle != null && bundle.containsKey("barCodeValue")) {
                val barCodeValue = bundle.getString("barCodeValue")
                val orderProductID = bundle.getString("orderProductID")
                Log.d("sfsdf",barCodeValue.toString())
                Log.d("sfsdf",orderProductID.toString())

                apiBarcode(barCodeValue.toString(), orderProductID!!)

            }
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
    //    startService(Intent(this, LocationService::class.java))
    }

}
