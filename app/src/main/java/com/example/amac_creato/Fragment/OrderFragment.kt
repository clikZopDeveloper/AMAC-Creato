package com.example.amac_creato.Fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.amac_creato.Activity.BarcodeScanActivity
import com.example.amac_creato.Activity.CustOrderListActivity
import com.example.amac_creato.Activity.LoginActivity
import com.example.amac_creato.Adapter.CustomersListAdapter
import com.example.amac_creato.ApiHelper.ApiController
import com.example.amac_creato.ApiHelper.ApiResponseListner
import com.example.amac_creato.Model.GetCustomersBean
import com.example.amac_creato.R
import com.example.amac_creato.Utills.GeneralUtilities
import com.example.amac_creato.Utills.PrefManager
import com.example.amac_creato.Utills.SalesApp
import com.example.amac_creato.Utills.Utility
import com.example.amac_creato.databinding.FragmentOrderBinding
import com.google.gson.JsonElement
import com.stpl.antimatter.Utils.ApiContants

class OrderFragment : Fragment(), ApiResponseListner {

    private var _binding: FragmentOrderBinding? = null
    private lateinit var apiClient: ApiController
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentOrderBinding.inflate(inflater, container, false)
        val root: View = binding.root
        binding.appbarLayout.ivMenu.setImageDrawable(resources.getDrawable(R.drawable.ic_logout_24))
        binding.appbarLayout.ivMenu.setOnClickListener {
            //requireActivity().onBackPressed()
            Toast.makeText(requireContext(), "Logout Successfully", Toast.LENGTH_SHORT).show()
            PrefManager.clear()
            GeneralUtilities.launchActivity(
                requireActivity() as AppCompatActivity?,
                LoginActivity::class.java
            )
            requireActivity().finishAffinity()
        }

      //  binding.appbarLayout.ivMenu.visibility=View.GONE
        binding.appbarLayout.tvTitle.text = "All Customers"
        apiClient = ApiController(requireContext(), this)

        binding.fbScanner.setOnClickListener {
            startActivity(Intent(requireContext(), BarcodeScanActivity::class.java))
        }

        apiAllCustomerList()
        return root
    }

    fun apiAllCustomerList() {
        SalesApp.isAddAccessToken = true
        val params = Utility.getParmMap()
        apiClient.progressView.showLoader()
        //  params["status"]=statusType
        apiClient.getApiPostCall(ApiContants.getCustomers, params)

    }

    override fun success(tag: String?, jsonElement: JsonElement?) {
        try {
            apiClient.progressView.hideLoader()
            if (tag == ApiContants.getCustomers) {
                val projectBean = apiClient.getConvertIntoModel<GetCustomersBean>(
                    jsonElement.toString(),
                    GetCustomersBean::class.java
                )
                if (projectBean.error == false) {
                    handleCustomersList(projectBean.data)
                }
            }
        } catch (e: Exception) {

        }
    }

    override fun failure(tag: String?, errorMessage: String?) {
        apiClient.progressView.hideLoader()
        Utility.showSnackBar(requireActivity(), errorMessage!!)
    }

    fun handleCustomersList(data: List<GetCustomersBean.Data>) {
        binding.rcTeamContactList.layoutManager = LinearLayoutManager(requireContext())
        var mAdapter = CustomersListAdapter(requireActivity(), data
        ) { status, workstatus, amt, id ->
            startActivity(
                Intent(
                    requireContext(),
                    CustOrderListActivity::class.java
                ).putExtra("customer_id", id.toString())
            )
        }
        binding.rcTeamContactList.adapter = mAdapter
        // rvMyAcFiled.isNestedScrollingEnabled = false

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}