package com.orpatservice.app.ui.leads.new_lead_fragment

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.*
import com.orpatservice.app.R
import com.orpatservice.app.data.Resource
import com.orpatservice.app.data.Status
import com.orpatservice.app.data.model.requests_leads.LeadData
import com.orpatservice.app.data.model.requests_leads.RequestLeadResponse
import com.orpatservice.app.databinding.FragmentAssignToTechnicianBinding
import com.orpatservice.app.ui.admin.technician.TechniciansViewModel
import com.orpatservice.app.ui.leads.new_lead_fragment.activity.ChangeTechnicianActivity
import com.orpatservice.app.ui.leads.new_lead_fragment.activity.LocationActivity
import com.orpatservice.app.ui.leads.new_lead_fragment.adapter.AssignTechnicianLeadAdapter
import com.orpatservice.app.ui.leads.service_center.TaskCompletedDetailsActivity
import com.orpatservice.app.ui.leads.service_center.TechnicianTaskUpdateActivity
import com.orpatservice.app.ui.login.LoginActivity
import com.orpatservice.app.utils.Constants
import com.orpatservice.app.utils.Utils
import com.tapadoo.alerter.Alerter

class AssignToTechnicianFragment : Fragment() , LocationListener {

    val PERMISSION_ID = 42
    lateinit var mFusedLocationClient: FusedLocationProviderClient


    private lateinit var binding: FragmentAssignToTechnicianBinding
    private var leadDataArrayList: ArrayList<LeadData> = ArrayList()
    private var tempDataArrayList: ArrayList<LeadData> = ArrayList()
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var requestLeadsViewModel: TechniciansViewModel
    private var isLoading: Boolean = false
    private var pageNumber = 1
    private var totalPage = 1
    private var latitude: String =""
    private var longitude: String =""
    private var total = 0
    private  var totalCount :TextView? = null


    private val onItemClickListener: (Int, View) -> Unit = { position, view ->
        when (view.id) {
            R.id.btn_view_details -> {
                    val intent = Intent(activity, TechnicianTaskUpdateActivity::class.java)
                    intent.putExtra(Constants.LEAD_DATA, leadDataArrayList[position])
                    intent.putExtra(Constants.POSITION, position)
                    startActivity(intent)

            }

            R.id.btn_update_otp -> {

                val intent = Intent(activity, TaskCompletedDetailsActivity::class.java)
                intent.putExtra(Constants.TASK_DATA, leadDataArrayList[position])
                startActivity(intent)
            }

            R.id.tv_request_location ->{
                /*val intent = Intent(activity, LocationActivity::class.java)
                intent.putExtra(Constants.LATITUDE, leadDataArrayList[position].latitude)
                intent.putExtra(Constants.LONGITUDE, leadDataArrayList[position].longitude)
                startActivity(intent)*/
            // openDirection(position)

            }
            R.id.btn_hide_view_details ->{

                Utils.instance.popupPinUtil(requireActivity(),
                    "Servicing by Technician in process",
                    "",
                    false)
            }
            /*R.id.btn_hide_change_technician ->{

                Utils.instance.popupPinUtil(requireActivity(),
                    "Technician cannot be updated at this stage",
                    "",
                    false)
            }*/
        }
    }

    private val requestsLeadsAdapter = AssignTechnicianLeadAdapter(
        leadDataArrayList,
        itemClickListener = onItemClickListener,
        Constants.LEAD_ASSIGN_TECHNICIAN
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
        binding = FragmentAssignToTechnicianBinding.inflate(inflater, container, false)


        requestLeadsViewModel = ViewModelProvider(this)[TechniciansViewModel::class.java]


        setObserver()
        loadUI()
        utilUIBind()


        binding.rvAssignTechnician.addOnScrollListener(scrollListener)

        return binding.root
    }

    private fun utilUIBind() {
        layoutManager = LinearLayoutManager(activity)
        binding.rvAssignTechnician.layoutManager = layoutManager
        binding.rvAssignTechnician.apply {
            adapter = requestsLeadsAdapter
        }

        binding.edtTechnicianSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {

            }

            override fun beforeTextChanged(char: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(charSeq: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(!charSeq.isNullOrEmpty()){
                    leadDataArrayList.clear()
                    tempDataArrayList.clear()
                    filter(charSeq.toString())
                }else{
                    leadDataArrayList.clear()
                    tempDataArrayList.clear()
                    filter("")

                }
            }
        })
    }

    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            if (!isLoading) {
                if (layoutManager.findLastCompletelyVisibleItemPosition() == leadDataArrayList.size - 1 && totalPage > pageNumber) {
                    pageNumber++
                    binding.cpiLoading.visibility = View.VISIBLE
                    requestLeadsViewModel.completedTechnicianLead(pageNumber)
                    isLoading = true
                }
            }
        }
    }

    private fun setObserver() {
        requestLeadsViewModel.completedTechnicianLead(pageNumber)
        requestLeadsViewModel.completedTechnicianData.observe(viewLifecycleOwner, this::getAssignedLeads)
    }
    fun filter(text: String) {
        loadSearchLead(text)
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
           // appendQueryParameter("destination", "${leadDataArrayList[position].latitude},-${(leadDataArrayList[position].longitude)}")
        }.build()
        startActivity(Intent(Intent.ACTION_VIEW).apply {
            data = intentUri
        })
    }
    private var nextPage: String? = null
    private fun getAssignedLeads(resources: Resource<RequestLeadResponse>) {
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
                        nextPage = it.data.pagination.next_page_url
                        Constants.VERIFY_TOTAL = total.toString()

                        totalCount?.text = Constants.VERIFY_TOTAL

                        leadDataArrayList.clear()
                        tempDataArrayList.clear()
                        leadDataArrayList.addAll(response.data.data)
                        tempDataArrayList.addAll(response.data.data)
                        requestsLeadsAdapter.notifyDataSetChanged()

                        isLoading = false
                        if (pageNumber == 1) {
                            requestsLeadsAdapter.notifyDataSetChanged()
                        }else {
                            requestsLeadsAdapter.notifyItemInserted(leadDataArrayList.size - 1)
                        }
                        if(leadDataArrayList.isNullOrEmpty()){
                            binding.tvNoLeads.visibility = View.VISIBLE
                        } else {
                            binding.tvNoLeads.visibility = View.GONE
                        }
                    }else{
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


    fun loadTotalLead(toolbarTotalLead: TextView) {
        totalCount = toolbarTotalLead

        totalCount?.text = Constants.VERIFY_TOTAL.toString()
    }


    fun loadSearchLead(query: String) {
        leadDataArrayList.clear()
        tempDataArrayList.clear()
        requestLeadsViewModel.searchCompletedLeads(query)
        tempDataArrayList.clear()
        tempDataArrayList.addAll(leadDataArrayList)
        //leadDataArrayList.addAll(tempDataArrayList)
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

    private fun loadUI () {
        binding.tvNoLeads.visibility = View.GONE
        binding.cpiLoading.visibility = View.VISIBLE
    }
    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AssignToTechnicianFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }

    override fun onResume() {
        super.onResume()
        setObserver()
    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {

                activity?.let {
                    mFusedLocationClient.lastLocation.addOnCompleteListener(it) { task ->
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
                }
            } else {
                //Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermissions()
        }
    }

    private fun checkPermissions(): Boolean {
        if (activity?.let {
                ActivityCompat.checkSelfPermission(
                    it,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            } == PackageManager.PERMISSION_GRANTED &&
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
        activity?.let {
            ActivityCompat.requestPermissions(
                it,
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ),
                PERMISSION_ID
            )
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_ID) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
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
            var mLastLocation: Location = locationResult.lastLocation
            // findViewById<TextView>(R.id.latTextView).text = mLastLocation.latitude.toString()
            //findViewById<TextView>(R.id.lonTextView).text = mLastLocation.longitude.toString()
        }
    }

    private fun isLocationEnabled(): Boolean {
        var locationManager: LocationManager = activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    override fun onLocationChanged(p0: Location) {
        TODO("Not yet implemented")
    }


}