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
import com.example.amac_creato.Adapter.BoxesDispatchListAdapter
import com.example.amac_creato.Adapter.BoxesListAdapter
import com.example.amac_creato.Adapter.CustOrderListAdapter
import com.example.amac_creato.ApiHelper.ApiController
import com.example.amac_creato.ApiHelper.ApiResponseListner
import com.example.amac_creato.Model.BoxListBean
import com.example.amac_creato.Model.CustOrderListBean
import com.example.amac_creato.R
import com.example.amac_creato.Utills.*
import com.example.amac_creato.databinding.ActivityCustomerListBinding
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.gson.JsonElement
import com.stpl.antimatter.Utils.ApiContants

class BoxDispatchListActivity : AppCompatActivity(), ApiResponseListner,
    GoogleApiClient.OnConnectionFailedListener,
    ConnectivityListener.ConnectivityReceiverListener {
    private lateinit var binding: ActivityCustomerListBinding
    private lateinit var apiClient: ApiController
    var myReceiver: ConnectivityListener? = null

    var activity: Activity = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_customer_list)
        if (SalesApp.isEnableScreenshort == true) {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE
            )
        }

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        myReceiver = ConnectivityListener()

        apiClient = ApiController(this, this)
        binding.igToolbar.ivMenu.setImageDrawable(resources.getDrawable(R.drawable.ic_back_black))
        binding.igToolbar.ivMenu.setOnClickListener { finish() }
        binding.igToolbar.tvTitle.text = "All Boxes"
        intent.getStringExtra("slotID")?.let { apiAllBoxesList(it) }

    }

    fun apiAllBoxesList(slotIDS: String) {
        SalesApp.isAddAccessToken = true
        val params = Utility.getParmMap()
        apiClient.progressView.showLoader()
          params["slot_id"]=slotIDS
        apiClient.getApiPostCall(ApiContants.GetBoxes, params)

    }

    override fun success(tag: String?, jsonElement: JsonElement?) {
        try {
            apiClient.progressView.hideLoader()
            if (tag == ApiContants.GetBoxes) {
                val boxListBean = apiClient.getConvertIntoModel<BoxListBean>(
                    jsonElement.toString(),
                    BoxListBean::class.java
                )
                if (boxListBean.error == false) {
                    handleBoxesList(boxListBean.data)
                }
            }
            if (tag == ApiContants.getBarcodeBoxes) {
                val custOrderListBean = apiClient.getConvertIntoModel<CustOrderListBean>(
                    jsonElement.toString(),
                    CustOrderListBean::class.java
                )

                if (custOrderListBean.error == false) {
                    Toast.makeText(this, custOrderListBean.msg, Toast.LENGTH_SHORT).show()
                    // apiTeamContactList(intent.getStringExtra("customer_id")!!)
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

    fun handleBoxesList(data: List<BoxListBean.Data>) {
        binding.rcTeamContactList.layoutManager = LinearLayoutManager(this)
        var mAdapter = BoxesDispatchListAdapter(this, data
        ) { status, workstatus, amt, id ->
            startActivity(
                Intent(
                    this,
                    BoxDisptchDetailActivity::class.java
                ).putExtra("box_id", id.toString())
            )
        }
        binding.rcTeamContactList.adapter = mAdapter
        // rvMyAcFiled.isNestedScrollingEnabled = false

    }

    fun dialog(status: String, ids: Int) {
        val builder = AlertDialog.Builder(this@BoxDispatchListActivity)
        builder.setMessage("Are you sure you want to start service?")
            .setCancelable(false)
            .setPositiveButton("Yes") { dialog, id ->
                // Delete selected note from database

                //     apiAccept(status,ids)
            }
            .setNegativeButton("No") { dialog, id ->
                // Dismiss the dialog
                dialog.dismiss()
            }
        val alert = builder.create()
        alert.show()

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

    fun apiBarcode(scannedValue: String, orderProductID: String) {
        SalesApp.isAddAccessToken = true
        val params = Utility.getParmMap()
        params["slot_id"] = orderProductID.toString()
        params["barcode"] = scannedValue.toString()
        apiClient.progressView.showLoader()
        apiClient.getApiPostCall(ApiContants.getBarcodeBoxes, params)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 101 && resultCode == RESULT_OK) {
            Bundle()
            val bundle = data!!.extras
            if (bundle != null && bundle.containsKey("barCodeValue")) {
                val barCodeValue = bundle.getString("barCodeValue")
                val orderProductID = bundle.getString("orderProductID")
            //    val variantData = bundle.getString("variantValue")
                Log.d("sfsdf", barCodeValue.toString())
                Log.d("sfsdf", orderProductID.toString())

                apiBarcode(barCodeValue.toString(), orderProductID!!)

            }
        }
    }

}
