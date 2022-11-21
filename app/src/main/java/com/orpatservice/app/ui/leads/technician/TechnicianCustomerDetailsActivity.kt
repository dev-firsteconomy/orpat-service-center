package com.orpatservice.app.ui.leads.technician

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityManager
import android.app.AlertDialog
import android.app.Dialog
import android.content.ContentValues
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.*
import android.provider.MediaStore
import android.provider.Settings
import android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.Window
import android.widget.*
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.zxing.BarcodeFormat
import com.google.zxing.ResultPoint
import com.google.zxing.client.android.BeepManager
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import com.journeyapps.barcodescanner.DecoratedBarcodeView
import com.journeyapps.barcodescanner.DefaultDecoderFactory
import com.orpatservice.app.BuildConfig
import com.orpatservice.app.R
import com.orpatservice.app.data.Resource
import com.orpatservice.app.data.Status
import com.orpatservice.app.data.model.requests_leads.ImageListData
import com.orpatservice.app.data.model.requests_leads.WarrantryPart
import com.orpatservice.app.data.sharedprefs.SharedPrefs
import com.orpatservice.app.databinding.ActivityTechnicianCustomerDetailsBinding
import com.orpatservice.app.databinding.ItemTechnicianComplaintBinding
import com.orpatservice.app.ui.admin.technician.CAMERA
import com.orpatservice.app.ui.admin.technician.CANCEL
import com.orpatservice.app.ui.leads.customer_detail.*
import com.orpatservice.app.ui.leads.customer_detail.adapter.ServiceableWarrantryPartAdapter
import com.orpatservice.app.ui.leads.new_lead_fragment.adapter.CustomExpandableListAdapter
import com.orpatservice.app.ui.leads.technician.adapter.TechnicianCustomerDetailsAdapter
import com.orpatservice.app.ui.leads.technician.response.TechnicianEnquiryImage
import com.orpatservice.app.ui.leads.technician.response.TechnicianLeadData
import com.orpatservice.app.ui.leads.technician.response.TechnicianRequestLeadResponse
import com.orpatservice.app.ui.leads.technician.section.CameraBottomDialogFragment
import com.orpatservice.app.ui.leads.technician.section.EnquirySliderScreenImageActivity
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
import java.util.*

const val MY_PERMISSIONS_WRITE_READ_REQUEST_CODE = 1000
class TechnicianCustomerDetailsActivity : AppCompatActivity(), View.OnClickListener,
    CameraBottomDialogFragment.BottomSheetItemClick {

    companion object {
        private const val REQUEST_CAMERA_PERMISSION = 10
    }
    private lateinit var binding: ActivityTechnicianCustomerDetailsBinding
    private lateinit var leadData: TechnicianLeadData
    private lateinit var complaintAdapter: TechnicianCustomerDetailsAdapter
    private var resultUri: Uri? = null
    private var mCurrentCaptureImage: String? = null
    lateinit var customerDetailsViewModel: CustomerDetailsModel
    private var invoiceUrl: String? = null
    private lateinit var bindingAdapter : ItemTechnicianComplaintBinding
    private var pos: Int? = null
    private var barcodeView: DecoratedBarcodeView? = null
    private var beepManager: BeepManager? = null

    private var currentScannedProduct: String? = null
    private var enquiryImage : ArrayList<TechnicianEnquiryImage>? = null
    private val formats: Collection<BarcodeFormat> = Arrays.asList(BarcodeFormat.QR_CODE, BarcodeFormat.CODE_39)
    private val mRequestCode = 100
    //private val imgList : ArrayList<String>? = null
    private val imgList: ArrayList<String> = ArrayList()
    private lateinit var alertDialogBuilder:Dialog
    lateinit var  dialog: Dialog
    private var warrantyPartsList: ArrayList<WarrantryPart> = ArrayList()
    var image_uri: Uri? = null

    private val RESULT_LOAD_IMAGE = 123
    val IMAGE_CAPTURE_CODE = 654

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTechnicianCustomerDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        customerDetailsViewModel = ViewModelProvider(this)[CustomerDetailsModel::class.java]
        // set toolbar as support action bar
        setSupportActionBar(binding.toolbar)

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

        }

        leadData = intent?.getParcelableExtra<TechnicianLeadData>(Constants.LEAD_DATA) as TechnicianLeadData

        if (leadData.enquiries.isNullOrEmpty()) {
            // binding.includedContent.tvEnquiryHeading.visibility = View.GONE
        }

        complaintAdapter = TechnicianCustomerDetailsAdapter(leadData.enquiries,leadData, itemClickListener = onItemClickListener)

        binding.includedContent.rvComplaint.apply {
            adapter = complaintAdapter
        }

        if(leadData.pending_technician_detail_count == "0"/* && leadData.in_warranty_enquiries_count!! > "0"*/){
            binding.includedContent.btnTaskComplete.visibility = View.VISIBLE
            binding.includedContent.btnTaskCompleteHide.visibility = View.GONE

        }else{
            binding.includedContent.btnTaskComplete.visibility = View.GONE
            binding.includedContent.btnTaskCompleteHide.visibility = View.VISIBLE
        }
       // supportActionBar?.hide()

        bindUserDetails(leadData)
        setObserver()


    }

    private fun setObserver() {
        customerDetailsViewModel.assignTechnicianLead()

       //customerDetailsViewModel.qrCodeData.observe(this, this::onValidateQRCode)

        customerDetailsViewModel.invoiceUploadData.observe(this, this::onFileUploaded)

    }



    private fun onValidateQRCode(resources: Resource<ValidateProductResponse>) {
        when (resources.status) {
            Status.LOADING -> {
                //showLoadingUI()

            }
            Status.ERROR -> {
               // hideLoadingUI()
                val r = Runnable {
                    barcodeView?.resume()
                }
                Handler().postDelayed(r, 2000)

                //SHOW POPUP
               /* Alerter.create(this)
                    .setText(resources.error?.message.toString())
                    .setBackgroundColorRes(R.color.orange)
                    .setDuration(1500)
                    .show()*/

                Utils.instance.popupPinUtil(this,
                    resources.error?.message.toString(),
                    "",
                    false)
            }
            else -> {
               // hideLoadingUI()


                val data = resources.data
                println("datadata Code: " + data)
                data.let {
                    if(it?.success == true){
                        //ADD IMAGE OF QR CODE and stop scanner
                        bindingAdapter.ivQrCodeImage.visibility = View.VISIBLE
                        bindingAdapter.barcodeScanner.visibility = View.INVISIBLE

                        bindingAdapter.tvQrcodeSuccessVerify.text = data?.message.toString()


                        Utils.instance.popupPinUtil(this,
                            "Task Barcode Scanned Successfully",
                            "",
                            true)
                        /*Alerter.create(this)
                            .setText("Upload Invoice")
                            .setBackgroundColorRes(R.color.orange)
                            .setDuration(1500)
                            .show()*/

                    }else{
                        it?.message?.let { msg ->
                            Utils.instance.popupUtil(this@TechnicianCustomerDetailsActivity,
                                msg,
                                null,
                                false)
                        }
                        bindingAdapter.ivQrCodeImage.visibility = View.INVISIBLE
                        bindingAdapter.barcodeScanner.visibility = View.VISIBLE

                        val r = Runnable {
                            barcodeView?.resume()
                        }
                        Handler().postDelayed(r, 1000)
                    }
                }.run {  }
            }
        }
    }

    private fun bindUserDetails(leadData: TechnicianLeadData) {
        binding.includedContent.tvCustomerNameValue.text = leadData.name
        binding.includedContent.tvContactNumberValue.text = leadData.mobile
        binding.includedContent.tvPinCodeValue.text = leadData.pincode
        binding.includedContent.tvFullAddressValue.text = leadData.address1+""+","+""+leadData.address2/*+""+","+""+leadData.state*/
        binding.includedContent.tvTvRequestIdValue.text = leadData.complain_id.toString()
       // binding.includedContent.tvRequestDateValue.text = leadData.service_center_assigned_at
        val str = leadData.created_at
        val delimiter = " "
        val parts = str?.split(delimiter)

        binding.includedContent.tvRequestDateValue.text = parts?.get(0)+""+" "+""+ parts?.get(1)+""+" "+""+ parts?.get(2)+""+""+"\n"+ parts?.get(3)+""+" "+""+ parts?.get(4)+"\n"


        binding.includedContent.tvTimerValue.text = leadData.timer
        binding.includedContent.tvTimerValue.setTextColor(Color.parseColor(leadData.color_code))

        binding.includedContent.btnTaskComplete.setOnClickListener {

            customerDetailsViewModel.hitTaskCompletedRequest(
                leadData.id,
            ).observe(this@TechnicianCustomerDetailsActivity, this::onTaskCompletedRequest)
        }
        binding.includedContent.tvContactNumber.setOnClickListener {
            openCallDialPad(leadData.mobile.toString())
        }
        binding.includedContent.tvFullAddressValue.setOnClickListener {
            openDirection(leadData)
        }
    }

    private fun openDirection(leadDta: TechnicianLeadData) {
      /*  val gmmIntentUri = Uri.parse("https://www.google.com/maps/dir/48.8276261,2.3350114/48.8476794,2.340595/48.8550395,2.300022/48.8417122,2.3028844")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        startActivity(mapIntent)*/


        val dir: String
        if(!leadDta.latitude.isNullOrEmpty() && !leadDta.longitude.isNullOrEmpty()) {
            dir = leadDta.latitude + "" + "," + "" + leadDta.longitude
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

    private fun openCallDialPad(contactNumber: String) {
        val i = Intent(Intent.ACTION_DIAL)
        val p = "tel:$contactNumber"
        i.data = Uri.parse(p)
        startActivity(i)
    }

    private fun onTaskCompletedRequest(resources: Resource<TechnicianRequestLeadResponse>) {
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

                        Handler(Looper.getMainLooper()).postDelayed({
                            val intent = Intent(this, TechnicianRequestLeadActivity::class.java)
                            startActivity(intent)
                            //onBackPressed()
                            finish()
                        }, 5000)

                    } else {

                    }
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                //onBackPressed()
                CommonUtils.imageList.clear()
                val intent = Intent(this, TechnicianRequestLeadActivity::class.java)
                startActivity(intent)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }


    @RequiresApi(Build.VERSION_CODES.M)
    private val onItemClickListener: (Int, View, ItemTechnicianComplaintBinding, enquiryImage: ArrayList<TechnicianEnquiryImage>) -> Unit =
        { position, view, binding, image ->
            pos = position
            bindingAdapter = binding
            enquiryImage = image
            when (view.id) {
                R.id.iv_invoice_image -> {
                      goToFullScreenImageActivity(leadData.enquiries[position].invoice_url)
                }
                R.id.iv_qr_code_image -> {
                    // goToFullScreenImageActivity(leadData.enquiries[position].qr_image)
                }

                R.id.btn_scan_qr -> {
                 // clearScannerAddMoreProducts()
                  //   barcodeView = binding.barcodeScanner
                   // initScanner()

                  /*val intent = Intent(this, BarScannerActivity::class.java)
                    intent.putExtra("lead_id",leadData.enquiries.get(pos!!).id.toString())
                    startActivityForResult(intent,1)*/

                }
                R.id.btn_upload_image -> {
                    //println("positionpositionpositionposition"+position)
                    val arraylist = ArrayList<String>()
                    for(i in CommonUtils.imageList){
                        if(i.position == pos.toString()){
                            arraylist.add(i.file!!)
                        }
                    }
                    if(arraylist.count() < 5) {
                        if (checkCameraPermission()) {
                            bindingAdapter = binding
                            loadBottomSheetDialog()
                        }else{
                            println("requestPermission"+"requestPermission")

                            /*if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                                == PackageManager.PERMISSION_DENIED){
                                    requestPermissions()
                                println("PERMISSION_DENIED"+"PERMISSION_DENIED")
                                Intent(
                                    ACTION_APPLICATION_DETAILS_SETTINGS,
                                    Uri.parse("package:${this!!.packageName}")
                                ).apply {
                                    addCategory(Intent.CATEGORY_DEFAULT)
                                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                    startActivity(this)
                                }
                               // ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), REQUEST_CAMERA_PERMISSION)

                            }else{
                                println("GRANTED"+"GRANTED")
                                bindingAdapter = binding
                                loadBottomSheetDialog()
                            }*/
                          }
                    }else{
                        Utils.instance.popupPinUtil(this@TechnicianCustomerDetailsActivity,
                            "Please select maximum 5 item!",
                            "",
                            false)
                    }
                }
                R.id.btn_technician_update -> {
                    if (invoiceUrl == null && image.isEmpty()) {

                        Utils.instance.popupPinUtil(
                            this@TechnicianCustomerDetailsActivity,
                            "Please upload Image!",
                            "",
                            false
                        )
                    }
                   /* if(currentScannedProduct == null){
                        Utils.instance.popupPinUtil(
                            this@TechnicianCustomerDetailsActivity,
                            "Please verify QR code!",
                            "",
                            false
                        )
                      //  Toast.makeText(getApplicationContext(), "Please upload Image", Toast.LENGTH_SHORT).show();
                    }*/ /*else {

                    }*/
                    hitUpdateRequest(position)
                }
                R.id.tv_count_image ->{

                    for(i in CommonUtils.imageList){
                        if(i.position == pos.toString()){
                            println("i.filei.filei.filei.file"+i.file)
                        }
                    }
                    goToSliderImageActivity(CommonUtils.imageList)
                }
                R.id.iv_upload_image -> {
                    //if(!CommonUtils.imageList.isNullOrEmpty()) {
                        if (leadData.enquiries[position].lead_enquiry_images.isNullOrEmpty()) {

                            goToSliderImageActivity(CommonUtils.imageList)
                        } else {
                            val intent = Intent(this, EnquirySliderScreenImageActivity::class.java)
                            intent.putExtra(Constants.IMAGE_DATA, leadData.enquiries[position])
                            startActivity(intent)
                    }
                }
                R.id.btn_scan_qrcode -> {
                    val intent = Intent(this, BarScannerActivity::class.java)
                    intent.putExtra("lead_id",leadData.enquiries.get(pos!!).id.toString())
                    startActivityForResult(intent,1)

                }
                R.id.btn_cancel_task -> {
                    showCancelEnquiryPopUp(position)

                }
                R.id.tv_serviceable_warranty_parts -> {
                    openDropDown()
                }
            }
        }

    private fun openDropDown() {

        val dialog = Dialog(this)
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

        if(warrantyPartsList.isNullOrEmpty()){
            val adapter = ServiceableWarrantryPartAdapter(leadData.enquiries[pos!!].warranty_parts)
            rv_complaint_list.adapter = adapter
        }else {
            val adapter = ServiceableWarrantryPartAdapter(warrantyPartsList)
            rv_complaint_list.adapter = adapter
        }
        if(warrantyPartsList.isNullOrEmpty()) {
            val expandableListAdapter =
                CustomExpandableListAdapter(
                    this,
                    leadData.enquiries[pos!!].warranty_parts,
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
            println("groupPosition"+groupPosition)

            // tv_not_covered_condition.setVisibility(View.VISIBLE);

        }

        expandableListView.setOnGroupCollapseListener { groupPosition ->

        }
        expandableListView.setOnChildClickListener { parent, v, groupPosition, childPosition, id ->

            false
        }

        // This will pass the ArrayList to our Adapter

        dialog.show()
    }

    private fun showCancelEnquiryPopUp(position: Int) {

        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View = inflater.inflate(R.layout.cancel_lead_popup_layout, null)
        val tv_cancel_lead_cancel = view.findViewById<TextView>(R.id.tv_cancel_lead_cancel)
        val tv_cancel_lead_ok = view.findViewById<TextView>(R.id.tv_cancel_lead_ok)
        val et_cancel_lead_reason = view.findViewById<EditText>(R.id.et_cancel_lead_reason)
        alertDialogBuilder = Dialog(this@TechnicianCustomerDetailsActivity)
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

        customerDetailsViewModel.hitTechnicianTaskCancelRequest(jsonObject)
            .observe(this, this::getCancelRequestLead)

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
                        Handler(Looper.getMainLooper()).postDelayed({
                            val intent = Intent(this, TechnicianRequestLeadActivity::class.java)
                            startActivity(intent)
                            finish()
                        }, 5000)

                    } else {

                    }
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            if (resultCode == 1) {
                //get extra data from data intent here
                currentScannedProduct  = data!!.getStringExtra("currentScanner")

                bindingAdapter.ivQrCodeImage.visibility = View.VISIBLE
                bindingAdapter.barcodeScanner.visibility = View.INVISIBLE

                bindingAdapter.liNoScannerPart.visibility = VISIBLE
                bindingAdapter.liScannerPart.visibility = GONE

              //currentScanner?.let { hitValidQRCode(it) }
            }else if(resultCode == 2){
                bindingAdapter.btnCancelTask.visibility = VISIBLE
            }
        }else if (requestCode == IMAGE_CAPTURE_CODE && resultCode == Activity.RESULT_OK) {
                //imageView.setImageURI(image_uri);
                val bitmap = uriToBitmap(image_uri!!)
                //binding.liUploadFileValue.visibility = VISIBLE
                //binding.uploadedImg.setImageBitmap(bitmap)
            }
    }

    private fun goToFullScreenImageActivity(invoiceImage: String?) {
        val intent = Intent(this, FullScreenImageActivity::class.java)

        intent.putExtra(Constants.IMAGE_URL, invoiceImage)
        startActivity(intent)
    }
    private fun goToSliderImageActivity(imageList: ArrayList<ImageListData>?) {
        val arraylist = ArrayList<String>()
        for(i in CommonUtils.imageList){
            if(i.position == pos.toString()){
                arraylist.add(i.file!!)

            }
        }
        val intent = Intent(this, SliderScreenImageActivity::class.java)
        intent.putExtra(Constants.IMAGE_DATA, arraylist)
        startActivity(intent)
    }

    private fun hitUpdateRequest(position: Int) {

        //currentScannedProduct?.let { validateScanerQRCode(it) }
      /*  if (currentScannedProduct == null) {
            Toast.makeText(getApplicationContext(), "Please Scan QRCode", Toast.LENGTH_SHORT)
                .show();
        } else {
*/
            val jsonObject = JsonObject()

            try {
                val arraylist = ArrayList<String>()
                for(i in CommonUtils.imageList){
                    if(i.position == pos.toString()){
                        arraylist.add(i.file!!)


                    }
                }
                val jsArray  = JsonArray()
                for (i in arraylist){
                    val jsonObj = JsonObject()
                    jsonObj.addProperty("file",i)
                    //jsArray.add(jsonObj)
                    jsArray.add(i)
                    //println("i.file"+jsArray)
                }
                /*for (i in CommonUtils.imageList){
                    val jsonObj = JsonObject()
                    jsonObj.addProperty("file",i.file)
                    //jsArray.add(jsonObj)
                    jsArray.add(i.file)
                    //println("i.file"+jsArray)
                }*/

               /* if (invoiceUrl == null) {
                    jsonObject.addProperty(
                       // "image_link", enquiryImage!![position].image
                        "image_link", leadData.enquiries.get(pos!!).invoice_url
                    )
                } else {
                    jsonObject.addProperty("image_link", invoiceUrl)
                }
*/
                println("jsArray"+jsArray);
                jsonObject.add("image_links", jsArray)
                customerDetailsViewModel.hitTechnicianUpdateRequest(
                    jsonObject,
                    leadData.enquiries.get(pos!!).id
                ).observe(this@TechnicianCustomerDetailsActivity, this::onUpdateRequest)
               // CommonUtils.imageList.clear()


            } catch (e: JSONException) {
                e.printStackTrace()
            }
       // }
    }

    private fun clearScannerAddMoreProducts() {

        currentScannedProduct = null
        bindingAdapter.tvQrCodeNumber.text = ""

        bindingAdapter.ivQrCodeImage.visibility = View.INVISIBLE
        bindingAdapter.barcodeScanner.visibility = View.VISIBLE

        val r = Runnable {
            barcodeView?.resume()
        }
        Handler().postDelayed(r, 1000)
    }

    private fun loadBottomSheetDialog() {
        val fragment = CameraBottomDialogFragment().newInstance()
        fragment.bottomSheetItemClick = this@TechnicianCustomerDetailsActivity
        fragment.show(supportFragmentManager, "Bottomsheet_Media_Selection")
    }

    private fun checkCameraPermission(): Boolean {
       /* return if ((ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
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
                println("permission"+"permission")

            } else {
                println("no permission"+"no permission")
                requestPermissionLauncher.launch(
                    Manifest.permission.CAMERA)
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

    val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                println("hua h "+"hua h")
                loadBottomSheetDialog()
                // Permission is granted. Continue the action or workflow in your
                // app.
            } else {

                println("nhi hua h "+" nhi hua h")
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_DENIED){
                    requestPermissions()
                    AlertDialog.Builder(this)
                        .setTitle("Need External Permission")
                        .setMessage("We need external access permission for uploading your image")
                        .setPositiveButton(
                            "ok"
                        ) { dialogInterface: DialogInterface?, i: Int ->
                            //Prompt the user once explanation has been shown

                            Intent(
                                ACTION_APPLICATION_DETAILS_SETTINGS,
                                Uri.parse("package:${this!!.packageName}")
                            ).apply {
                                addCategory(Intent.CATEGORY_DEFAULT)
                                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                startActivity(this)
                            }

                        }.create().show()
                    println("PERMISSION_DENIED"+"PERMISSION_DENIED")
                    /*Intent(
                        ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.parse("package:${this!!.packageName}")
                    ).apply {
                        addCategory(Intent.CATEGORY_DEFAULT)
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(this)
                    }*/
                    // ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), REQUEST_CAMERA_PERMISSION)

                }else{
                    println("GRANTED"+"GRANTED")
                    //bindingAdapter = binding
                    loadBottomSheetDialog()
                }
                // Explain to the user that the feature is unavailable because the
                // features requires a permission that the user has denied. At the
                // same time, respect the user's decision. Don't link to system
                // settings in an effort to convince the user to change their
                // decision.
            }
        }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this, arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ), MY_PERMISSIONS_WRITE_READ_REQUEST_CODES
        )
    }

    private fun initScanner() {
        // Request camera permissions
        if (isCameraPermissionGranted()) {
            val formats: Collection<BarcodeFormat> = Arrays.asList(BarcodeFormat.QR_CODE, BarcodeFormat.CODE_39)

            barcodeView?.barcodeView?.decoderFactory = DefaultDecoderFactory(formats)
            barcodeView?.initializeFromIntent(intent)
            barcodeView?.decodeContinuous(callback)
            barcodeView?.barcodeView?.cameraSettings?.isAutoFocusEnabled = true
            beepManager = BeepManager(this)
            barcodeView?.getStatusView()?.setVisibility(View.GONE);

        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), REQUEST_CAMERA_PERMISSION)
        }
    }

    private fun isCameraPermissionGranted(): Boolean {
        val selfPermission = ContextCompat.checkSelfPermission(baseContext, Manifest.permission.CAMERA)
        return selfPermission == PackageManager.PERMISSION_GRANTED
    }

     private val callback: BarcodeCallback = object : BarcodeCallback {
        override fun barcodeResult(result: BarcodeResult) {
            if (result.text == null) { // Prevent duplicate scans
                return
            }

            var resultBarCode = result.text
            var currentScanner =
                resultBarCode.replace("http://", "").replace("http:// www.", "").replace("www.", "")

            barcodeView!!.setStatusText(currentScanner)
            //beepManager!!.playBeepSoundAndVibrate()

            println("Scanned Code: " + result.text)
            // writeScancodeEvent(result.text)

            currentScannedProduct = currentScanner

            bindingAdapter.ivQrCodeImage.visibility = View.VISIBLE
            bindingAdapter.barcodeScanner.visibility = View.INVISIBLE

            val r = Runnable {
                barcodeView?.resume()
            }
            Handler().postDelayed(r, 1000)

            barcodeView?.pause()

            Handler().postDelayed(r, 2000)

            hitValidQRCode(currentScanner)

        }
        override fun possibleResultPoints(resultPoints: List<ResultPoint>) {}
    }

    private fun hitValidQRCode(currentScanner: String) {

        val jsonObject = JsonObject()

        try {
            //  val jsArray  = JsonArray()
            jsonObject.addProperty("scanned_barcode", currentScanner)

        } catch (e: JSONException) {
            e.printStackTrace()
        }

        customerDetailsViewModel.hitValidateQRApi(
           jsonObject,
            leadData.enquiries.get(pos!!).id.toString()
        ).observe(this@TechnicianCustomerDetailsActivity, this::validateQRCode)
    }

    private fun validateQRCode(resources: Resource<UpdateRequestResponse>) {
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
/*
                        Alerter.create(this@TechnicianCustomerDetailsActivity)
                            .setTitle("")
                            .setText("" + it.message?.toString())
                            .setBackgroundColorRes(R.color.orange)
                            .setDuration(1000)
                            .show()*/

                        Utils.instance.popupPinUtil(this@TechnicianCustomerDetailsActivity,
                            it.message,
                            "",
                            true)


                        bindingAdapter.tvQrcodeSuccessVerify.visibility = VISIBLE
                        bindingAdapter.tvQrcodeSuccessVerify.text = response.message.toString()

                      /* bindingAdapter.ivQrCodeImage.visibility = VISIBLE
                        bindingAdapter.scannerView.visibility = GONE*/

                    } else {
                           // clearScannerAddMoreProducts()
                        it.message?.let { msg ->
                            Utils.instance.popupPinUtil(this@TechnicianCustomerDetailsActivity,
                                msg,
                                null,
                                false)
                        }
                        val r = Runnable {
                            barcodeView?.resume()
                        }
                        Handler().postDelayed(r, 1000)


                        Handler(Looper.getMainLooper()).postDelayed({
                            val intent = Intent(this, BarScannerActivity::class.java)
                            startActivity(intent)
                            //onBackPressed()
                            //finish()
                        }, 5000)
                    }
                }
            }
        }
    }

    private fun onUpdateRequest(resources: Resource<TechnicianUpdateRequestResponse>) {
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

                      /*  Alerter.create(this@TechnicianCustomerDetailsActivity)
                            .setTitle("")
                            .setText("" + it.message?.toString())
                            .setBackgroundColorRes(R.color.orange)
                            .setDuration(1000)
                            .show()*/


                        Utils.instance.popupUtil(this@TechnicianCustomerDetailsActivity,
                            it.message,
                            "",
                            true)

                        showData(response)

                    } else {

                    }
                }
            }
        }
    }

    private fun showData(data: TechnicianUpdateRequestResponse) {
        if (data.data.technician_detail_status == "1") {
            bindingAdapter.btnTechnicianHideUpdate.visibility = View.VISIBLE
            bindingAdapter.btnTechnicianUpdate.visibility = View.GONE

            bindingAdapter.btnUploadImage.setFocusable(false);
            bindingAdapter.btnUploadImage.setEnabled(false);
            bindingAdapter.btnUploadImage.setCursorVisible(false);
            bindingAdapter.btnUploadImage.setKeyListener(null);

            //if(data.data.purchase_at != null){
                if(!data.data.warranty_parts.isEmpty()){
                        warrantyPartsList.clear()
                    // warrantyPartsList.add(data.data.warranty_parts.get(pos!!))
                      bindingAdapter.tvServiceableWarrantyParts.visibility = VISIBLE
                }else {
                        bindingAdapter.tvServiceableWarrantyParts.visibility = GONE
                }
           /* }else{
                bindingAdapter.tvServiceableWarrantyParts.visibility = GONE
            }*/

        }else{
            bindingAdapter.btnTechnicianHideUpdate.visibility = View.GONE
            bindingAdapter.btnTechnicianUpdate.visibility = View.VISIBLE

        }
      /*  if(!data.data.warranty_parts.isEmpty()){
            warrantyPartsList.clear()
            warrantyPartsList.add(data.data.warranty_parts.get(pos!!))
            bindingAdapter.tvServiceableWarrantyParts.visibility = VISIBLE
        }else {
            bindingAdapter.tvServiceableWarrantyParts.visibility = GONE
        }*/
        //if(data.data.pending_technician_detail == "0" /*&& leadData.in_warranty_enquiries_count!! > "0"*/){
        if(data.data.pending_technician_detail == "0"/*&& leadData.in_warranty_enquiries_count!! > "0"*/){
            binding.includedContent.btnTaskComplete.visibility = View.VISIBLE
            binding.includedContent.btnTaskCompleteHide.visibility = View.GONE

        }else{
            binding.includedContent.btnTaskComplete.visibility = View.GONE
            binding.includedContent.btnTaskCompleteHide.visibility = View.VISIBLE
        }
    }




 /*   override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return barcodeView!!.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event)
    }
*/
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (isCameraPermissionGranted()) {
                barcodeView?.barcodeView?.decoderFactory = DefaultDecoderFactory(formats)
                barcodeView?.initializeFromIntent(intent)
                barcodeView?.decodeContinuous(callback)
                beepManager = BeepManager(this@TechnicianCustomerDetailsActivity)
                barcodeView?.pause()

                val r = Runnable {
                    barcodeView?.resume()

                }
                Handler().postDelayed(r, 2000)
                //loadBottomSheetDialog()

            } else {
                Toast.makeText(this, "Camera permission is required.", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btn_assign_technician -> {
            }
        }
    }

    override fun bottomSheetItemClick(clickAction: String?) {

        when (clickAction) {
            CAMERA -> {
                //  barcodeView?.pause()
                startImageCapture()
            }
            /* GALLERY -> {
                getImageGallery()
            }*/
            CANCEL -> {
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

           // resultUri = Utils.instance.reSizeImg(image)

            //validationUtil()

        } catch (ex: java.lang.Exception) {
            ex.printStackTrace()
        }
        buildMutilPart()
        //  binding.includedContent.ivUploadImage.setImageURI(resultUri)
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

        customerDetailsViewModel.hitUploadFile(params.build())
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

    @Throws(IOException::class)
    private fun createImageFile(): File? {

        val imageFileName = "orpatservice_"
       // val storageDir = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "ALCS"+ File.separator + "LaneShots")
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

    private fun onFileUploaded(resources: Resource<UploadFileResponse>) {
        when (resources.status) {
            Status.LOADING -> {
                binding.cpiLoading.visibility = View.VISIBLE

            }
            Status.ERROR -> {
                binding.cpiLoading.visibility = View.GONE
               /* Alerter.create(this)
                    .setText(resources.error?.message.toString())
                    .setBackgroundColorRes(R.color.orange)
                    .setDuration(1500)
                    .show()*/

                Utils.instance.popupPinUtil(
                    this@TechnicianCustomerDetailsActivity,
                    resources.error?.message.toString(),
                    null,
                    false
                )
            }
            else -> {
                binding.cpiLoading.visibility = View.GONE

                val data = resources.data

                data.let {
                    if(it?.success == true){
                        invoiceUrl = it.data.invoice_url


                        Utils.instance.popupPinUtil(
                            this@TechnicianCustomerDetailsActivity,
                            it.message,
                            null,
                            true
                        )
                       // println("invoiceUrlinvoiceUrl"+it.data.invoice_url)/
                        Glide.with(bindingAdapter.ivUploadImage)
                            .load(invoiceUrl)
                            //.diskCacheStrategy(DiskCacheStrategy.ALL)
                            //.circleCrop() // .error(R.drawable.active_dot)
                            .placeholder(R.color.gray)
                            .into(bindingAdapter.ivUploadImage)

                        val imageData = ImageListData(invoiceUrl,pos.toString())

                        CommonUtils.imageList.add(imageData)
                        val arraylist = ArrayList<String>()

                        for(i in CommonUtils.imageList){
                            if(i.position == pos.toString()){
                                arraylist.add(i.file!!)

                            }
                        }
                        println("imgListimgListimgList"+arraylist.count())
                        bindingAdapter.tvCountImage.visibility = VISIBLE
                       // bindingAdapter.tvCountImage.text = "+"+""+""+CommonUtils.imageList.count().toString()
                        bindingAdapter.tvCountImage.text = "+"+""+""+arraylist.count().toString()

                    }else{
                        it?.message?.let { msg ->
                             Utils.instance.popupUtil(this@TechnicianCustomerDetailsActivity, msg, null, false)
                        }
                        /*val r = Runnable {
                            // barcodeView?.resume()
                        }
                        Handler().postDelayed(r, 1000)*/
                    }
                }.run {  }
            }
        }
    }
}

