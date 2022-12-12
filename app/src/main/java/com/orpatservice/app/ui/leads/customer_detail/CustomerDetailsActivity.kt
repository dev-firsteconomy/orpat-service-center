package com.orpatservice.app.ui.leads.customer_detail

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.ContentValues
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.*
import android.provider.MediaStore
import android.provider.Settings
import android.text.Html
import android.view.*
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.*
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.gms.location.*
import com.google.gson.JsonObject
import com.orpatservice.app.R
import com.orpatservice.app.data.Resource
import com.orpatservice.app.data.Status
import com.orpatservice.app.data.model.data.InvoiceData
import com.orpatservice.app.data.model.requests_leads.LeadData
import com.orpatservice.app.data.model.requests_leads.WarrantryPart
import com.orpatservice.app.data.sharedprefs.SharedPrefs
import com.orpatservice.app.databinding.ActivityCustomerDetailsBinding
import com.orpatservice.app.databinding.ItemComplaintBinding
import com.orpatservice.app.ui.admin.technician.*
import com.orpatservice.app.ui.leads.adapter.ComplaintAdapter
import com.orpatservice.app.ui.leads.customer_detail.adapter.ServiceableWarrantryPartAdapter
import com.orpatservice.app.ui.leads.new_lead_fragment.adapter.CustomExpandableListAdapter
import com.orpatservice.app.ui.leads.new_lead_fragment.new_lead_request.NewRequestResponse
import com.orpatservice.app.ui.leads.new_lead_fragment.new_lead_request.RequestData
import com.orpatservice.app.ui.leads.new_lead_fragment.new_lead_request.VerifyGSTRequestData
import com.orpatservice.app.ui.leads.service_center.RequestLeadActivity
import com.orpatservice.app.utils.CommonUtils
import com.orpatservice.app.utils.Constants
import com.orpatservice.app.utils.Utils
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.json.JSONException
import java.io.File
import java.io.FileDescriptor
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by Vikas Singh on 26/12/21.
 */

const val MY_PERMISSIONS_WRITE_READ_REQUEST_CODE = 1000
class CustomerDetailsActivity : AppCompatActivity(), View.OnClickListener, CameraBottomSheetDialogFragment.BottomSheetItemClick/*, DatePickerDialog.OnDateSetListener*/ {

    companion object {
        private const val REQUEST_CAMERA_PERMISSION = 10
    }
    private lateinit var binding: ActivityCustomerDetailsBinding
    private lateinit var leadData: LeadData
    private lateinit var complaintAdapter: ComplaintAdapter
    private var resultUri: Uri? = null
    private var mCurrentCaptureImage: String? = null
    lateinit var customerDetailsViewModel: CustomerDetailsModel
    private var invoiceUrl: String? = null
    private lateinit var bindingAdapter : ItemComplaintBinding
    private var pos: String? = null
    private var index:Int? = 0
    private var pageNumber = 1
    private lateinit var alertDialogBuilder:Dialog
    private var assignTechnicianListMsg:String? = null
    private var techList: ArrayList<RequestData> = ArrayList()
    private var warrantyPartsList: ArrayList<WarrantryPart> = ArrayList()

    private  final val permissionCode  = 42
    lateinit var mFusedLocationClient: FusedLocationProviderClient
    private var latitude : String = ""
    private var longitude : String = ""
    lateinit var  dialog: Dialog
    var image_uri: Uri? = null

    private val RESULT_LOAD_IMAGE = 123
    val IMAGE_CAPTURE_CODE = 654

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCustomerDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        customerDetailsViewModel = ViewModelProvider(this)[CustomerDetailsModel::class.java]
        // set toolbar as support action bar
        setSupportActionBar(binding.toolbar)


        //   binding.includedContent.btnAssignTechnician.setOnClickListener(this)
        //      binding.includedContent.ivCall.setOnClickListener(this)

        supportActionBar?.apply {
            title = ""
            // show back button on toolbar
            // on back button press, it will navigate to parent activity
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        if (intent.getStringExtra(Constants.LEAD_TYPE).equals(Constants.LEAD_NEW)) {
            // binding.includedContent.btnAssignTechnician.visibility = View.VISIBLE

        } else {
            //binding.includedContent.btnAssignTechnician.visibility = View.GONE

        }


        if (SharedPrefs.getInstance().getString(Constants.USER_TYPE, "")
                .equals(Constants.SERVICE_CENTER)
        ) {
            //  binding.includedContent.btnAssignTechnician.text =
            resources.getString(R.string.btn_assign_to_technician)

        } else {
            //   binding.includedContent.btnAssignTechnician.visibility = View.GONE
            //   binding.includedContent.btnAssignTechnician.text =
            //   resources.getString(R.string.btn_close_complaint)

        }

        leadData = intent?.getParcelableExtra<LeadData>(Constants.LEAD_DATA) as LeadData

        if (leadData.enquiries.isNullOrEmpty()) {
            // binding.includedContent.tvEnquiryHeading.visibility = View.GONE
        }

        //if (!leadData.enquiries.isEmpty()){
        complaintAdapter = ComplaintAdapter(
            leadData.enquiries,
            leadData,
            itemClickListener = onItemClickListener
        )
        // binding.includedContent.btnAddTask.visibility = GONE
        binding.includedContent.rvComplaint.apply {
            adapter = complaintAdapter
        }
        /* }else{
            // binding.includedContent.btnAddTask.visibility = VISIBLE
             //binding.includedContent.tvCancelLeadHide.visibility = GONE

         }*/

        if(leadData.pending_lead_enqury_detail_count == "0" /*&& leadData.in_warranty_enquiries_count!! > "0"*/){
            binding.includedContent.btnAssignTechnician.visibility = View.VISIBLE
            binding.includedContent.btnAssignTechnicianHide.visibility = View.GONE

        }else{
            binding.includedContent.btnAssignTechnician.visibility = View.GONE
            binding.includedContent.btnAssignTechnicianHide.visibility = View.VISIBLE
        }

        binding.includedContent.tvCancelLeadHide.setOnClickListener {

            showCancelLeadPopUp()
        }
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        //  getLastLocation()
        bindUserDetails(leadData)
        setObserver()

        // customerDetailsViewModel.loadTechnicianLeads(pageNumber,leadData.id!!)
        // customerDetailsViewModel.loadAssignedTechnicianLeads(pageNumber)

    }
    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {

                mFusedLocationClient.lastLocation.addOnCompleteListener(this) { task ->
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
                onBackPressed()
            }
        } else {
            // requestPermissions()
        }
    }

    /*override fun onResume() {
        super.onResume()
        if (!checkPermissions()) {
            requestPermissions()
        } else {
            getLastLocation()
        }
    }
*/

    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    /*private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            permissionCode
        )
    }
*/

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this, arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ), com.orpatservice.app.ui.leads.technician.MY_PERMISSIONS_WRITE_READ_REQUEST_CODE
        )
    }

    /*private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.CAMERA,
                GALLERY
            ),
            permissionCode
        )
    }*/

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        val mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 0
        mLocationRequest.fastestInterval = 0
        mLocationRequest.numUpdates = 1

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
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
        val locationManager: LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }


    private fun showCancelLeadPopUp() {

        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View = inflater.inflate(R.layout.cancel_lead_popup_layout, null)
        var tv_cancel_lead_cancel = view.findViewById<TextView>(R.id.tv_cancel_lead_cancel)
        val tv_cancel_lead_ok = view.findViewById<TextView>(R.id.tv_cancel_lead_ok)
        val et_cancel_lead_reason = view.findViewById<EditText>(R.id.et_cancel_lead_reason)
        val alertDialogBuilder = Dialog(this@CustomerDetailsActivity)
        alertDialogBuilder.setContentView(view)
        //alertDialogBuilder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialogBuilder.setCancelable(false)
        tv_cancel_lead_cancel.setOnClickListener {
            alertDialogBuilder.dismiss()
        }
        tv_cancel_lead_ok.setOnClickListener {

            hitCancelLead(et_cancel_lead_reason, alertDialogBuilder)
        }
        alertDialogBuilder.show()

    }

    private fun hitCancelLead(etCancelLeadReason: EditText?,alertDialogBuilder: Dialog) {
        if (Utils.instance.validateReason(etCancelLeadReason)

        ) {
            val jsonObject = JsonObject()

            try {
                //  val jsArray  = JsonArray()
                jsonObject.addProperty("lead_cancelled_reason", etCancelLeadReason?.text.toString())

            } catch (e: JSONException) {
                e.printStackTrace()
            }
            customerDetailsViewModel.hitCancelRequest(jsonObject, leadData.id).observe(this, this::getCancelRequestLead)
            alertDialogBuilder.dismiss()
        }else{

        }
    }

    private fun setObserver() {
        customerDetailsViewModel.invoiceUploadData.observe(this, this::onFileUploaded)
        customerDetailsViewModel.verifyNumData.observe(this, this::getVerifyData)

        customerDetailsViewModel.assignToTechnicianList.observe(this, this::getTechnicianList)
        leadData.id?.let { customerDetailsViewModel.loadTechnicianLeads(pageNumber, it) }

    }

    private fun bindUserDetails(leadData: LeadData) {
        binding.includedContent.tvCustomerNameValue.text = leadData.name
        binding.includedContent.tvContactNumberValue.text = leadData.mobile
        binding.includedContent.tvPinCodeValue.text = leadData.pincode
        binding.includedContent.tvFullAddressValue.text = leadData.address1+""+" , "+""+leadData.address2+""+", "+""+leadData.landmark
        binding.includedContent.tvTvRequestIdValue.text = leadData.complain_id.toString()
        // binding.includedContent.tvRequestDateValue.text = leadData.service_center_assigned_at
        binding.includedContent.tvTimerValue.text = leadData.timer
        binding.includedContent.tvTimerValue.setTextColor(Color.parseColor(leadData.color_code))
        val str = leadData.created_at
        val delimiter = " "
        val parts = str?.split(delimiter)

        binding.includedContent.tvRequestDateValue.text = parts?.get(0)+""+" "+""+ parts?.get(1)+""+" "+""+ parts?.get(2)+""+""+"\n"+ parts?.get(3)+""+" "+""+ parts?.get(4)+"\n"

        binding.includedContent.btnAssignTechnician.setOnClickListener {


            if(!techList.isEmpty()) {

                val intent = Intent(this, AllTechnicianActivity::class.java)
                intent.putExtra(Constants.IS_NAV, Constants.ComingFrom.CUSTOMER_DETAILS)
                intent.putExtra(Constants.LEADS_ID, leadData.id.toString())
                intent.putExtra(
                    Constants.TECHNICIAN_ID,
                    leadData.technician?.first_name + "" + " " + "" + leadData.technician?.last_name
                )
                //No need to send new lead data because closing complaint perform through adapter
                startActivity(intent)
                finish()
            }else{
                assignTechnicianListMsg?.let { it1 ->
                    Utils.instance.popupPinUtil(this,
                        it1,
                        "",
                        false)
                }
            }
        }

        binding.includedContent.tvContactNumber.setOnClickListener {
            openCallDialPad(leadData.mobile.toString())
        }
        binding.includedContent.tvFullAddressValue.setOnClickListener {
            // openDirection()
        }

        /*binding.includedContent.btnAddTask.setOnClickListener {

            val intent = Intent(this, AddTaskActivity::class.java)
            intent.putExtra(Constants.LEAD_DETAILS, leadData)
            startActivity(intent)
        }*/
    }
    private fun getTechnicianList(resources: Resource<NewRequestResponse>) {
        when (resources.status) {
            Status.LOADING -> {
                binding.cpiLoading.visibility = View.VISIBLE

            }
            Status.ERROR -> {
                binding.cpiLoading.visibility = View.GONE
                //println("resources.error?.message.toString()"+resources.error?.message.toString())
                /* Utils.instance.popupPinUtil(this,
                     resources.error?.message.toString(),
                     "",
                     false)*/
            }
            else -> {
                binding.cpiLoading.visibility = View.GONE

                val data = resources.data

                data.let {
                    if(it?.success == true){
                        assignTechnicianListMsg = data?.message
                        techList.clear()
                        techList.addAll(it.data.data)
                    }else{
                        it?.message?.let { msg ->
                            // Utils.instance.popupUtil(this@CustomerDetailsActivity, msg, null, false)
                        }
                        val r = Runnable {
                            // barcodeView?.resume()
                        }
                        Handler().postDelayed(r, 1000)
                    }
                }.run {  }
            }
        }
    }


    private fun getCancelRequestLead(resources: Resource<CancelRequestResponse>) {
        when (resources.status) {
            Status.LOADING -> {
                binding.cpiLoading.visibility = View.VISIBLE
            }
            Status.ERROR -> {
                binding.cpiLoading.visibility = View.GONE
            }
            else -> {
                binding.cpiLoading.visibility = View.GONE
                val response = resources.data

                response?.let {
                    if (it.success) {

                        it.message?.toString()?.let { it1 ->
                            Utils.instance.popupUtil(this,
                                it1,
                                "",
                                true)
                        }
                        /*  try {
                              println("indexindex"+index)
                              *//*val listOfProducts = mutableListOf(leadData.enquiries)
                            for (i in listOfProducts.indices) {
                                listOfProducts.de(index!!)
                            }
*//*

                          //  val result = leadData.enquiries.toMutableList()
                            val arrayone: ArrayList<Enquiry> = leadData.enquiries
                            arrayone.removeAt(0)
                            val arraytwo: ArrayList<Enquiry> = arrayone
                            for (item in arraytwo) {
                                println(item)
                            }
                             //result.toIntArray()

                        if(leadData.enquiries.isEmpty()){

                        }else{

                        }

                        }catch (e: IllegalArgumentException){

                        }*/
                        // println("indexindex"+leadData.enquiries.removeAt(index!!))
                        //index?.let { it1 -> leadData.enquiries.removeAt(it1) }
                        // val refresh = Intent(this, CustomerDetailsActivity::class.java)
                        //startActivity(refresh)


                        Handler(Looper.getMainLooper()).postDelayed({
                            val intent = Intent(this, RequestLeadActivity::class.java)
                            startActivity(intent)
                            finish()
                        }, 5000)

                    } else {

                    }
                }
            }
        }
    }

    private fun openCallDialPad(contactNumber: String) {
        val i = Intent(Intent.ACTION_DIAL)
        val p = "tel:$contactNumber"
        i.data = Uri.parse(p)
        startActivity(i)
    }

    private fun goToFullScreenImageActivity(invoiceImage: String?) {


        if (CommonUtils.invoiceData.isEmpty()) {
            if(invoiceImage != null) {
                val intent = Intent(this, FullScreenImageActivity::class.java)
                intent.putExtra(Constants.IMAGE_URL, invoiceImage)
                startActivity(intent)
            }

        } else {
            for (i in CommonUtils.invoiceData) {
                if (i.position == index) {
                    val intent = Intent(this, FullScreenImageActivity::class.java)
                    intent.putExtra(Constants.IMAGE_URL, i.invoice_url)
                    startActivity(intent)
                }
            }
        }

        /*if(invoiceUrl.isNullOrEmpty()) {
            if(!invoiceImage.isNullOrEmpty()) {
                val intent = Intent(this, FullScreenImageActivity::class.java)
                intent.putExtra(Constants.IMAGE_URL, invoiceImage)
                startActivity(intent)
            }
        }else{
            if(CommonUtils.invoiceData[0].position == index) {
                val intent = Intent(this, FullScreenImageActivity::class.java)
                intent.putExtra(Constants.IMAGE_URL, invoiceUrl)
                startActivity(intent)
            }else{
                val intent = Intent(this, FullScreenImageActivity::class.java)
                intent.putExtra(Constants.IMAGE_URL, invoiceImage)
                startActivity(intent)
            }
        }*/
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                // onBackPressed()
                val intent = Intent(this, RequestLeadActivity::class.java)
                startActivity(intent)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun openDirection() {
        val dir: String
        if(!leadData.latitude.isNullOrEmpty() && !leadData.longitude.isNullOrEmpty()) {
            dir = leadData.latitude + "" + "," + "" + leadData.longitude
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

    /*override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == permissionCode) {
            if (grantResults.isNotEmpty() && grantResults.get(0) ==
                PackageManager.PERMISSION_GRANTED){
                    loadBottomSheetDialog()
                //Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show()
            } else {
                //Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show()
                Intent(ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:${this!!.packageName}")).apply {
                    addCategory(Intent.CATEGORY_DEFAULT)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(this)
                }
            }
        }
    }*/

    private var lastClickedPos: Int = 0
    private val onItemClickListener: (Int, View, ItemComplaintBinding) -> Unit = { position, view,binding ->
        pos = position.toString()
        index = position
        bindingAdapter = binding
        when (view.id) {
            R.id.img_Invoice_image -> {
                println("clicksssssssssssssssss"+"click")
                goToFullScreenImageActivity(leadData.enquiries[position].invoice_url)
            }
            R.id.iv_qr_code_image -> {
                // goToFullScreenImageActivity(leadData.enquiries[position].qr_image)
            }
            R.id.btn_upload_invoices -> {

                if (checkCameraPermission()) {
                    bindingAdapter = binding
                    loadBottomSheetDialog()
                }
            }

            R.id.edt_select_invoice_date ->{
                bindingAdapter = binding
                val tvDatePicker: TextView = view.findViewById(R.id.edt_select_invoice_date)
                if(/*leadData.enquiries[position].purchase_at == null &&*/ binding.edtSelectInvoiceDate.text.toString().isEmpty()) {
                    openDatePicker(tvDatePicker)
                }else{
                    openSelectedDatePicker(tvDatePicker)
                }

                binding.tvErrorInvoiceDate.visibility = GONE
            }
            R.id.img_close -> {
                bindingAdapter.edtSelectInvoiceDate.text = ""
            }

            R.id.btn_update -> {
                bindingAdapter = binding
                val selectedUnderWarranty: String
                if (binding.radiobtnYes.isChecked()) {
                    selectedUnderWarranty = "Yes"
                } else if(binding.radiobtnNo.isChecked){
                    selectedUnderWarranty = "No"
                }else if(binding.radiobtnNotSure.isChecked){
                    selectedUnderWarranty = "Not Sure"
                }else{
                    selectedUnderWarranty = "Not Sure"
                }
                hitUpdateRequest(binding,position,selectedUnderWarranty,view)
            }

            R.id.tv_verify_gst -> {
                hitVerifyApi(bindingAdapter.edtGstNumber.text.toString())
            }
            R.id.radiobtn_yes -> {

                bindingAdapter.liUpdate.visibility = VISIBLE

                bindingAdapter.liGenerateCancel.visibility = GONE
            }
            R.id.radiobtn_no -> {
                bindingAdapter.liUpdate.visibility = GONE
                bindingAdapter.liGenerateCancel.visibility = VISIBLE

            }
            R.id.radiobtn_not_sure -> {
                bindingAdapter.liUpdate.visibility = VISIBLE

                bindingAdapter.liGenerateCancel.visibility = GONE

            }
            R.id.btn_gst_cancel -> {
                showCancelEnquiryPopUp(position)
            }
            R.id.btn_gst_generate_chargeable -> {
                hitCancelTask("",  position, "2")
            }
            R.id.tv_remove_image -> {
                invoiceUrl = null
                Glide.with(bindingAdapter.imgInvoiceImage)
                    .load("")
                    .placeholder(R.drawable.ic_no_invoice)
                    .into(bindingAdapter.imgInvoiceImage)
                bindingAdapter.tvRemoveImage.visibility = GONE

                Constants.IMAGE_URL = ""
                val invoiceData = position.let { it1 ->
                    InvoiceData(
                        "",
                        it1
                    )
                }
                if (invoiceData != null) {
                    CommonUtils.invoiceData.add(invoiceData)
                }
            }
            R.id.tv_serviceable_warranty_parts -> {
                openDropDown()
            }
        }
    }

    private fun openDropDown() {

        dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.warranty_parts_list)
        val rv_complaint_list = dialog.findViewById(R.id.rv_warranty_parts_list) as RecyclerView
        val expandableListView = dialog.findViewById(R.id.expandableListView) as ExpandableListView
        val popup_img_close = dialog.findViewById(R.id.popup_img_close) as ImageView
        popup_img_close.setOnClickListener {
            dialog.dismiss()
        }

        rv_complaint_list.layoutManager = LinearLayoutManager(this)

        // This will pass the ArrayList to our Adapter
        if(warrantyPartsList.isNullOrEmpty()){
            val adapter = ServiceableWarrantryPartAdapter(leadData.enquiries[index!!].warranty_parts)
            rv_complaint_list.adapter = adapter
        }else {
            val adapter = ServiceableWarrantryPartAdapter(warrantyPartsList)
            rv_complaint_list.adapter = adapter
        }

        if(warrantyPartsList.isNullOrEmpty()) {
            val expandableListAdapter =
                CustomExpandableListAdapter(
                    this,
                    leadData.enquiries[index!!].warranty_parts,
                    expandableListView
                )
            expandableListView.setAdapter(expandableListAdapter)
        }else{
            val expandableListAdapter =
                CustomExpandableListAdapter(
                    this,
                    warrantyPartsList,
                    expandableListView
                )
            expandableListView.setAdapter(expandableListAdapter)
        }

        expandableListView.setOnGroupExpandListener { groupPosition ->


            // tv_not_covered_condition.setVisibility(View.VISIBLE);


        }

        expandableListView.setOnGroupCollapseListener { groupPosition ->

        }
        expandableListView.setOnChildClickListener { parent, v, groupPosition, childPosition, id ->

            false
        }
        dialog.show()
    }


    private fun showCancelEnquiryPopUp(position: Int) {

        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View = inflater.inflate(R.layout.cancel_lead_popup_layout, null)
        val tv_cancel_lead_cancel = view.findViewById<TextView>(R.id.tv_cancel_lead_cancel)
        val tv_cancel_lead_ok = view.findViewById<TextView>(R.id.tv_cancel_lead_ok)
        val et_cancel_lead_reason = view.findViewById<EditText>(R.id.et_cancel_lead_reason)
        alertDialogBuilder = Dialog(this@CustomerDetailsActivity)
        alertDialogBuilder.setContentView(view)
        //alertDialogBuilder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialogBuilder.setCancelable(false)
        tv_cancel_lead_cancel.setOnClickListener {
            alertDialogBuilder.dismiss()
        }
        tv_cancel_lead_ok.setOnClickListener {
            if (Utils.instance.validateReason(et_cancel_lead_reason)

            ) {
                hitCancelTask(et_cancel_lead_reason.text.toString(), position, "1")
                alertDialogBuilder.dismiss()
            }
        }
        alertDialogBuilder.show()

    }

    private fun hitCancelTask(etCancelLeadReason: String?,position:Int,type:String) {

        val jsonObject = JsonObject()

        try {
            //  val jsArray  = JsonArray()
            jsonObject.addProperty("lead_cancelled_reason", etCancelLeadReason.toString())
            jsonObject.addProperty("lead_id", leadData.id)
            jsonObject.addProperty("enquiry_id", leadData.enquiries[position].id)
            jsonObject.addProperty("cancellation_type", type)

        } catch (e: JSONException) {
            e.printStackTrace()
        }

        customerDetailsViewModel.hitTaskCancelRequest(jsonObject)
            .observe(this, this::getCancelRequestLead)

    }

    private fun hitVerifyApi(gstNum: CharSequence?) {

        customerDetailsViewModel.verifyGstNum(gstNum.toString())
    }

    private fun hitUpdateRequest(binding: ItemComplaintBinding,position: Int,selectedUnderWarranty: String,view: View) {

        if (Utils.instance.validateDescription(binding.tvServiceCenterDescriptionValue) //&&
        //Utils.instance.validateInvoice(binding.edtInvoiceNumberValue) &&
        //Utils.instance.validateDate(binding.edtSelectInvoiceDate) &&
        //Utils.instance.validateGSTNum(binding.edtGstNumber)
        // Utils.instance.validateWarranty(binding,view)
        // Utils.instance.validateImage(binding.ivInvoiceImage,view)
        ) {
            //validateImage(position)
            val jsonObject = JsonObject()

            try {
                //  val jsArray  = JsonArray()
                if (CommonUtils.invoiceData.isEmpty()) {
                    jsonObject.addProperty(
                        "invoice_url",
                        leadData.enquiries.get(position).invoice_url
                    )
                } else {
                    val arraylist = ArrayList<String>()
                    var selectedPosInvoice : String = ""
                    for(i in CommonUtils.invoiceData){
                        if(i.position == index){
                            //  arraylist.add(i.invoice_url)
                            selectedPosInvoice = i.invoice_url
                        }else {
                            if (leadData.enquiries.get(position).invoice_url != null) {
                                selectedPosInvoice = leadData.enquiries.get(position).invoice_url!!
                            }else{
                                selectedPosInvoice = ""
                            }
                        }
                    }
                    jsonObject.addProperty("invoice_url", selectedPosInvoice)
                }

                jsonObject.addProperty("purchase_at", binding.edtSelectInvoiceDate.text.toString())
                jsonObject.addProperty("in_warranty", selectedUnderWarranty)
                jsonObject.addProperty("invoice_no", binding.edtInvoiceNumberValue.text.toString().trim())
                jsonObject.addProperty("buyer_name", binding.edtBuyerName.text.toString().trim())
                jsonObject.addProperty("seller_gst_no", binding.edtGstNumber.text.toString().trim())
                jsonObject.addProperty("seller_name", binding.tvGstFirstName.text.toString().trim())
                jsonObject.addProperty("seller_trade_name", binding.tvGstTradeName.text.toString().trim())
                jsonObject.addProperty(
                    "service_center_discription",
                    binding.tvServiceCenterDescriptionValue.text.toString().trim()
                )
            } catch (e: JSONException) {
                e.printStackTrace()
            }

            println("jsonObjectjsonObject"+jsonObject)
            println("leadDataleadData"+leadData.id + leadData.enquiries.get(position).id)
            customerDetailsViewModel.hitUpdateRequest(
                jsonObject,
                leadData.id,
                leadData.enquiries.get(position).id
            ).observe(this@CustomerDetailsActivity, this::onUpdateRequest)
        }
    }

    fun validateImage(position: Int) {

        if(invoiceUrl == null /*&& leadData.enquiries[position].invoice_url == null*/){
            //Toast.makeText(getApplicationContext(), "Please upload invoice", Toast.LENGTH_SHORT).show();

            Utils.instance.popupPinUtil(this@CustomerDetailsActivity,
                "Please Upload Invoice Image!",
                "",
                false)
        }else{

        }
    }

    private fun onUpdateRequest(resources: Resource<UpdateRequestResponse>) {
        when (resources.status) {
            Status.LOADING -> {
                binding.cpiLoading.visibility = View.VISIBLE

            }
            Status.ERROR -> {
                binding.cpiLoading.visibility = View.GONE

            }
            else -> {
                binding.cpiLoading.visibility = View.GONE
                val response = resources.data

                response?.let {
                    if (it.success) {
                        val massage = Html.fromHtml(it.message)
                        println("massage"+massage)
                        Utils.instance.popupPinUtil(this@CustomerDetailsActivity,
                            massage.toString(),
                            "",
                            true)

                        showData(response)

                    } else {
                        Utils.instance.popupPinUtil(this@CustomerDetailsActivity,
                            it.message,
                            "",
                            false)

                    }
                }
            }
        }
    }

    private fun showData(data: UpdateRequestResponse) {
        if ((data.data.detail_status.toString()) == "1") {
            bindingAdapter.imgUpdatedTask.visibility = View.VISIBLE
            //bindingAdapter.btnHideUpdate.visibility = View.VISIBLE
            //bindingAdapter.btnUpdate.visibility = View.GONE
            /* if(data.data.purchase_at != null) {
                 bindingAdapter.tvServiceableWarrantyParts.visibility = VISIBLE
             }else{
                 bindingAdapter.tvServiceableWarrantyParts.visibility = GONE
             }
             warrantyPartsList.clear()
             warrantyPartsList.add(data.data.warranty_parts.get(index!!))*/

            //if(data.data.purchase_at != null){
                if(!data.data.warranty_parts.isEmpty()){
                    warrantyPartsList.clear()
                   // warrantyPartsList.add(data.data.warranty_parts.get(index!!))
                    bindingAdapter.tvServiceableWarrantyParts.visibility = VISIBLE
                }else {
                    bindingAdapter.tvServiceableWarrantyParts.visibility = GONE
                }
            /*}else{
                bindingAdapter.tvServiceableWarrantyParts.visibility = GONE
            }*/

        }else{
            bindingAdapter.imgUpdatedTask.visibility = View.GONE
            bindingAdapter.tvServiceableWarrantyParts.visibility = GONE
            // bindingAdapter.btnHideUpdate.visibility = View.GONE
            // bindingAdapter.btnUpdate.visibility = View.VISIBLE
        }

        /*if(!data.data.warranty_parts.isEmpty()){
            warrantyPartsList.clear()
            warrantyPartsList.add(data.data.warranty_parts.get(index!!))
            bindingAdapter.tvServiceableWarrantyParts.visibility = VISIBLE
        }else {
            bindingAdapter.tvServiceableWarrantyParts.visibility = GONE
        }*/

        if(data.data.pending_lead_enqury_detail_count == "0" /*&& data.data.in_warranty_enquiries_count > "0"*/){
            binding.includedContent.btnAssignTechnician.visibility = View.VISIBLE
            binding.includedContent.btnAssignTechnicianHide.visibility = View.GONE

        }else{
            binding.includedContent.btnAssignTechnician.visibility = View.GONE
            binding.includedContent.btnAssignTechnicianHide.visibility = View.VISIBLE
        }
    }

    private fun openDatePicker(tvDatePicker: TextView) {
        /*val calendar: Calendar = Calendar.getInstance()
        val  dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            calendar[Calendar.YEAR] = year
            calendar[Calendar.MONTH] = month
            calendar[Calendar.DAY_OF_MONTH] = dayOfMonth*/
            val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy")


            val calendar = Calendar.getInstance()
            val year = calendar[Calendar.YEAR]
            val month = calendar[Calendar.MONTH]
            val day = calendar[Calendar.DAY_OF_MONTH]
          //  tvDatePicker.setText(simpleDateFormat.format(calendar.time))
          //  bindingAdapter.tvErrorInvoiceDate.visibility = GONE
      //  }

        /*val datePickerDialog = DatePickerDialog(
            this@CustomerDetailsActivity, dateSetListener, calendar
                .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )*/
        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            tvDatePicker.setText(dayOfMonth.toString() + "/" + (month+1) + "/" + year)
            bindingAdapter.tvErrorInvoiceDate.visibility = GONE
            bindingAdapter.imgClose.visibility = VISIBLE
        }

        val datePickerDialog = DatePickerDialog(
            this,
            AlertDialog.THEME_HOLO_LIGHT, dateSetListener, year, month, day
        )


        //following line to restrict future date selection
        //following line to restrict future date selection
        datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
        datePickerDialog.show()

    }

    private fun openSelectedDatePicker(tvDatePicker: TextView) {

        val calendar: Calendar = Calendar.getInstance()
        val calendarEnd: Calendar = Calendar.getInstance()


        /*val year = calendar[Calendar.YEAR]
        val month = calendar[Calendar.MONTH]
        val day = calendar[Calendar.DAY_OF_MONTH]
*/

       // val date = leadData.enquiries[index!!].purchase_at
        val date =bindingAdapter.edtSelectInvoiceDate.text.toString()
        val selectedDate = date.split("/")

        var dayofMonth = selectedDate.get(0)
        var months = selectedDate.get(1)
        var years = selectedDate.get(2)


        /*val  dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            if (years != null) {
                calendar[Calendar.YEAR] = years. toInt()
            }
            if (months != null) {
                calendar[Calendar.MONTH] = months.toInt()
            }
            calendar[Calendar.DAY_OF_MONTH] = dayofMonth!!.toInt()
            val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy")

            tvDatePicker.setText(simpleDateFormat.format(calendar.time))
            bindingAdapter.tvErrorInvoiceDate.visibility = GONE
        }
*/

        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->

            if (years != null) {
                calendar[Calendar.YEAR] = years. toInt()
            }
            if (months != null) {
                calendar[Calendar.MONTH -1] = months.toInt()
            }
            calendar[Calendar.DAY_OF_MONTH] = dayofMonth!!.toInt()

            //val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy")
           // tvDatePicker.setText(simpleDateFormat.format(calendar.time))
            tvDatePicker.setText(dayOfMonth.toString() + "/" + (month+1) + "/" + year)
            bindingAdapter.tvErrorInvoiceDate.visibility = GONE
            bindingAdapter.imgClose.visibility = VISIBLE
        }

        /*val datePickerDialog = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, myear, mmonth, mdayOfMonth ->

           tvDatePicker.setText(""+calendar[Calendar.DAY_OF_MONTH]  +"/"+ calendar[Calendar.MONTH]  +"/"+ calendar[Calendar.YEAR])
         // tvDatePicker.setText(""+mdayOfMonth +"/"+ mmonth  +"/"+ myear)
        }, years!!.toInt(), (months!!.toInt()), dayofMonth!!.toInt())
*/


        val datePickerDialog = DatePickerDialog(
            this,
            AlertDialog.THEME_HOLO_LIGHT, dateSetListener, years!!.toInt(), (months!!.toInt()-1), dayofMonth!!.toInt()
        )

        datePickerDialog.show()
        //following line to restrict future date selection
        //following line to restrict future date selection
         datePickerDialog.datePicker.maxDate = calendarEnd.timeInMillis


    }

    private fun loadBottomSheetDialog() {
        val fragment = CameraBottomSheetDialogFragment().newInstance()
        fragment.bottomSheetItemClick = this@CustomerDetailsActivity
        fragment.show(supportFragmentManager, "Bottomsheet_Media_Selection")
    }

    private fun checkCameraPermission(): Boolean {
        /*return  if ((ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) || (ContextCompat.checkSelfPermission(
                this, Manifest.permission.READ_EXTERNAL_STORAGE
            )
                    != PackageManager.PERMISSION_GRANTED) || (ContextCompat.checkSelfPermission(
                this, Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
                    != PackageManager.PERMISSION_GRANTED)
        ) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.CAMERA
                )
                || ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
                || ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            ) {
                AlertDialog.Builder(this)
                    .setTitle("Need External Permission")
                    .setMessage("We need external access permission for uploading your image")
                    .setPositiveButton(
                        "ok"
                    ) { dialogInterface: DialogInterface?, i: Int ->
                        //Prompt the user once explanation has been shown
                        requestPermissions()

                    }.create().show()
            } else {
                println("NOTclicked" + "NOTclicked")
                requestPermissions()
                *//*Intent(
                    ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.parse("package:${this!!.packageName}")
                ).apply {
                    addCategory(Intent.CATEGORY_DEFAULT)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(this)
                }*//*
                // No explanation needed, we can request the permission.
                //requestPermissions()
            }
            false
        } else {
            true
        }*/

        return if ((ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) || (ContextCompat.checkSelfPermission(
                this, Manifest.permission.READ_EXTERNAL_STORAGE
            )
                    != PackageManager.PERMISSION_GRANTED) || (ContextCompat.checkSelfPermission(
                this, Manifest.permission.WRITE_EXTERNAL_STORAGE
            )

                    != PackageManager.PERMISSION_GRANTED)
        ) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.CAMERA
                )
                || ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
                || ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )

            ) {
                AlertDialog.Builder(this)
                    .setTitle("Need External Permission")
                    .setMessage("We need external access permission for uploading your image")
                    .setPositiveButton(
                        "ok"
                    ) { dialogInterface: DialogInterface?, i: Int ->
                        //Prompt the user once explanation has been shown
                        requestPermissions()

                    }.create().show()
            } else {
                // No explanation needed, we can request the permission.
                // requestPermissions()


                if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_DENIED){
                    println("PERMISSION_DENIED" + "PERMISSION_DENIED")

                    requestPermissions()

                    AlertDialog.Builder(this)
                        .setTitle("Need External Permission")
                        .setMessage("We need external access permission for uploading your image")
                        .setPositiveButton(
                            "ok"
                        ) { dialogInterface: DialogInterface?, i: Int ->
                            //Prompt the user once explanation has been shown
                            Intent(
                                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                Uri.parse("package:${this!!.packageName}")
                            ).apply {
                                addCategory(Intent.CATEGORY_DEFAULT)
                                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                startActivity(this)
                            }

                        }.create().show()
                }else{
                    println("GRANTED" + "GRANTED")
                    loadBottomSheetDialog()
                }
            }
            false
        } else {
            true
        }
    }


    @SuppressLint("QueryPermissionsNeeded")
    private fun startImageCapture() {
       /* val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
        if (intent.resolveActivity(this.packageManager) != null) {
            var photoFile: File? = null
            try {
                photoFile = createImageFile()
            } catch (ex: IOException) {
                // Error occurred while creating the File
                ex.printStackTrace()
            }
            if (photoFile != null) {
                try {
                    val photoURI = FileProvider.getUriForFile(
                        this,
                        BuildConfig.APPLICATION_ID+".fileprovider",
                        photoFile
                    )
                    val resInfoList = this.packageManager.queryIntentActivities(
                        intent,
                        PackageManager.MATCH_DEFAULT_ONLY
                    )
                    for (resolveInfo in resInfoList) {
                        val packageName = resolveInfo.activityInfo.packageName
                        this.grantUriPermission(
                            packageName,
                            photoURI,
                            Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION
                        )
                    }
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startForResultCamera.launch(intent)
                    //startActivityForResult(intent, CUSTOM_REQUEST_CODE)
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            }
        }*/

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED || checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_DENIED) {
                val permission = arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                requestPermissions(permission, 121)
            } else {
                openCamera()
            }
        } else {
            openCamera()
        }
        true
    }

    private fun openCamera() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "New Picture")
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera")
        image_uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri)
        startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_CAPTURE_CODE && resultCode == Activity.RESULT_OK) {
            //imageView.setImageURI(image_uri);
            val bitmap = uriToBitmap(image_uri!!)
            //binding.liUploadFileValue.visibility = VISIBLE
            //binding.uploadedImg.setImageBitmap(bitmap)
        }
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            image_uri = data.data
            //imageView.setImageURI(image_uri);
            val bitmap = uriToBitmap(image_uri!!)
            //binding.liUploadFileValue.visibility = VISIBLE
            // binding.uploadedImg?.setImageBitmap(bitmap)
        }
    }

    private fun uriToBitmap(selectedFileUri: Uri): Bitmap? {
        try {
            val parcelFileDescriptor = contentResolver.openFileDescriptor(selectedFileUri, "r")
            val fileDescriptor: FileDescriptor = parcelFileDescriptor!!.fileDescriptor
            val image = BitmapFactory.decodeFileDescriptor(fileDescriptor)

            resultUri = Utils.instance.reSizeImg(image,this)
            //val resultUri = Utils.instance.getCompressedBitmap(image)
            println("resultUri"+resultUri)

            buildMutilPart()


            //parcelFileDescriptor.close()
            return image
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }

    override fun bottomSheetItemClick(clickAction: String?) {
        when (clickAction) {
            CAMERA -> {
                //  barcodeView?.pause()
                startImageCapture()
            }
            GALLERY -> {
                getImageGallery()
            }
            CANCEL -> {
            }
        }
    }
    @Throws(IOException::class)
    private fun createImageFile(): File? {

        val imageFileName = "orpatservice_"
        //val storageDir = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "ALCS"+ File.separator + "LaneShots")
        val storageDir = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(
            imageFileName,  /* prefix */
            ".jpg",  /* suffix */
            storageDir  /* directory */
        )
        // Save a file: path for use with ACTION_VIEW intents
        mCurrentCaptureImage = image.absolutePath
        return image
    }

    private val startForResultCamera =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val intent = result.data
                resultUri = Uri.fromFile(File(mCurrentCaptureImage ?: ""))
                setImageOnView()
                // Handle the Intent
                //do stuff here
            }
        }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btn_assign_technician -> {
                if (SharedPrefs.getInstance().getString(Constants.USER_TYPE, "")
                        .equals(Constants.SERVICE_CENTER)
                ) {
                    val intent = Intent(this, TechniciansActivity::class.java)

                    intent.putExtra(Constants.IS_NAV, Constants.ComingFrom.CUSTOMER_DETAILS)
                    intent.putExtra(Constants.LEADS_ID, leadData.id)
                    startActivity(intent)

                } else if (SharedPrefs.getInstance().getString(Constants.USER_TYPE, "")
                        .equals(Constants.TECHNICIAN)
                ) {
                    val intent = Intent(this, CloseComplaintActivity::class.java)
                    intent.putExtra(Constants.IS_NAV, Constants.ComingFrom.CUSTOMER_DETAILS)
                    intent.putExtra(Constants.COMPLAINT_ID, leadData.id)
                    closeComplaintLauncher.launch(intent)
                }
            }
            R.id.iv_call -> {
                openCallDialPad(leadData.mobile.toString())
            }
        }
    }

    private fun getImageGallery() {
        /*val chosePhoto =
            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        chosePhoto.type = "image/*"
        startForResultGallery.launch(chosePhoto)*/
         */

        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE)

    }
    private val startForResultGallery =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                //val intent = result.data.data
                resultUri = result.data?.data
                setImageOnView()

                // Handle the Intent
                //do stuff here
            }
        }

    private fun setImageOnView() {
        try {
            val parcelFileDescriptor = this.contentResolver.openFileDescriptor(
                resultUri ?: Uri.EMPTY, "r"
            )
            val fileDescriptor: FileDescriptor =
                parcelFileDescriptor?.fileDescriptor ?: FileDescriptor()
            val image = BitmapFactory.decodeFileDescriptor(fileDescriptor)
            parcelFileDescriptor?.close()

            resultUri = Utils.instance.reSizeImg(image,this)


            //validationUtil()

        } catch (ex: java.lang.Exception) {
            ex.printStackTrace()
        }
        buildMutilPart()
        //binding.includedContent.ivUploadImage.setImageURI(resultUri)
        //binding.ivSelectedImage.setImageURI(resultUri)
    }

    private fun buildMutilPart(){
        val params = MultipartBody.Builder().setType(MultipartBody.FORM)


        if (!resultUri?.path.isNullOrBlank()){
            val newUri = Uri.parse(resultUri?.path)
            val files = File(newUri.path ?: "")
            val requestFile: RequestBody = files.asRequestBody("multipart/form-data".toMediaType())
            params.addFormDataPart("file", files.name, requestFile)

            // binding.btnUploadInvoiceFile.visibility = View.VISIBLE
            // binding.btnUploadInvoiceFile.setText(" ${files.name}")

        }
        customerDetailsViewModel.hitServiceCenterUploadFile(
            params.build(),
        )
    }
    private fun onFileUploaded(resources: Resource<UploadFileResponse>) {
        when (resources.status) {
            Status.LOADING -> {
                binding.cpiLoading.visibility = View.VISIBLE

            }
            Status.ERROR -> {
                binding.cpiLoading.visibility = View.GONE

                /* Utils.instance.popupPinUtil(this@CustomerDetailsActivity,
                     resources.error?.message.toString(),
                     "",
                     false)*/
            }
            else -> {
                binding.cpiLoading.visibility = View.GONE

                val data = resources.data

                data.let {
                    if(it?.success == true){
                        invoiceUrl = it.data.invoice_url

                        Utils.instance.popupPinUtil(this@CustomerDetailsActivity,
                            it.message,
                            "",
                            true)

                        Glide.with(bindingAdapter.imgInvoiceImage)
                            .load(invoiceUrl)
                            //  .diskCacheStrategy(DiskCacheStrategy.ALL)
                            //.circleCrop() // .error(R.drawable.active_dot)
                            .placeholder(R.drawable.ic_no_invoice)
                            .into(bindingAdapter.imgInvoiceImage)
                       // CommonUtils.invoiceData.clear()

                        val invoiceData = index?.let { it1 ->
                            InvoiceData(
                                invoiceUrl!!,
                                it1
                            )
                        }

                        if (invoiceData != null) {
                            CommonUtils.invoiceData.add(invoiceData)
                        }

                        bindingAdapter.tvRemoveImage.visibility = VISIBLE

                    }else{
                        it?.message?.let { msg ->
                            // Utils.instance.popupUtil(this@CustomerDetailsActivity, msg, null, false)
                        }
                        val r = Runnable {
                            // barcodeView?.resume()
                        }
                        Handler().postDelayed(r, 1000)
                    }
                }.run {  }
            }
        }
    }


    private fun getVerifyData(resources: Resource<VerifyGSTRequestData>) {
        when (resources.status) {
            Status.LOADING -> {
                binding.cpiLoading.visibility = View.VISIBLE

            }
            Status.ERROR -> {
                binding.cpiLoading.visibility = View.GONE

                /*  Utils.instance.popupPinUtil(this@CustomerDetailsActivity,
                      resources.error?.message.toString(),
                      "",
                      false)*/
            }
            else -> {
                binding.cpiLoading.visibility = View.GONE

                val data = resources.data

                data.let {
                    if(it?.success == true){

                        Utils.instance.popupPinUtil(this@CustomerDetailsActivity,
                            it.message,
                            "",
                            true)

                        bindingAdapter.tvGstName.visibility = VISIBLE
                        bindingAdapter.tvGstFirstName.visibility = VISIBLE
                        bindingAdapter.tvGstTrade.visibility = VISIBLE
                        bindingAdapter.tvGstTradeName.visibility = VISIBLE
                        bindingAdapter.tvErrorGstNumber.visibility = GONE

                        bindingAdapter.tvGstFirstName.text = data?.data?.firm_name.toString()
                        bindingAdapter.tvGstTradeName.text = data?.data?.trade_name.toString()


                    }else{
                        it?.message?.let { msg ->
                             Utils.instance.popupUtil(this@CustomerDetailsActivity, msg, null, false)
                        }

                        bindingAdapter.tvGstName.visibility = GONE
                        bindingAdapter.tvGstFirstName.visibility = GONE
                        bindingAdapter.tvGstTrade.visibility = GONE
                        bindingAdapter.tvGstTradeName.visibility = GONE
                        bindingAdapter.tvErrorGstNumber.visibility = GONE

                        bindingAdapter.tvGstFirstName.text = ""
                        bindingAdapter.tvGstTradeName.text = ""
                        /*val r = Runnable {
                            // barcodeView?.resume()
                        }
                        Handler().postDelayed(r, 1000)*/
                    }
                }.run {  }
            }
        }
    }

    private var closeComplaintLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                //setObserver()
                // leadData.pending_lead_enquiries =
                // leadData.pending_lead_enquiries?.toInt()?.minus(1).toString()
                //  leadData.enquiries[lastClickedPos].status = true

                complaintAdapter.notifyItemChanged(lastClickedPos)
            }
        }

   /* override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        textview.setText(day + ":" + (month+1) + ":" + year);
    }*/
}

