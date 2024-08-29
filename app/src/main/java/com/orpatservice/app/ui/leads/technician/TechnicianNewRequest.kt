package com.orpatservice.app.ui.leads.technician

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.*
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.orpatservice.app.R
import com.orpatservice.app.data.Resource
import com.orpatservice.app.data.Status
import com.orpatservice.app.data.model.requests_leads.CancelLeadResponse
import com.orpatservice.app.data.model.requests_leads.LeadData
import com.orpatservice.app.data.model.requests_leads.RequestLeadResponse
import com.orpatservice.app.databinding.FragmentNewRequestBinding
import com.orpatservice.app.databinding.LayoutDialogCancelLeadBinding
import com.orpatservice.app.ui.leads.new_lead_fragment.NewRequestsFragment
import com.orpatservice.app.ui.leads.technician.adapter.TechnicianRequestLeadAdapter
import com.orpatservice.app.ui.leads.technician.response.TechnicianLeadData
import com.orpatservice.app.ui.leads.technician.response.TechnicianRequestLeadResponse
import com.orpatservice.app.ui.leads.viewmodel.RequestsLeadsViewModel
import com.orpatservice.app.ui.login.LoginActivity
import com.orpatservice.app.utils.Constants
import com.orpatservice.app.utils.Utils
import com.tapadoo.alerter.Alerter

class TechnicianNewRequest : Fragment() {

    private lateinit var binding: FragmentNewRequestBinding
    private var leadDataArrayList: ArrayList<TechnicianLeadData> = ArrayList()
    private var tempDataArrayList: ArrayList<TechnicianLeadData> = ArrayList()
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var requestLeadsViewModel: RequestsLeadsViewModel
    private var removeIndex = -1
    private var isLoading: Boolean = false
    private var pageNumber = 1
    private var totalPage = 1
    val PERMISSION_ID = 42
    lateinit var mFusedLocationClient: FusedLocationProviderClient
    private var latitude : String = ""
    private var longitude : String = ""
    private var total = 0
    private  var totalCount :TextView? = null

    //Click listener for List Item
    private val onItemClickListener: (Int, View) -> Unit = { position, view ->
        when (view.id) {
            R.id.btn_view_details -> {
                val intent = Intent(activity, TechnicianCustomerDetailsActivity::class.java)

                intent.putExtra(Constants.LEAD_DATA, leadDataArrayList[position])
                //No need to send new lead data because closing complaint perform through adapter
                intent.putExtra(Constants.LEAD_TYPE, Constants.LEAD_NEW)
                startActivity(intent)
            }
            R.id.tv_request_location -> {
               // openDirection(position)
            }
            R.id.btn_direction -> {
                openDirection(position)
            }
        }
    }

    private val requestsLeadsAdapter = TechnicianRequestLeadAdapter(
        leadDataArrayList, itemClickListener = onItemClickListener, Constants.LEAD_NEW
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentNewRequestBinding.inflate(inflater, container, false)

        layoutManager = LinearLayoutManager(activity)
        binding.rvNewRequest.layoutManager = layoutManager
        binding.rvNewRequest.apply {
            adapter = requestsLeadsAdapter
        }

        requestLeadsViewModel = ViewModelProvider(this)[RequestsLeadsViewModel::class.java]
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        getLastLocation()
        setObserver()
        loadUI()
        requestLeadsViewModel.loadTechnicianPendingLeads(pageNumber)

        binding.rvNewRequest.addOnScrollListener(scrollListener)

        return binding.root
    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {

                mFusedLocationClient.lastLocation.addOnCompleteListener(requireActivity()) { task ->
                    var location: Location? = task.result
                    if (location == null) {
                        requestNewLocationData()
                    } else {
                        println("latitude"+location.latitude.toString())
                        println("longitude"+location.longitude.toString())
                        latitude = location.latitude.toString()
                        longitude = location.longitude.toString()
                    }
                }
            } else {
                //Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
              //  requireActivity().onBackPressed()
            }
        } else {
            requestPermissions()
        }
    }


    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            PERMISSION_ID
        )
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            PERMISSION_ID -> if (grantResults.isNotEmpty() && grantResults[0] ==
                PackageManager.PERMISSION_GRANTED
            ) {
                getLastLocation()
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        var mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 0
        mLocationRequest.fastestInterval = 0
        mLocationRequest.numUpdates = 1

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        mFusedLocationClient!!.requestLocationUpdates(
            mLocationRequest, mLocationCallback,
            Looper.myLooper()
        )
    }
    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val mLastLocation: Location = locationResult.lastLocation
            latitude = mLastLocation.latitude.toString()
            longitude = mLastLocation.longitude.toString()
        }
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager = requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }


    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            if (!isLoading) {
                if (layoutManager.findLastCompletelyVisibleItemPosition() == leadDataArrayList.size - 1 && totalPage > pageNumber) {
                    pageNumber++
                    binding.cpiLoading.visibility = View.VISIBLE
                    requestLeadsViewModel.loadPendingLeads(pageNumber)
                    isLoading = true
                }
            }
        }
    }

    private fun openDirection(position: Int) {
        val dir: String
        if(!leadDataArrayList[position].latitude.isNullOrEmpty() && !leadDataArrayList[position].longitude.isNullOrEmpty()) {
            dir = leadDataArrayList[position].latitude + "" + "," + "" + leadDataArrayList[position].longitude
        }else{
            dir = "" + "" + "," + "" +""
        }
        val intentUri = Uri.Builder().apply {
            scheme("https")
            authority("www.google.com")
            appendPath("maps")
            appendPath("dir")
            appendPath("")
            appendQueryParameter("api", "1")
            appendQueryParameter("destination", dir)
            //appendQueryParameter("destination", "${leadDataArrayList[position].latitude},-${(leadDataArrayList[position].longitude)}")
        }.build()
        startActivity(Intent(Intent.ACTION_VIEW).apply {
            data = intentUri
        })
    }

    private fun setObserver() {
        requestLeadsViewModel.technicianPendingLeadsData.observe(viewLifecycleOwner, this::getPendingLeads)
      //  requestLeadsViewModel.cancelLeadsData.observe(viewLifecycleOwner, this::doCancelLead)
    }

    private fun getPendingLeads(resources: Resource<TechnicianRequestLeadResponse>) {
        when (resources.status) {
            Status.LOADING -> {
                binding.cpiLoading.visibility = View.VISIBLE
            }
            Status.ERROR -> {
                binding.cpiLoading.visibility = View.GONE
                isLoading = false
                /*activity?.let {
                    Alerter.create(it)
                        .setText(resources.error?.message.toString())
                        .setBackgroundColorRes(R.color.orange)
                        .setDuration(1500)
                        .show()
                }*/

                Utils.instance.popupPinUtil(requireActivity(),
                    resources.error?.message.toString(),
                    "",
                    false)
            }
            else -> {
                binding.cpiLoading.visibility = View.GONE

                val response = resources.data

                response?.let {
                    if (it.success) {
                        totalPage = response.data.pagination.last_page
                        total = response.data.pagination.total

                        Constants.TECHNICIAN_TOTAL = total.toString()

                        totalCount?.text = Constants.TECHNICIAN_TOTAL

                        leadDataArrayList.clear()
                        leadDataArrayList.addAll(response.data.data)
                        requestsLeadsAdapter.notifyDataSetChanged()

                        isLoading = false

                        if(leadDataArrayList.isNullOrEmpty()){
                            binding.tvNoLeads.visibility = View.VISIBLE
                        } else {
                            binding.tvNoLeads.visibility = View.GONE
                        }
                    } else{
                    if(it.code == 401){
                        val intent = Intent(requireActivity(), LoginActivity::class.java)
                        startActivity(intent)
                        requireActivity().finish()
                    }
                }
                }
            }
        }
    }

    private fun doCancelLead(resources: Resource<CancelLeadResponse>) {
        when (resources.status) {
            Status.LOADING -> {
                binding.cpiLoading.visibility = View.VISIBLE
            }
            Status.ERROR -> {
                binding.cpiLoading.visibility = View.GONE
                activity?.let {
                    Alerter.create(it)
                        .setText(resources.error?.message.toString())
                        .setBackgroundColorRes(R.color.orange)
                        .setDuration(1500)
                        .show()
                }
            }
            else -> {
                binding.cpiLoading.visibility = View.GONE

                val response = resources.data

                response?.let {
                    if (it.success) {

                        if (removeIndex != -1) {
                            leadDataArrayList.removeAt(removeIndex)
                            requestsLeadsAdapter.notifyItemRemoved(removeIndex)

                        }
                        activity?.let {
                            Alerter.create(it)
                                .setText(resources.data.message)
                                .setBackgroundColorRes(R.color.orange)
                                .setDuration(1500)
                                .show()
                        }
                    }
                }
            }
        }
    }

    fun loadSearchLead(query: String) {
        requestLeadsViewModel.searchPendingLeads(query)
        tempDataArrayList.clear()
        tempDataArrayList.addAll(leadDataArrayList)
        leadDataArrayList.clear()
        requestsLeadsAdapter.notifyDataSetChanged()
        isLoading = true
        loadUI()
    }

    fun loadOldLeadData() {
        leadDataArrayList.clear()
        leadDataArrayList.addAll(tempDataArrayList)
        requestsLeadsAdapter.notifyDataSetChanged()
    }

    private fun confirmationDialog(context: Context, id: Int?) {

        val dialogBinding = LayoutDialogCancelLeadBinding.inflate(layoutInflater)
        val dialog = MaterialAlertDialogBuilder(context)
            .setView(dialogBinding.root)
        dialog.setCancelable(false)
        val alertDialog = dialog.show() as AlertDialog

        dialogBinding.btnReject.setOnClickListener {
            if (id != null && Utils.instance.validateEditText(dialogBinding.edtRejectionReason)) {
                requestLeadsViewModel.doCancelLead(id, dialogBinding.edtRejectionReason.text.toString())
                binding.cpiLoading.visibility = View.VISIBLE
                alertDialog.dismiss()
            } else {
                Toast.makeText(activity, "Lead Id not found. Please try again!", Toast.LENGTH_SHORT)
                    .show()
                alertDialog.dismiss()
            }
        }
        dialogBinding.btnCancel.setOnClickListener{
            alertDialog.dismiss()
        }
    }

    private fun loadUI () {
        binding.tvNoLeads.visibility = View.GONE
        binding.cpiLoading.visibility = View.VISIBLE
    }

    override fun onResume() {
        super.onResume()
        //totalCount?.text = Constants.ASSIGN_TOTAL.toString()
        setObserver()

    }

    fun loadTotalLead(toolbarTotalLead: TextView) {
        totalCount = toolbarTotalLead
        println("toolbarTotalLead"+toolbarTotalLead)
        totalCount?.text = Constants.TECHNICIAN_TOTAL.toString()
    }


    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TechnicianNewRequest().apply {
                arguments = Bundle().apply {
                }
            }
    }
}