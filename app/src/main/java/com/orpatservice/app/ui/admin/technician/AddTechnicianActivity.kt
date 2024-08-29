package com.orpatservice.app.ui.admin.technician

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.orpatservice.app.R
import com.orpatservice.app.data.Resource
import com.orpatservice.app.data.Status
import com.orpatservice.app.data.model.AddTechnicianResponse
import com.orpatservice.app.data.model.TechnicianData
import com.orpatservice.app.databinding.ActivityAddTechnicianBinding
import com.orpatservice.app.databinding.AdapterRequestPincodeBinding
import com.orpatservice.app.ui.leads.customer_detail.UploadFileResponse
import com.orpatservice.app.utils.CommonUtils
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


const val ADD = "ADD"
const val UPDATE = "UPDATE"
const val PARCELABLE_TECHNICIAN = "PARCELABLE_TECHNICIAN"
const val MY_PERMISSIONS_WRITE_READ_REQUEST_CODE = 1000
const val MY_PERMISSIONS_WRITE_READ_REQUEST_CODE_AADHARCARD = 2000

class AddTechnicianActivity : AppCompatActivity(), View.OnClickListener,
    CameraBottomSheetDialogFragment.BottomSheetItemClick {
    private lateinit var binding: ActivityAddTechnicianBinding
    private lateinit var viewModel: TechniciansViewModel
    lateinit var dialog: Dialog
    private var invoiceUrl: String? = null
    private var aadharCardUrl: String? = null
    private var pincodeDataArrayList: ArrayList<PincodeData> = ArrayList()
    private var flag:String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddTechnicianBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            title = ""
            // show back button on toolbar
            // on back button press, it will navigate to parent activity
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        viewModel = ViewModelProvider(this)[TechniciansViewModel::class.java]


        setObserver()
        uiBind()

        binding.includedContent.btnSubmitMobile.setOnClickListener(this)
        binding.includedContent.vImage.setOnClickListener(this)
        binding.includedContent.vUploadAadharcard.setOnClickListener(this)
        binding.includedContent.etPinCode.setOnClickListener(this)


        if (intent.getStringExtra(UPDATE).equals(UPDATE)) {
            bindUpdateTechnician()

        }
    }

    private fun uiBind() {

        if(!CommonUtils.pincodeData.isEmpty()) {
            for(i in CommonUtils.pincodeData) {
                binding.includedContent.etPinCode.setText(i.pincode.toString())
            }
        }
    }

    private fun setObserver() {

        viewModel.invoiceUploadData.observe(this, this::onFileUploaded)
        viewModel.pincodeData.observe(this, this::getPincode)
        viewModel.submitTechnicianData.observe(this, this::onAddTechnicianUpload)
        viewModel.loadPincode()

    }

    private var technicianID: Int? = 0
    private fun bindUpdateTechnician() {
        val technicianData = intent.getParcelableExtra<TechnicianList>(
            PARCELABLE_TECHNICIAN
        )
        binding.includedContent.etFirstName.setText(technicianData?.first_name)
        binding.includedContent.etLastName.setText(technicianData?.last_name)
        binding.includedContent.etMobileNo.setText(technicianData?.mobile)
        binding.includedContent.etPinCode.setText(technicianData?.pincode)

        val list = ArrayList<String>()
        for (i in pincodeDataArrayList) {
            //list.clear()
            list.add(i.pincode.toString())

        }
        val text: String = list.toString().replace("[", "").replace("]", "")
        binding.includedContent.etPinCode.setText(text.toString())



        /*binding.includedContent.etPinCode.setOnClickListener {

            //  val intent = Intent(this, PincodeSelectionActivity::class.java)
            // startActivity(intent)

        }*/

        if (!CommonUtils.pincodeData.isEmpty()) {
            for (i in CommonUtils.pincodeData) {
                println("i.pincode"+i.pincode)
                //binding.includedContent.etPinCode.text = i.pincode
            }
        }

        technicianID = technicianData?.id
        if (technicianData?.status == "0") {
            binding.includedContent.rbDeActivate.isChecked = true
        }

        Glide.with(binding.includedContent.ivUploadImage)
            .load(technicianData?.image)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .placeholder(R.drawable.ic_tech_avatar)
            .into(binding.includedContent.ivUploadImage)

    }

    private fun onFileUploaded(resources: Resource<UploadFileResponse>) {
        when (resources.status) {
            Status.LOADING -> {
                //    showLoadingUI()

            }
            Status.ERROR -> {
                //  hideLoadingUI()

                Utils.instance.popupPinUtil(this@AddTechnicianActivity,
                    resources.error?.message.toString(),
                    "",
                    false)
            }
            else -> {
                // hideLoadingUI()

                val data = resources.data

                data.let {
                    if(it?.success == true){
                        if(flag == "image") {
                            invoiceUrl = it.data.invoice_url
                        }else{
                            aadharCardUrl = it.data.invoice_url
                        }

                        Utils.instance.popupPinUtil(this@AddTechnicianActivity,
                            it.message,
                            "",
                            true)

                        if(flag == "image") {
                            Glide.with(binding.includedContent.ivUploadImage)
                                .load(invoiceUrl)
                                .placeholder(R.color.gray)
                                .into(binding.includedContent.ivUploadImage)
                        }else{
                            Glide.with(binding.includedContent.ivUploadAadharcard)
                                .load(aadharCardUrl)
                                .placeholder(R.color.gray)
                                .into(binding.includedContent.ivUploadAadharcard)
                        }

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

    private fun onAddTechnicianUpload(resources: Resource<AddTechnicianResponse>) {
        when (resources.status) {
            Status.LOADING -> {
                //    showLoadingUI()
            }

            Status.ERROR -> {

                Utils.instance.popupPinUtil(this@AddTechnicianActivity,
                    resources.error?.message.toString(),
                    "",
                    false)

            }
            else -> {
                // hideLoadingUI()


                val data = resources.data

                data.let {
                    if(it?.success == true){

                        data?.let { it1 ->
                            Utils.instance.popupUtil(this@AddTechnicianActivity,
                                it1.message,
                                "",
                                true)

                            /*Handler(Looper.getMainLooper()).postDelayed({
                                val intent = Intent()
                                intent.putExtra(PARCELABLE_TECHNICIAN, it.data)
                                setResult(Activity.RESULT_OK, intent)
                                finish()
                            }, 5000)*/
                            Handler(Looper.getMainLooper()).postDelayed({
                                val intent = Intent(this, TechniciansActivity::class.java)
                                startActivity(intent)
                                finish()
                            },5000)
                        }
                    }else{
                        it?.message?.let { msg ->
                            Utils.instance.popupPinUtil(this@AddTechnicianActivity,
                                msg,
                                null,
                                false)
                        }
                    }
                }.run {  }
            }
        }
    }


    private fun getPincode(resources: Resource<RequestPincodeResponse>) {
        when (resources.status) {
            Status.LOADING -> {

            }
            Status.ERROR -> {

                Utils.instance.popupPinUtil(
                    this,
                    resources.error?.message.toString(),
                    "",
                    false
                )
            }
            else -> {
                val response = resources.data

                response?.let {
                    if (it.success) {
                    //    pincodeDataArrayList.clear()

                        pincodeDataArrayList.addAll(response.data.pincodes)
                        //requestsPincodeAdapter.notifyDataSetChanged()
                    }
                }
            }
        }
    }

    private fun loadAddTechnician(): Observer<Resource<AddTechnicianResponse>> {
        return Observer { it ->
            when (it?.status) {
                Status.LOADING -> {
                    binding.cpiLoading.visibility = View.VISIBLE

                }
                Status.ERROR -> {
                    binding.cpiLoading.visibility = View.GONE
                    /*Alerter.create(this@AddTechnicianActivity)
                        .setTitle("")
                        .setText("" + it.error?.message.toString())
                        .setBackgroundColorRes(R.color.orange)
                        .setDuration(1000)
                        .show()*/

                    /*Utils.instance.popupPinUtil(
                        this,
                        it.error?.message.toString(),
                        "",
                        false
                    )*/
                }
                else -> {
                    binding.cpiLoading.visibility = View.GONE
                    val data = it?.data

                    data?.let {
                        if (it.success) {
                            //Toast.makeText(this, "" + it.message, Toast.LENGTH_LONG).show()
                            val intent = Intent()
                            intent.putExtra(PARCELABLE_TECHNICIAN, it.data)
                            setResult(Activity.RESULT_OK, intent)
                            finish()

                        }
                    } ?: run {
                        /*Alerter.create(this@AddTechnicianActivity)
                            .setTitle("")
                            .setText(it.data?.message.toString())
                            .setBackgroundColorRes(R.color.orange)
                            .setDuration(1000)
                            .show()*/

                        Utils.instance.popupPinUtil(
                            this,
                            it.data?.message.toString(),
                            "",
                            false
                        )
                    }
                }
            }
        }
    }

    private fun hitAPIAddTechnician() {
       /* val params = MultipartBody.Builder().setType(MultipartBody.FORM)

        params.addFormDataPart(
            "first_name",
            binding.includedContent.etFirstName.text.toString().trim()
        )
        params.addFormDataPart(
            "last_name",
            binding.includedContent.etLastName.text.toString().trim()
        )
        params.addFormDataPart("mobile", binding.includedContent.etMobileNo.text.toString().trim())
        params.addFormDataPart("aadhar_card_no", binding.includedContent.etAadharCardNum.text.toString().trim())

        //params.addFormDataPart("pincode", binding.includedContent.etPinCode.text.toString().trim())

        if (binding.includedContent.rbActivate.isChecked) {
            params.addFormDataPart("status", "1")

        } else if (binding.includedContent.rbDeActivate.isChecked) {
            params.addFormDataPart("status", "0")

        }

        if (!resultUri?.path.isNullOrBlank()) {
            val files = File(resultUri?.path ?: "")
            val requestFile: RequestBody = files.asRequestBody("multipart/form-data".toMediaType())
            params.addFormDataPart("image", files.name, requestFile)
        }

        val jsArray = JsonArray()
        for (i in CommonUtils.pincodeData) {

            // jsonObj.addProperty("pincodes", i.pincode)
            jsArray.add(i.pincode)
        }

        params.addFormDataPart("pincodes", jsArray.toString())
        println("paramsparamsparams"+params.build() + jsArray.toString())

        viewModel.hitAPIAddTechnician(
            params.build()
        ).observe(this, loadAddTechnician())*/


        val jsonObject = JsonObject()
        try {
            val jsArray = JsonArray()
            for (i in CommonUtils.pincodeData) {
                val jsonObj = JsonObject()
                // jsonObj.addProperty("pincodes", i.pincode)
                jsArray.add(i.id)

            }

            if (binding.includedContent.rbActivate.isChecked) {
                jsonObject.addProperty("status", "1")

            } else if (binding.includedContent.rbDeActivate.isChecked) {
                jsonObject.addProperty("status", "0")

            }
            var profileImg: String?= null
            if(invoiceUrl != null){
                profileImg = invoiceUrl
            }else{
                profileImg = ""
            }

            var aadharCardImg: String?= null
            if(aadharCardUrl != null){
                aadharCardImg = aadharCardUrl
            }else{
                aadharCardImg = ""
            }
            jsonObject.addProperty("first_name", binding.includedContent.etFirstName.text.toString().trim())
            jsonObject.addProperty("last_name", binding.includedContent.etLastName.text.toString().trim())
            jsonObject.addProperty("mobile", binding.includedContent.etMobileNo.text.toString().trim())
            jsonObject.addProperty("image", profileImg)
            jsonObject.addProperty("aadhar_card_no", binding.includedContent.etAadharCardNum.text.toString().trim())
            jsonObject.addProperty("aadhar_image", aadharCardImg)
            jsonObject.addProperty("passcode", binding.includedContent.etPassport.text.toString().trim())
            jsonObject.add("pincodes", jsArray)

        } catch (e: JSONException) {
            e.printStackTrace()
        }
        println("jsArray" + jsonObject)

        viewModel.hitSubmitTechnician(jsonObject)


    }

    private fun hitAPIUpdateTechnician() {
        val params = MultipartBody.Builder().setType(MultipartBody.FORM)

        params.addFormDataPart(
            "first_name",
            binding.includedContent.etFirstName.text.toString().trim()
        )
        params.addFormDataPart(
            "last_name",
            binding.includedContent.etLastName.text.toString().trim()
        )
        params.addFormDataPart("mobile", binding.includedContent.etMobileNo.text.toString().trim())
       // params.addFormDataPart("pincode", binding.includedContent.etPinCode.text.toString().trim())
        params.addFormDataPart("aadhar_card_no", binding.includedContent.etAadharCardNum.text.toString().trim())


        if (binding.includedContent.rbActivate.isChecked) {
            params.addFormDataPart("status", "1")

        } else if (binding.includedContent.rbDeActivate.isChecked) {
            params.addFormDataPart("status", "0")

        }

        val jsArray = JsonArray()
        for (i in CommonUtils.pincodeData) {

           // jsonObj.addProperty("pincodes", i.pincode)
            jsArray.add(i.id)
        }

        params.addFormDataPart("pincodes", binding.includedContent.etPinCode.text.toString().trim())

        if (!resultUri?.path.isNullOrBlank()) {
            val files = File(resultUri?.path ?: "")
            val requestFile: RequestBody = files.asRequestBody("multipart/form-data".toMediaType())
            params.addFormDataPart("image", files.name, requestFile)
        }

        println("paramsparamsparams"+params)
        viewModel.hitAPIUpdateTechnician(
            params.build(), technicianID
        ).observe(this, loadAddTechnician())



      /*  val jsonObject = JsonObject()
        try {
            val jsArray = JsonArray()
            for (i in CommonUtils.pincodeData) {
                val jsonObj = JsonObject()
               // jsonObj.addProperty("pincodes", i.pincode)
                jsArray.add(i.pincode)

            }

            if (binding.includedContent.rbActivate.isChecked) {
                jsonObject.addProperty("status", "1")

            } else if (binding.includedContent.rbDeActivate.isChecked) {
                jsonObject.addProperty("status", "0")

            }

            jsonObject.addProperty("first_name", binding.includedContent.etFirstName.text.toString().trim())
            jsonObject.addProperty("last_name", binding.includedContent.etLastName.text.toString().trim())
            jsonObject.addProperty("mobile", binding.includedContent.etMobileNo.text.toString().trim())
            jsonObject.addProperty("image", invoiceUrl)
            jsonObject.addProperty("aadhar_card_no", binding.includedContent.etAadharCardNum.text.toString().trim())
            jsonObject.addProperty("aadhar_image", invoiceUrl)
            jsonObject.add("pincodes", jsArray)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        println("jsArray" + jsonObject)

        viewModel.hitSubmitTechnician(jsonObject)*/
    }

    private fun isValidAddTechnician(): Boolean {
        return (Utils.instance.validateFirstName(binding.includedContent.etFirstName)
                && Utils.instance.validateLastName(binding.includedContent.etLastName)
                && Utils.instance.validatePhoneNumber(binding.includedContent.etMobileNo))
               // && Utils.instance.validatePinCode(binding.includedContent.etPinCode))

    }

    private fun loadBottomSheetDialog(flag:String) {
        val fragment = CameraBottomSheetDialogFragment().newInstance()
        fragment.bottomSheetItemClick = this
        fragment.show(supportFragmentManager, "Bottomsheet_Media_Selection")
    }

    override fun bottomSheetItemClick(clickAction: String?) {
        when (clickAction) {
            CAMERA -> {
                if(flag == "image") {
                    startImageCapture()
                }else{
                    startImageCapture()
                }
            }
            GALLERY -> {
                if(flag == "image") {
                    getImageGallery()
                }else{
                    getImageGallery()
                }
            }
            CANCEL -> {
            }
        }
    }

    private var mCurrentCaptureImage: String? = null
    private var resultUri: Uri? = null

    private fun getImageGallery() {
        val chosePhoto =
            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        chosePhoto.type = "image/*"
        startForResultGallery.launch(chosePhoto)

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
                        "com.orpatservice.app.fileprovider",
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

    private fun createImageFile(): File? {
        val imageFileName = "orpatservice_"
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

        } catch (ex: java.lang.Exception) {
            ex.printStackTrace()
        }
        //binding.includedContent.ivUploadImage.setImageURI(resultUri)
        //binding.ivSelectedImage.setImageURI(resultUri)

        buildMutilPart()
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

        viewModel.hitServiceCenterUploadFile(
            params.build(),
        )
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

    private fun checkAadharCardCameraPermission(): Boolean {
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
                        requestPermissionsAadharCard()

                    }.create().show()
            } else {
                // No explanation needed, we can request the permission.
                requestPermissionsAadharCard()

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

    private fun requestPermissionsAadharCard() {
        ActivityCompat.requestPermissions(
            this, arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ), MY_PERMISSIONS_WRITE_READ_REQUEST_CODE_AADHARCARD
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == MY_PERMISSIONS_WRITE_READ_REQUEST_CODE) {
            if ((ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
                        == PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
                        == PackageManager.PERMISSION_GRANTED)
            ) {
                loadBottomSheetDialog("image")
            }

        }else if(requestCode == MY_PERMISSIONS_WRITE_READ_REQUEST_CODE_AADHARCARD) {
            if ((ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
                        == PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
                        == PackageManager.PERMISSION_GRANTED)
            ) {
                loadBottomSheetDialog("aadhar")
            }
        }
    }

    override fun onClick(view: View?) {
        when (view?.id) {

            R.id.btn_submit_mobile -> {

                if (intent.getStringExtra(ADD).equals(ADD) && isValidAddTechnician()) {
                    hitAPIAddTechnician()

                } else {
                    if (isValidAddTechnician()) {
                        hitAPIUpdateTechnician()

                    }
                }
            }

            R.id.v_image -> {
                flag = "image"
                if (checkCameraPermission()) {
                    loadBottomSheetDialog("image")
                }
            }

            R.id.v_upload_aadharcard -> {
                flag = "aadhar"
                if (checkAadharCardCameraPermission()) {
                    loadBottomSheetDialog("aadhar")
                }
            }

            R.id.et_pin_code -> {
                //  val intent = Intent(this, PincodeSelectionActivity::class.java)
                //  startActivity(intent)
                openPincodeList()
            }
        }
    }

    private fun openPincodeList() {

        dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.activity_pincode_selection)
        val rv_pincode_list = dialog.findViewById(R.id.rv_pincode_list) as RecyclerView
        val btn_ok = dialog.findViewById(R.id.btn_ok) as Button
        val iv_cancel = dialog.findViewById(R.id.iv_cancel) as ImageView
        iv_cancel.setOnClickListener {
            dialog.dismiss()
        }
        btn_ok.setOnClickListener {
            dialog.dismiss()
        }
        rv_pincode_list.layoutManager = LinearLayoutManager(this)

        // This will pass the ArrayList to our Adapter
        val adapter =
            RequestsPincodeAdapter(pincodeDataArrayList, itemClickListener = onItemClickListener)

        // Setting the Adapter with the recyclerview
        rv_pincode_list.adapter = adapter
        dialog.show()

    }

    private val onItemClickListener: (Int, View, AdapterRequestPincodeBinding) -> Unit =
        { position, view, bind ->
            when (view.id) {
               R.id.check_id -> {
                   val list = ArrayList<String>()

                   if(bind.checkId.isChecked) {

                       val pincodeData = PinData(
                           pincodeDataArrayList[position].id.toString(),
                           pincodeDataArrayList[position].pincode.toString(),
                       )
                       CommonUtils.pincodeData.add(pincodeData)
                       for (i in CommonUtils.pincodeData) {
                           //list.clear()
                           list.add(i.pincode)

                       }
                       val text: String = list.toString().replace("[", "").replace("]", "")
                       binding.includedContent.etPinCode.setText(text.toString())

                   }else if(!bind.checkId.isChecked){
                       val pincodeData = PinData(
                           pincodeDataArrayList[position].id.toString(),
                           pincodeDataArrayList[position].pincode.toString(),
                       )
                       CommonUtils.pincodeData.remove(pincodeData)
                       for (i in CommonUtils.pincodeData) {
                           //list.clear()
                           list.add(i.pincode)

                       }
                       val text: String = list.toString().replace("[", "").replace("]", "")
                       binding.includedContent.etPinCode.setText(text.toString())

                   }
                }
            }
        }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                CommonUtils.pincodeData.clear()
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

}