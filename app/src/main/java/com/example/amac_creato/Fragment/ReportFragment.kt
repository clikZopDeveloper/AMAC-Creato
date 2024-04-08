package com.example.amac_creato.Fragment

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.amac_creato.ApiHelper.ApiController
import com.example.amac_creato.ApiHelper.ApiResponseListner
import com.example.amac_creato.Model.*
import com.example.amac_creato.R
import com.example.amac_creato.Utills.GeneralUtilities
import com.example.amac_creato.Utills.RvStatusComplClickListner
import com.example.amac_creato.Utills.SalesApp
import com.example.amac_creato.Utills.Utility
import com.example.amac_creato.databinding.FragmentReportBinding
import com.google.gson.JsonElement
import com.stpl.antimatter.Utils.ApiContants
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class ReportFragment : Fragment(), ApiResponseListner {
    private var _binding: FragmentReportBinding? = null
    var statusType = "Customer Wise Report"
    private val binding get() = _binding!!
    private lateinit var apiClient: ApiController
    var customerID = 0
    var toDate = ""
    var fromDate = ""

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentReportBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.appbarLayout.ivMenu.setImageDrawable(resources.getDrawable(R.drawable.ic_back_black))
        binding.appbarLayout.ivMenu.setOnClickListener {
            requireActivity().onBackPressed()
        }
        binding.appbarLayout.tvTitle.text = "Report"

        binding.ivFilter.setOnClickListener {
            dialogFilter()
        }

        val todaydate: LocalDate = LocalDate.now()
        fromDate = LocalDate.now().withDayOfMonth(1).toString()
        System.out.println("Months first date in yyyy-mm-dd: " + LocalDate.now().withDayOfMonth(1))


        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val current = LocalDateTime.now().format(formatter)
        toDate = current.toString()
        System.out.println("yyyy-mm-dd: " + fromDate + "\n" + current)

        apiCustomerList()
        return root

    }

    fun apiCustomerList() {
        SalesApp.isAddAccessToken = true
        apiClient = ApiController(requireContext(), this)
        val params = Utility.getParmMap()
        apiClient.progressView.showLoader()
        apiClient.getApiPostCall(ApiContants.GetCustomer, params)
    }


    fun setCustomerData(data: List<GetCustomerBean.Data>) {
        val state = arrayOfNulls<String>(data.size)
        for (i in data.indices) {
            state[i] = data.get(i).name
        }

        binding.stateCustomer.setAdapter(
            ArrayAdapter(
                requireContext(),
                android.R.layout.simple_list_item_1, state
            )
        )

        binding.stateCustomer.setOnItemClickListener(AdapterView.OnItemClickListener { parent, view, position, id ->
            //  var sourceName = SalesApp.sourceList.get(position).name


            Log.d("StateID", "" + parent.getItemAtPosition(position).toString())
            for (customerBean in data) {
                if (customerBean.name.equals(
                        parent.getItemAtPosition(position).toString()
                    )
                ) {
                    customerID = customerBean.id
                    Log.d("StateID", "" + customerBean.id)

                    binding.handleViewVisible.visibility = View.VISIBLE
                    binding.tvMobNo.setText("Mobile Number : " + customerBean.mobile)
                 //   binding.tvEmail.setText("Email : " + customerBean.email)
                    binding.stateCustomer.setText(parent.getItemAtPosition(position).toString())


                }
            }
            Toast.makeText(
                requireContext(),
                binding.stateCustomer.getText().toString(),
                Toast.LENGTH_SHORT
            ).show()
            setCustomerData(data)
        })
    }


    fun dialogFilter() {
        /*      val builder = AlertDialog.Builder(this,R.style.CustomAlertDialog)
                  .create()
              val dialog = layoutInflater.inflate(R.layout.dialog_reamrk,null)

              builder.setView(dialog)

              builder.setCanceledOnTouchOutside(false)*/
        // builder.show()

        val dialog: Dialog = GeneralUtilities.openBootmSheetDailog(
            R.layout.dialog_filter, R.style.AppBottomSheetDialogTheme,
            requireActivity()
        )
        val ivClose = dialog.findViewById<ImageView>(R.id.ivClose)
        val btnSearch = dialog.findViewById<TextView>(R.id.btnSearch) as TextView
        val editDate = dialog.findViewById<EditText>(R.id.editDate) as EditText
        val editToDate = dialog.findViewById<EditText>(R.id.editToDate) as EditText

        ivClose.setOnClickListener { dialog.dismiss() }
        editDate.setOnClickListener(View.OnClickListener {
            val c = Calendar.getInstance()
            val year = c[Calendar.YEAR]
            val month = c[Calendar.MONTH]
            val day = c[Calendar.DAY_OF_MONTH]
            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { view, year, monthOfYear, dayOfMonth ->
                    //  dob.setText(dateofnews);
                    val dateofnews =
                        "${year.toString() + "-" + (monthOfYear + 1).toString() + "-" + dayOfMonth.toString()}"
                    fromDate = dateofnews
                    editDate.setText(dateofnews)
                },
                year, month, day
            )
            datePickerDialog.show()
        })

        editToDate.setOnClickListener(View.OnClickListener {
            val c = Calendar.getInstance()
            val year = c[Calendar.YEAR]
            val month = c[Calendar.MONTH]
            val day = c[Calendar.DAY_OF_MONTH]
            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { view, year, monthOfYear, dayOfMonth ->
                    //  dob.setText(dateofnews);
                    val dateofnews =
                        "${year.toString() + "-" + (monthOfYear + 1).toString() + "-" + dayOfMonth.toString()}"
                    toDate = dateofnews
                    editToDate.setText(dateofnews)
                },
                year, month, day
            )
            datePickerDialog.show()
        })
        btnSearch.setOnClickListener {
            if (editDate.text.isNullOrEmpty()) {
                Toast.makeText(requireContext(), "Select From Date", Toast.LENGTH_SHORT).show()
            } else if (editToDate.text.isNullOrEmpty()) {
                Toast.makeText(requireContext(), "Select To Date", Toast.LENGTH_SHORT).show()
            } else {
                dialog.dismiss()


            }

        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun success(tag: String?, jsonElement: JsonElement?) {
        try {
            apiClient.progressView.hideLoader()
            if (tag == ApiContants.getCustomer) {
                val getCustomerBean = apiClient.getConvertIntoModel<GetCustomerBean>(
                    jsonElement.toString(),
                    GetCustomerBean::class.java
                )

                if (getCustomerBean.error == false) {
                    setCustomerData(getCustomerBean.data)
                }

            }

        } catch (e: Exception) {
            Log.d("error>>", e.localizedMessage)
        }

    }

    override fun failure(tag: String?, errorMessage: String) {
        apiClient.progressView.hideLoader()
        Utility.showSnackBar(requireActivity(), errorMessage)
        Log.d("error", errorMessage)

    }

}