package com.example.amac_creato.Fragment

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.amac_creato.Activity.*
import com.example.amac_creato.ApiHelper.ApiController
import com.example.amac_creato.ApiHelper.ApiResponseListner
import com.example.amac_creato.Model.*
import com.example.amac_creato.R
import com.example.amac_creato.Utills.*
import com.example.amac_creato.databinding.FragmentHomeBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.stpl.antimatter.Utils.ApiContants
import java.util.*

class HomeFragment : Fragment(), ApiResponseListner, View.OnClickListener {
    private var areaReport = ""
    private lateinit var apiClient: ApiController
    private var _binding: FragmentHomeBinding? = null
    private var currentLoc: String? = null
    private val permissionId = 2
    var list: List<Address>? = null
    private val binding get() = _binding!!
    lateinit var shopActivity: DashboardActivity
    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    override fun onAttach(context: Context) {
        super.onAttach(context)
        shopActivity = activity as DashboardActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        // getLocation()

        if (PrefManager.getString(ApiContants.UserType, "").equals("packer")) {
            binding.llPackedSection.visibility = View.VISIBLE
            binding.llDispactedSection.visibility = View.GONE
        } else {
            binding.llDispactedSection.visibility = View.VISIBLE
            binding.llPackedSection.visibility = View.GONE
        }

        //   apiAllGet()

      //  setupFabButtons()

        binding.tvUserName.setText("Hello, " + PrefManager.getString(ApiContants.userName, ""))

         binding.fbSetting.setOnClickListener {
             //  callPGURL("https://atulautomotive.online/architect-signup")
             startActivity(Intent(requireActivity(), SettingActivity::class.java))
         }

        ///////// Packed Section //////////
        binding.llPackedReqOut.setOnClickListener {
            startActivity(Intent(requireActivity(), CustOrderListActivity::class.java))

        }
        binding.llPackedCompOut.setOnClickListener {

        }
        binding.llPackedNewSlot.setOnClickListener {
            startActivity(Intent(requireActivity(), SlotPackerListActivity::class.java))
        }
        binding.llPackedCompSlot.setOnClickListener {

        }
        binding.llPackedHistory.setOnClickListener {

        }

        /////////////// Dispatched Section ////////////////

        binding.llPending.setOnClickListener {

            startActivity(Intent(requireActivity(), CustOrderDispchListActivity::class.java))

        }
        binding.llDeliverd.setOnClickListener {

        }
        binding.tvDisptchComp.setOnClickListener {

        }
        binding.llDelPending.setOnClickListener {

        }
        binding.llDelDeliverd.setOnClickListener {

        }

     /*   binding.llPending.setOnClickListener {
            shopActivity.navController.navigate(R.id.action_navigation_home_to_navigation_order)
            //     (shopActivity).replaceFrag(R.id.action_ShopCategoryFragment_to_ShopSubCategoryFragment, "", data[pos].categoryID, data[pos].categoryName)

        }
        binding.llDeliverd.setOnClickListener {
            shopActivity.navController.navigate(R.id.action_navigation_home_to_navigation_order)

        }*/

        /*    binding.llApproved.setOnClickListener {
                shopActivity.navController.navigate(R.id.action_navigation_home_to_navigation_order)

            }
            binding.llRejected.setOnClickListener {
                shopActivity.navController.navigate(R.id.action_navigation_home_to_navigation_order)

            }
            binding.llDispatched.setOnClickListener {
                shopActivity.navController.navigate(R.id.action_navigation_home_to_navigation_order)

            }*/

        return root

    }


    fun apiAllGet() {
        SalesApp.isAddAccessToken = true
        apiClient = ApiController(activity, this)
        val params = Utility.getParmMap()
        apiClient.progressView.showLoader()
        apiClient.getApiPostCall(ApiContants.getDashboard, params)
    }

    override fun success(tag: String?, jsonElement: JsonElement) {
        try {
            apiClient.progressView.hideLoader()

            if (tag == ApiContants.logout) {
                val baseResponseBean = apiClient.getConvertIntoModel<BaseResponseBean>(
                    jsonElement.toString(),
                    BaseResponseBean::class.java
                )
                Toast.makeText(activity, baseResponseBean.msg, Toast.LENGTH_SHORT).show()
                PrefManager.clear()
                GeneralUtilities.launchActivity(
                    requireContext() as AppCompatActivity?,
                    LoginActivity::class.java
                )
                requireActivity().finishAffinity()
            }

            if (tag == ApiContants.getDashboard) {
                val salesmanDashboardBean = apiClient.getConvertIntoModel<DashboardBean>(
                    jsonElement.toString(),
                    DashboardBean::class.java
                )

                if (salesmanDashboardBean.error == false) {
                    if (salesmanDashboardBean.data.areaReport != null) {
                        areaReport = salesmanDashboardBean.data.areaReport
                    }
                    binding.apply {
                        tvPending.text = salesmanDashboardBean.data.pendingOrder
                        tvDeliverd.text = salesmanDashboardBean.data.deliveredOrder
                        /*   tvApproved.text = salesmanDashboardBean.data.approvedOrder
                           tvRejected.text = salesmanDashboardBean.data.rejectedOrder
                           tvDispatched.text = salesmanDashboardBean.data.dispatchedOrder*/
                        tvThisMonthExpenses.text =
                            ApiContants.currency + salesmanDashboardBean.data.thisMonthExpense
                        tvLastMonthExpenses.text =
                            ApiContants.currency + salesmanDashboardBean.data.lastMonthExpense
                    }

                    //   handleRcDashboard(salesmanDashboardBean.data)

                } else {
                    Toast.makeText(activity, salesmanDashboardBean.msg, Toast.LENGTH_SHORT).show()
                }
            }


        } catch (e: Exception) {
            Log.d("error>>", e.localizedMessage)
        }
    }

    override fun failure(tag: String?, errorMessage: String) {
        apiClient.progressView.hideLoader()
        Utility.showSnackBar(requireActivity(), errorMessage)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun dialog(wayType: String) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setMessage("Are you sure you want to " + wayType + "?")
            .setCancelable(false)
            .setPositiveButton("Yes") { dialog, id ->
                // Delete selected note from database
                dialog.dismiss()

            }
            .setNegativeButton("No") { dialog, id ->
                // Dismiss the dialog
                dialog.dismiss()

                /*if (binding.switchOfficeBreak.isChecked==true){
                    binding.switchOfficeBreak.isChecked=false
                }else{
                    binding.switchOfficeBreak.isChecked=true
                }*/
            }
        val alert = builder.create()
        alert.show()

    }

    ///////////////////////Location//////////////////////////////

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            requireContext().getSystemService(AppCompatActivity.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    @SuppressLint("MissingPermission", "SetTextI18n")
    private fun getLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient.lastLocation.addOnCompleteListener(requireActivity()) { task ->
                    val location: Location? = task.result
                    if (location != null) {
                        val geocoder = Geocoder(requireContext(), Locale.getDefault())
                        list = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                        Log.d("zxxzv", "Lat" + Gson().toJson(list?.get(0)?.latitude))
                        Log.d("zxxzv", "Long" + Gson().toJson(list?.get(0)?.longitude))
                        Log.d("zxxzv", Gson().toJson(list?.get(0)?.countryName))
                        Log.d("zxxzv", Gson().toJson(list?.get(0)?.locality))
                        Log.d("zxxzv", Gson().toJson(list?.get(0)?.getAddressLine(0)))

                        currentLoc = list?.get(0)?.getAddressLine(0)
                        /*    mainBinding.apply {
                                tvLatitude.text = "Latitude\n${list[0].latitude}"
                                tvLongitude.text = "Longitude\n${list[0].longitude}"
                                tvCountryName.text = "Country Name\n${list[0].countryName}"
                                tvLocality.text = "Locality\n${list[0].locality}"
                                tvAddress.text = "Address\n${list[0].getAddressLine(0)}"
                            }*/
                    }
                }
            } else {
                Toast.makeText(requireContext(), "Please turn on location", Toast.LENGTH_LONG)
                    .show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermissions()
        }
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ),
            permissionId
        )
    }

    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                requireActivity(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                requireActivity(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {

            return true
        }
        return false
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == permissionId) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getLocation()
            }
        } else {
            //  checkPermissions()
        }
    }

    private fun setupFabButtons() {
        binding.fabMenuActions.shrink()
        binding.fabMenuActions.setOnClickListener(this)
        binding.fabCreateExpnse.setOnClickListener(this)
        binding.fabCreateOrder.setOnClickListener(this)
        binding.fabCreateROder.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.fab_menu_actions -> {
                //  expandOrCollapseFAB()
                requireActivity().startActivity(
                    Intent(
                        requireActivity(),
                        CustomerListActivity::class.java
                    )
                )
            }
        }
    }

    private fun expandOrCollapseFAB() {
        if (binding.fabMenuActions.isExtended) {
            binding.fabMenuActions.shrink()
            binding.fabCreateExpnse.hide()
            binding.fabCreateOrder.hide()
            binding.fabCreateROder.hide()
            binding.tvCreateOrder.visibility = View.GONE
            binding.tvCreateExpnse.visibility = View.GONE
            binding.tvCreateROder.visibility = View.GONE
        } else {
            binding.fabMenuActions.extend()
            binding.fabCreateExpnse.show()
            binding.fabCreateROder.hide()
            binding.fabCreateOrder.show()
            binding.tvCreateExpnse.visibility = View.VISIBLE
            binding.tvCreateOrder.visibility = View.VISIBLE
            binding.tvCreateROder.visibility = View.GONE
        }
    }

    fun dialogRemark() {
        val builder = AlertDialog.Builder(requireContext(), R.style.CustomAlertDialog)
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
            if (binding.switchDayStart.isChecked == true) {
                binding.switchDayStart.isChecked = false
            } else {
                binding.switchDayStart.isChecked = true
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        requireActivity().startService(Intent(requireActivity(), LocationService::class.java))
    }

}