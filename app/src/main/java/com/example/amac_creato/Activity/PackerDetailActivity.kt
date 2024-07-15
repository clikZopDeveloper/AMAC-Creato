package com.example.amac_creato.Activity

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.amac_creato.Adapter.HardwareProdListAdapter
import com.example.amac_creato.Adapter.ProductionProdListAdapter
import com.example.amac_creato.ApiHelper.ApiController
import com.example.amac_creato.ApiHelper.ApiResponseListner
import com.example.amac_creato.Model.BarcodepckerDetailBean
import com.example.amac_creato.Model.CustOrderListBean
import com.example.amac_creato.Model.MultipleProductBean
import com.example.amac_creato.Model.SlotPackerDetailBean
import com.example.amac_creato.R
import com.example.amac_creato.Utills.*
import com.example.amac_creato.databinding.ActivityPackerDetailBinding

import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.stpl.antimatter.Utils.ApiContants

class PackerDetailActivity : AppCompatActivity(), ApiResponseListner,
    GoogleApiClient.OnConnectionFailedListener,
    ConnectivityListener.ConnectivityReceiverListener {
    lateinit var mAdapterHardware: HardwareProdListAdapter
    lateinit var mAdapterProduction: ProductionProdListAdapter
    private lateinit var binding: ActivityPackerDetailBinding
    private lateinit var apiClient: ApiController
    var myReceiver: ConnectivityListener? = null
    var activity: Activity = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_packer_detail)
        if (SalesApp.isEnableScreenshort == true) {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE
            );
        }

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        myReceiver = ConnectivityListener()

        binding.igToolbar.tvTitle.text = "Packer Detail"
        binding.igToolbar.ivMenu.setImageDrawable(resources.getDrawable(R.drawable.ic_back_black))
        binding.igToolbar.ivMenu.setOnClickListener { finish() }

        intent.getStringExtra("slot_id")?.let { apiSlotPackerDetail(it) }
        //  orderID= intent.getStringExtra("project_id").toString()

        binding.btnSubmit.setOnClickListener {
            dialogRemark()
        }

        binding.fbAddArchitect.setOnClickListener {
            startActivity(Intent(this@PackerDetailActivity, BoxListActivity::class.java).putExtra("slotID",intent.getStringExtra("slot_id")))
        }

    }

    fun apiSlotPackerDetail(id: String) {
        SalesApp.isAddAccessToken = true
        apiClient = ApiController(this, this)
        val params = Utility.getParmMap()
        params["slot_id"] = id.toString()
        apiClient.progressView.showLoader()
        apiClient.getApiPostCall(ApiContants.GetSlotPackerDetail, params)
    }

    override fun success(tag: String?, jsonElement: JsonElement?) {
        try {
            apiClient.progressView.hideLoader()

            if (tag == ApiContants.GetSlotPackerDetail) {
                val slotPackerDetailBean = apiClient.getConvertIntoModel<SlotPackerDetailBean>(
                    jsonElement.toString(),
                    SlotPackerDetailBean::class.java
                )

                if (slotPackerDetailBean.error == false) {

                    handleHardwareList(slotPackerDetailBean.data.hardwareProducts)
                    handleProductList(slotPackerDetailBean.data.productionProducts)

                } else {
                    Toast.makeText(this, slotPackerDetailBean.msg, Toast.LENGTH_SHORT).show()
                }
            }

            if (tag == ApiContants.getBarcodePacker) {
                val barcodepckerDetailBean = apiClient.getConvertIntoModel<BarcodepckerDetailBean>(
                    jsonElement.toString(),
                    BarcodepckerDetailBean::class.java
                )

                if (barcodepckerDetailBean.error == false) {
                    Toast.makeText(this, barcodepckerDetailBean.msg, Toast.LENGTH_SHORT).show()
                   //  apiTeamContactList(intent.getStringExtra("customer_id")!!)
                    apiSlotPackerDetail(barcodepckerDetailBean.data.slotId.toString())
                }

            }

            if (tag == ApiContants.getCreateBox) {
                val custOrderListBean = apiClient.getConvertIntoModel<CustOrderListBean>(
                    jsonElement.toString(),
                    CustOrderListBean::class.java
                )

                if (custOrderListBean.error == false) {
                    Toast.makeText(this, custOrderListBean.msg, Toast.LENGTH_SHORT).show()
                    // apiTeamContactList(intent.getStringExtra("customer_id")!!)
                    finish()
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

    fun apiBarcode(scannedValue: String, orderProductID: String, variant: String) {
        SalesApp.isAddAccessToken = true
        val params = Utility.getParmMap()
        params["slot_id"] = orderProductID.toString()
        params["barcode"] = scannedValue.toString()
        params["variant"] = variant.toString()
        apiClient.progressView.showLoader()
        apiClient.getApiPostCall(ApiContants.getBarcodePacker, params)
    }

    fun apiCreateBox(
        hardwareList: MutableList<MultipleProductBean>,
        productionList: MutableList<MultipleProductBean>,
        remark: String
    ) {
        Log.d("scsc", Gson().toJson(hardwareList))
        Log.d("scsc", Gson().toJson(productionList))
        SalesApp.isAddAccessToken = true
        val params = Utility.getParmMap()
        params["hardware_product_list"] = Gson().toJson(hardwareList)
        params["production_product_list"] = Gson().toJson(productionList)
        params["remarks"] = remark.toString()
        params["slot_id"] = intent.getStringExtra("slot_id")!!
        apiClient.progressView.showLoader()
        apiClient.getApiPostCall(ApiContants.getCreateBox, params)

    }

    fun handleHardwareList(
        data: List<SlotPackerDetailBean.Data.HardwareProduct>
    ) {
        binding.rcHardware.layoutManager = LinearLayoutManager(this)
        mAdapterHardware = HardwareProdListAdapter(this, data, object :
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
        data: List<SlotPackerDetailBean.Data.ProductionProduct>
    ) {
        binding.rcProduction.layoutManager = LinearLayoutManager(this)
        mAdapterProduction = ProductionProdListAdapter(this, data, object :
            RvListClickListner {
            override fun clickPos(status: MutableList<Any?>?, id: Int) {

            }
        })
        binding.rcProduction.adapter = mAdapterProduction
        // rvMyAcFiled.isNestedScrollingEnabled = false
        binding.rcProduction.isNestedScrollingEnabled = false
        mAdapterProduction.notifyDataSetChanged()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 101 && resultCode == RESULT_OK) {
            Bundle()
            val bundle = data!!.extras
            if (bundle != null && bundle.containsKey("barCodeValue")) {
                val barCodeValue = bundle.getString("barCodeValue")
                val orderProductID = bundle.getString("orderProductID")
                val variantData = bundle.getString("variantValue")
                Log.d("sfsdf", barCodeValue.toString())
                Log.d("sfsdf", orderProductID.toString())

                apiBarcode(barCodeValue.toString(), orderProductID!!, variantData!!)

            }
        }
    }

    fun dialogRemark() {
        val builder = AlertDialog.Builder(this, R.style.CustomAlertDialog)
            .create()
        val dialog = layoutInflater.inflate(R.layout.dialog_days_reamrk, null)
        builder.setView(dialog)

        builder.setCanceledOnTouchOutside(false)
        builder.show()

        /*    val dialog: Dialog = GeneralUtilities.openBootmSheetDailog(
                R.layout.dialog_update_client, R.style.AppBottomSheetDialogTheme,
                this
            )*/

        val ivClose = dialog.findViewById<ImageView>(R.id.ivClose)
        val editReamrk =
            dialog.findViewById<TextInputEditText>(R.id.editReamrk) as TextInputEditText
        val btnSubmit = dialog.findViewById<TextView>(R.id.btnSubmit) as TextView

        ivClose.setOnClickListener {
            builder.dismiss()

        }

        btnSubmit.setOnClickListener {
            builder.dismiss()
            if (editReamrk.text.toString().isNullOrEmpty()) {
                Toast.makeText(this, "Enter Remark", Toast.LENGTH_SHORT).show()
            } else {
                apiCreateBox(
                    mAdapterHardware.data,
                    mAdapterProduction.data,
                    editReamrk.text.toString()
                )

            }
        }

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
