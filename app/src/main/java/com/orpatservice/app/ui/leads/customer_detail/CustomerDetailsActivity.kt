package com.orpatservice.app.ui.leads.customer_detail

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.EditText
import android.widget.TextView
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.gson.JsonObject
import com.orpatservice.app.BuildConfig
import com.orpatservice.app.R
import com.orpatservice.app.data.Resource
import com.orpatservice.app.data.Status
import com.orpatservice.app.data.model.requests_leads.Enquiry
import com.orpatservice.app.data.model.requests_leads.LeadData
import com.orpatservice.app.data.model.requests_leads.RequestLeadResponse
import com.orpatservice.app.data.sharedprefs.SharedPrefs
import com.orpatservice.app.databinding.ActivityCustomerDetailsBinding
import com.orpatservice.app.databinding.ItemComplaintBinding
import com.orpatservice.app.ui.admin.technician.*
import com.orpatservice.app.ui.leads.adapter.ComplaintAdapter
import com.orpatservice.app.ui.leads.new_lead_fragment.new_lead_request.NewRequestResponse
import com.orpatservice.app.ui.leads.new_lead_fragment.new_lead_request.RequestData
import com.orpatservice.app.ui.leads.new_lead_fragment.new_lead_request.UpdatePartsRequestData
import com.orpatservice.app.ui.leads.new_lead_fragment.new_lead_request.VerifyGSTRequestData
import com.orpatservice.app.ui.leads.service_center.RequestLeadActivity
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
import java.lang.IllegalArgumentException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by Vikas Singh on 26/12/21.
 */

const val MY_PERMISSIONS_WRITE_READ_REQUEST_CODE = 1000
class CustomerDetailsActivity : AppCompatActivity(), View.OnClickListener, CameraBottomSheetDialogFragment.BottomSheetItemClick {

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

        bindUserDetails(leadData)
        setObserver()

        customerDetailsViewModel.assignTechnicianLead()
        //customerDetailsViewModel.loadAssignedTechnicianLeads(pageNumber)

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
       customerDetailsViewModel.loadTechnicianLeads(pageNumber,leadData.id!!)
    }

    private fun bindUserDetails(leadData: LeadData) {
        binding.includedContent.tvCustomerNameValue.text = leadData.name
        binding.includedContent.tvContactNumberValue.text = leadData.mobile
        binding.includedContent.tvPinCodeValue.text = leadData.pincode
        binding.includedContent.tvFullAddressValue.text = leadData.address1+""+" , "+""+leadData.address2/*+""+", "+""+leadData.state*/
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
                //    showLoadingUI()

            }
            Status.ERROR -> {

                Utils.instance.popupPinUtil(this,
                    resources.error?.message.toString(),
                    "",
                    false)
            }
            else -> {
                // hideLoadingUI()

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
        if(invoiceUrl.isNullOrEmpty()) {
            val intent = Intent(this, FullScreenImageActivity::class.java)
            intent.putExtra(Constants.IMAGE_URL, invoiceImage)
            startActivity(intent)
        }else{
            val intent = Intent(this, FullScreenImageActivity::class.java)
            intent.putExtra(Constants.IMAGE_URL, invoiceUrl)
            startActivity(intent)
        }
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

    private var lastClickedPos: Int = 0
    private val onItemClickListener: (Int, View, ItemComplaintBinding) -> Unit = { position, view,binding ->
        pos = position.toString()
        index = position
        bindingAdapter = binding
        when (view.id) {
            R.id.iv_invoice_image -> {
                goToFullScreenImageActivity(leadData.enquiries[position].invoice_url)
            }
            R.id.iv_qr_code_image -> {
               // goToFullScreenImageActivity(leadData.enquiries[position].qr_image)
            }
            R.id.btn_upload_invoice -> {
                if (checkCameraPermission()) {
                    bindingAdapter = binding
                    loadBottomSheetDialog()
                }
            }
            R.id.edt_select_invoice_date ->{
                bindingAdapter = binding
                val tvDatePicker: TextView = view.findViewById(R.id.edt_select_invoice_date)
                openDatePicker(tvDatePicker)

                binding.tvErrorInvoiceDate.visibility = GONE
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
            R.id.btn_gst_cancel -> {
                showCancelEnquiryPopUp(position)
            }
            R.id.btn_gst_generate_chargeable -> {
                hitCancelTask("",  position, "2")
            }
        }
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
             validateImage(position)
            val jsonObject = JsonObject()

            try {
                //  val jsArray  = JsonArray()
                if (invoiceUrl == null) {
                    jsonObject.addProperty(
                        "invoice_url",
                        leadData.enquiries.get(position).invoice_url
                    )
                } else {
                    jsonObject.addProperty("invoice_url", invoiceUrl)
                }

                jsonObject.addProperty("purchase_at", binding.edtSelectInvoiceDate.text.toString())
                jsonObject.addProperty("in_warranty", selectedUnderWarranty)
                jsonObject.addProperty("invoice_no", binding.edtInvoiceNumberValue.text.toString().trim())
                jsonObject.addProperty(
                    "service_center_discription",
                    binding.tvServiceCenterDescriptionValue.text.toString().trim()
                )
            } catch (e: JSONException) {
                e.printStackTrace()
            }

            println("jsonObjectjsonObject"+jsonObject)
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

                       /* Alerter.create(this@CustomerDetailsActivity)
                            .setTitle("")
                            .setText("" + it.message?.toString())
                            .setBackgroundColorRes(R.color.orange)
                            .setDuration(1000)
                            .show()*/

                        Utils.instance.popupPinUtil(this@CustomerDetailsActivity,
                            "Task Update Successfully",
                            "",
                            true)

                        showData(response)

                    } else {

                    }
                }
            }
        }
    }

    private fun showData(data: UpdateRequestResponse) {
        if (data.data.detail_status == "1") {
            bindingAdapter.btnHideUpdate.visibility = View.VISIBLE
            bindingAdapter.btnUpdate.visibility = View.GONE
        }else{
            bindingAdapter.btnHideUpdate.visibility = View.GONE
            bindingAdapter.btnUpdate.visibility = View.VISIBLE
        }
        println("pending_lead_enqury_detail_count"+data.data.pending_lead_enqury_detail_count)
        println("in_warranty_enquiries_count"+data.data.in_warranty_enquiries_count)
        if(data.data.pending_lead_enqury_detail_count == "0" /*&& data.data.in_warranty_enquiries_count > "0"*/){
            binding.includedContent.btnAssignTechnician.visibility = View.VISIBLE
            binding.includedContent.btnAssignTechnicianHide.visibility = View.GONE

        }else{
            binding.includedContent.btnAssignTechnician.visibility = View.GONE
            binding.includedContent.btnAssignTechnicianHide.visibility = View.VISIBLE
        }
    }

    private fun openDatePicker(tvDatePicker: TextView) {
        val calendar: Calendar = Calendar.getInstance()
        val  dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                calendar[Calendar.YEAR] = year
                calendar[Calendar.MONTH] = month
                calendar[Calendar.DAY_OF_MONTH] = dayOfMonth
                val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy")

                tvDatePicker?.setText(simpleDateFormat.format(calendar.time))
                bindingAdapter.tvErrorInvoiceDate.visibility = GONE
            }

            val datePickerDialog = DatePickerDialog(
                this@CustomerDetailsActivity, dateSetListener, calendar
                    .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            //following line to restrict future date selection

            //following line to restrict future date selection
            datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
            datePickerDialog.show()

    }

    private fun loadBottomSheetDialog() {
        val fragment = CameraBottomSheetDialogFragment().newInstance()
        fragment.bottomSheetItemClick = this@CustomerDetailsActivity
        fragment.show(supportFragmentManager, "Bottomsheet_Media_Selection")
    }

    private fun checkCameraPermission(): Boolean {
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
                requestPermissions()
            }
            false
        } else {
            true
        }
    }
    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this, arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ), MY_PERMISSIONS_WRITE_READ_REQUEST_CODE
        )
    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun startImageCapture() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
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
        }
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
        val chosePhoto =
            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        chosePhoto.type = "image/*"
        startForResultGallery.launch(chosePhoto)

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

            resultUri = Utils.instance.reSizeImg(image)

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
            //    showLoadingUI()

            }
            Status.ERROR -> {
              //  hideLoadingUI()

                Utils.instance.popupPinUtil(this@CustomerDetailsActivity,
                    resources.error?.message.toString(),
                    "",
                    false)
            }
            else -> {
               // hideLoadingUI()

                val data = resources.data

                data.let {
                    if(it?.success == true){
                        invoiceUrl = it.data.invoice_url

                        Utils.instance.popupPinUtil(this@CustomerDetailsActivity,
                            it.message,
                            "",
                            true)

                        Glide.with(bindingAdapter.ivInvoiceImage)
                            .load(invoiceUrl)
                          //  .diskCacheStrategy(DiskCacheStrategy.ALL)
                            //.circleCrop() // .error(R.drawable.active_dot)
                            .placeholder(R.color.gray)
                            .into(bindingAdapter.ivInvoiceImage)

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
                //    showLoadingUI()

            }
            Status.ERROR -> {
                //  hideLoadingUI()

                Utils.instance.popupPinUtil(this@CustomerDetailsActivity,
                    resources.error?.message.toString(),
                    "",
                    false)
            }
            else -> {
                // hideLoadingUI()

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
}

