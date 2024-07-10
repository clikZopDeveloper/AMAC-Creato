package com.example.amac_creato.Activity


import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.amac_creato.Adapter.CustOrderListAdapter
import com.example.amac_creato.ApiHelper.ApiController
import com.example.amac_creato.ApiHelper.ApiResponseListner
import com.example.amac_creato.Model.CustOrderDetailBean
import com.example.amac_creato.Model.CustOrderListBean
import com.example.amac_creato.R
import com.example.amac_creato.Utills.*
import com.example.amac_creato.databinding.ActivityCustOrderListBinding
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.gson.JsonElement
import com.stpl.antimatter.Utils.ApiContants

class CustOrderListActivity : AppCompatActivity(), ApiResponseListner,
    GoogleApiClient.OnConnectionFailedListener,
    ConnectivityListener.ConnectivityReceiverListener {
    private lateinit var binding: ActivityCustOrderListBinding
    private lateinit var apiClient: ApiController
    var myReceiver: ConnectivityListener? = null
    private lateinit var mAllAdapter: CustOrderListAdapter
    var activity: Activity = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_cust_order_list)
        if (SalesApp.isEnableScreenshort == true) {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE
            )
        }

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        myReceiver = ConnectivityListener()

        binding.igToolbar.ivMenu.setImageDrawable(resources.getDrawable(R.drawable.ic_back_black))
        binding.igToolbar.ivMenu.setOnClickListener { finish() }
        binding.igToolbar.tvTitle.text = "All Customer Orders"

      //  intent.getStringExtra("customer_id")?.let { apiCustOrderList(it) }

    }

    fun apiCustOrderList(custID: String) {
        SalesApp.isAddAccessToken = true
        apiClient = ApiController(this, this)
        val params = Utility.getParmMap()
      //  params["customer_id"] = custID
        apiClient.progressView.showLoader()
        apiClient.getApiPostCall(ApiContants.getCustomerOrders, params)

    }

    fun handleCustOrderList(data: List<CustOrderListBean.Data>) {
        binding.rcTeamContactList.layoutManager = LinearLayoutManager(this)
        mAllAdapter = CustOrderListAdapter(this, data, object :
            RvStatusClickListner {
            override fun clickPos(status: String, id: Int) {
              //  apiCustomerDetail(id)
                startActivity(Intent(this@CustOrderListActivity,CustOrderDetailActivity::class.java)
                    .putExtra("id",id.toString()))

            }
        })
        binding.rcTeamContactList.adapter = mAllAdapter
        // rvMyAcFiled.isNestedScrollingEnabled = false
        binding.rcTeamContactList.isNestedScrollingEnabled = false
        mAllAdapter.notifyDataSetChanged()

        binding.edSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                if (data != null) {
                    mAllAdapter.filter.filter(s)
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                mAllAdapter.filter.filter(s)
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                mAllAdapter.filter.filter(s)
                /* if (s.toString().trim { it <= ' ' }.length < 1) {
                     ivClear.visibility = View.GONE
                 } else {
                     ivClear.visibility = View.GONE
                 }*/
            }
        })

    }

    fun apiCustomerDetail(id: Int) {
        SalesApp.isAddAccessToken = true
        apiClient = ApiController(this, this)
        val params = Utility.getParmMap()
        params["id"] = id.toString()
        apiClient.progressView.showLoader()
        apiClient.getApiPostCall(ApiContants.getCustomerOrderDetail, params)

    }

    fun apiBarcode(scannedValue: String, orderProductID: String) {
        SalesApp.isAddAccessToken = true
        val params = Utility.getParmMap()
        params["id"] = orderProductID.toString()
        params["barcode"] =scannedValue.toString()
        apiClient.progressView.showLoader()
        apiClient.getApiPostCall(ApiContants.getBarcode, params)

    }

    override fun success(tag: String?, jsonElement: JsonElement?) {
        try {
            apiClient.progressView.hideLoader()
            if (tag == ApiContants.getCustomerOrders) {
                val custOrderListBean = apiClient.getConvertIntoModel<CustOrderListBean>(
                    jsonElement.toString(),
                    CustOrderListBean::class.java
                )
                //   Toast.makeText(this, allStatusBean.msg, Toast.LENGTH_SHORT).show()
                if (custOrderListBean.error == false) {
                    handleCustOrderList(custOrderListBean.data)
                }
            }

            if (tag == ApiContants.getCustomerOrderDetail) {
                val custOrderDetailBean = apiClient.getConvertIntoModel<CustOrderDetailBean>(
                    jsonElement.toString(),
                    CustOrderDetailBean::class.java
                )
                //   Toast.makeText(this, allStatusBean.msg, Toast.LENGTH_SHORT).show()
                if (custOrderDetailBean.error == false) {
               //     dialogOrderDetail(custOrderDetailBean.data)

                }
            }

           /* if (tag == ApiContants.getBarcode) {
                val custOrderListBean = apiClient.getConvertIntoModel<CustOrderListBean>(
                    jsonElement.toString(),
                    CustOrderListBean::class.java
                )

                if (custOrderListBean.error == false) {
                    Toast.makeText(this, custOrderListBean.msg, Toast.LENGTH_SHORT).show()
                   // apiTeamContactList(intent.getStringExtra("customer_id")!!)
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

    fun dialogOrderDetail(data: List<CustOrderDetailBean.Data>) {
/*        val builder = AlertDialog.Builder(this,R.style.CustomAlertDialog)
            .create()
        val dialog = layoutInflater.inflate(R.layout.dialog_order_detail,null)

        builder.setView(dialog)

        builder.setCanceledOnTouchOutside(false)
        builder.show()*/

        val dialog: Dialog = GeneralUtilities.openBootmSheetDailog(
            R.layout.dialog_order_detail, R.style.AppBottomSheetDialogTheme,
            this
        )
        val ivClose = dialog.findViewById<ImageView>(R.id.ivClose)
        val ivScanner = dialog.findViewById<ImageView>(R.id.ivScanner)
        val ivVerifyed = dialog.findViewById<ImageView>(R.id.ivVerifyed)
        val tvName = dialog.findViewById<TextView>(R.id.tvName)
        val tvQty = dialog.findViewById<TextView>(R.id.tvQty)
        val tvBarcode = dialog.findViewById<TextView>(R.id.tvBarcode)
        val tvDate = dialog.findViewById<TextView>(R.id.tvDate)

        tvName.setText(data.get(0).name)
        tvQty.setText("QTY : " + data.get(0).qty)
        tvDate.setText(data.get(0).createdAt?.toString())

        ivClose.setOnClickListener { dialog.dismiss() }
        if (data.get(0).isProduction==1){
            ivScanner.visibility=View.GONE
            ivVerifyed.visibility=View.VISIBLE
        }else{
            ivScanner.visibility=View.VISIBLE
            ivVerifyed.visibility=View.GONE
        }

        ivScanner.setOnClickListener {
            dialog.dismiss()
            val intent = Intent(this, BarcodeScanActivity::class.java)
            intent.putExtra("orderProductID",data.get(0).id.toString())
            startActivityForResult(intent, 101)

        }
        tvBarcode.setText(data.get(0).productionBarcode?.toString())
    }

    fun dialog(status: String, ids: Int) {
        val builder = AlertDialog.Builder(this@CustOrderListActivity)
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
        apiCustOrderList("")
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

}
