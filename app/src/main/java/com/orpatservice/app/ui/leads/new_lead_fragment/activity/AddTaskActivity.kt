package com.orpatservice.app.ui.leads.new_lead_fragment.activity

import android.Manifest

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import com.google.gson.JsonObject
import com.google.zxing.BarcodeFormat
import com.google.zxing.ResultPoint
import com.google.zxing.client.android.BeepManager
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import com.journeyapps.barcodescanner.DecoratedBarcodeView
import com.journeyapps.barcodescanner.DefaultDecoderFactory
import com.orpatservice.app.BuildConfig
import com.orpatservice.app.data.Resource
import com.orpatservice.app.data.Status
import com.orpatservice.app.data.model.requests_leads.LeadData
import com.orpatservice.app.databinding.ActivityAddTaskBinding
import com.orpatservice.app.ui.admin.technician.CAMERA
import com.orpatservice.app.ui.admin.technician.CANCEL
import com.orpatservice.app.ui.admin.technician.CameraBottomSheetDialogFragment
import com.orpatservice.app.ui.admin.technician.GALLERY
import com.orpatservice.app.ui.leads.customer_detail.UploadFileResponse
import com.orpatservice.app.ui.leads.new_lead_fragment.new_lead_request.ProductViewModel
import com.orpatservice.app.ui.leads.technician.ValidateProductResponse
import com.orpatservice.app.utils.Constants
import com.orpatservice.app.utils.Utils
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileDescriptor
import java.io.IOException
import java.util.*

const val MY_PERMISSIONS_WRITE_READ_REQUEST_CODE = 1000
class AddTaskActivity : AppCompatActivity(), CameraBottomSheetDialogFragment.BottomSheetItemClick {

    companion object {
        private const val REQUEST_CAMERA_PERMISSION = 10
    }
    private var barcodeView: DecoratedBarcodeView? = null
    private var beepManager: BeepManager? = null

    private var currentScannedProduct: String? = null
    private var currentScannedProduct1: String? = null
    private lateinit var viewModel: ProductViewModel
    private var invoiceUrl: String? = null
    private var mCurrentCaptureImage: String? = null
    private var resultUri: Uri? = null
    private lateinit var leadData: LeadData

    val PERMISSION_ID = 42
    private val formats: Collection<BarcodeFormat> =
        Arrays.asList(BarcodeFormat.QR_CODE, BarcodeFormat.CODE_39)

    lateinit var binding: ActivityAddTaskBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityAddTaskBinding.inflate(layoutInflater)
        val root = binding.root
        setContentView(root)

        setSupportActionBar(binding.toolbar)
        leadData = intent?.getParcelableExtra<LeadData>(Constants.LEAD_DATA) as LeadData

        supportActionBar?.apply {
            title = ""
            // show back button on toolbar
            // on back button press, it will navigate to parent activity
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }
        barcodeView = binding.includedContent.barcodeScanner

        initScanner()
        initUI()
        setObserver()
    }

    private fun initUI() {

        viewModel = ViewModelProvider(this)[ProductViewModel::class.java]

        binding.includedContent.btnUploadInvoice.setOnClickListener {
            if (checkCameraPermission()) {
                loadBottomSheetDialog()
            }
        }

        binding.includedContent.btnProceed.setOnClickListener {
/*
            if (Utils.instance.validateCustomerName(binding.includedContent.edtCustomerName) &&
                Utils.instance.validateWhatsAppNumber(binding.edtCustomerWhatsappNumber) &&
                Utils.instance.validatePhoneNumber(binding.edtCustomerMobileNumber) &&
                Utils.instance.validateAddress1EditText(binding.edtCustomerAddressLine1) &&
                Utils.instance.validateAddress2EditText(binding.edtCustomerAddressLine2) &&
                Utils.instance.validateCityEditText(binding.edtCustomerCity) &&
                Utils.instance.validateLandmarkEditText(binding.edtCustomerAddressLandmark) *//*&&*//*
            ) {
            }*/

            /*val jsonObject = JsonObject()
            jsonObject.addProperty("invoice_url", invoiceUrl)
            jsonObject.addProperty("purchase_at", leadData.created_at)
            jsonObject.addProperty("in_warranty", leadData.i)
            jsonObject.addProperty("in_warranty", i.invoice_url)
            jsonObject.addProperty("scanned_barcode", i.scanned_barcode)*/
        }

        binding.includedContent.edtCustomerDesc.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if(s!!.isEmpty()){
                    binding.includedContent.tvErrorDesc.visibility = View.VISIBLE
                }else{
                    binding.includedContent.tvErrorDesc.visibility = View.GONE
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //  validationUtil()
                if(s!!.isEmpty()){
                    binding.includedContent.tvErrorDesc.visibility = View.VISIBLE
                }else{
                    binding.includedContent.tvErrorDesc.visibility = View.GONE
                }

                val btn = binding.includedContent.btnUploadInvoiceFile.text.toString();
                if (currentScannedProduct != null && !btn.isEmpty()) {

                    binding.includedContent.btnProceed.visibility = View.VISIBLE


                } else {

                }
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                if(count == 0){
                    binding.includedContent.tvErrorDesc.visibility = View.VISIBLE
                }else {
                    binding.includedContent.tvErrorDesc.visibility = View.GONE
                }
            }
        })
    }

    private fun setObserver() {
        viewModel.qrCodeData.observe(this, this::onValidateQRCode)
        viewModel.invoiceUploadData.observe(this, this::onFileUploaded)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initScanner() {
        // Request camera permissions
        if (isCameraPermissionGranted()) {
            val formats: Collection<BarcodeFormat> =
                Arrays.asList(BarcodeFormat.QR_CODE, BarcodeFormat.CODE_39)

            barcodeView?.barcodeView?.decoderFactory = DefaultDecoderFactory(formats)
            barcodeView?.initializeFromIntent(intent)

            barcodeView?.decodeContinuous(callback)
            barcodeView?.barcodeView?.cameraSettings?.isAutoFocusEnabled = true
            beepManager = BeepManager(this)

        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                REQUEST_CAMERA_PERMISSION
            )
        }
    }

    private val callback: BarcodeCallback = object : BarcodeCallback {
        override fun barcodeResult(result: BarcodeResult) {
            if (result.text == null) { // Prevent duplicate scans
                return
            }
            val resultBarCode = result.text
            val currentScanner =
                resultBarCode.replace("http://", "").replace("http:// www.", "").replace("www.", "")


            barcodeView!!.setStatusText(currentScanner)
            beepManager!!.playBeepSoundAndVibrate()

            println("Scanned Code: " + result.text)
            println("Scanned Code: " + currentScanner)
            // writeScancodeEvent(result.text)

            /*var employeesWithUniqueNames = CommonUtils.productData.distinctBy { it.scanned_barcode }

            fun add(input: CommonUtils.productData) {
                list?.plusAssign(input.scanned_barcode)
            }
            println("listlist"+list)
*/
            if (currentScannedProduct1.equals(currentScanner)) {

                currentScannedProduct = null

                /* val builder = AlertDialog.Builder(this@ScanQrActivity)
                 builder.setMessage("QRCode Already Exits!")
                 builder.show()*/

                Utils.instance.popupPinUtil(
                    this@AddTaskActivity,
                    "QRCode Already Exits!",
                    "",

                    true
                )

                binding.includedContent.dummyQRView.visibility = View.INVISIBLE
                binding.includedContent.barcodeScanner.visibility = View.VISIBLE

                val r = Runnable {
                    barcodeView?.resume()
                }
                Handler().postDelayed(r, 1000)

            } else {
                binding.includedContent.tvErrorQrcode.visibility = View.GONE
                binding.includedContent.dummyQRView.visibility = View.VISIBLE
                binding.includedContent.barcodeScanner.visibility = View.GONE


                viewModel.hitValidateQRApi(currentScanner)
                currentScannedProduct = currentScanner

            }

            barcodeView?.pause()
            val r = Runnable {
                barcodeView?.resume()
            }
            Handler().postDelayed(r, 2000)

            //   validationUtil()
        }

        override fun possibleResultPoints(resultPoints: List<ResultPoint>) {}
    }


    private fun onValidateQRCode(resources: Resource<ValidateProductResponse>) {
        when (resources.status) {
            Status.LOADING -> {
                //showLoadingUI()
                binding.includedContent.tvErrorQrcode.visibility = View.VISIBLE

            }
            Status.ERROR -> {
                //hideLoadingUI()
                val r = Runnable {
                    barcodeView?.resume()
                }
                Handler().postDelayed(r, 2000)
                binding.includedContent.tvErrorQrcode.visibility = View.VISIBLE
                binding.includedContent.dummyQRView.visibility = View.INVISIBLE
                binding.includedContent.barcodeScanner.visibility = View.VISIBLE
                //SHOW POPUP

                Utils.instance.popupPinUtil(
                    this@AddTaskActivity,
                    resources.error?.message.toString(),
                    null,
                    false
                )
            }
            else -> {
               // hideLoadingUI()

                val data = resources.data

                data.let {
                    if (it?.success == true) {
                        //ADD IMAGE OF QR CODE and stop scanner
                        binding.includedContent.dummyQRView.visibility = View.VISIBLE
                        binding.includedContent.barcodeScanner.visibility = View.GONE
                        binding.includedContent.tvErrorQrcode.visibility = View.GONE

                        Utils.instance.popupPinUtil(
                            this@AddTaskActivity,
                            "Product scanned successfully",
                            "",
                            true
                        )

                        val edtDesc = binding.includedContent.edtCustomerDesc.text.toString()
                        val btnFile = binding.includedContent.btnUploadInvoiceFile.text.toString()
                        if (!btnFile.isEmpty() && !edtDesc.isEmpty()) {

                        } else {
                            // Toast.makeText(this, "No", Toast.LENGTH_SHORT).show()
                        }

                    } else {
                        binding.includedContent.tvErrorQrcode.visibility = View.VISIBLE
                        it?.message?.let { msg ->
                            Utils.instance.popupPinUtil(
                                this@AddTaskActivity,
                                msg,
                                null,
                                false
                            )
                        }

                        binding.includedContent.dummyQRView.visibility = View.INVISIBLE
                        binding.includedContent.barcodeScanner.visibility = View.VISIBLE

                        val r = Runnable {
                            barcodeView?.resume()
                        }
                        Handler().postDelayed(r, 1000)
                        /* val r = Runnable {
                             barcodeView?.resume()
                         }
                         Handler().postDelayed(r, 1000)*/
                    }
                }.run { }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        barcodeView!!.resume()

    }

    override fun onPause() {
        super.onPause()
        barcodeView!!.pause()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return barcodeView!!.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event)
    }

    private fun isCameraPermissionGranted(): Boolean {
        val selfPermission =
            ContextCompat.checkSelfPermission(baseContext, Manifest.permission.CAMERA)
        return selfPermission == PackageManager.PERMISSION_GRANTED
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

        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION),
            PERMISSION_ID
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
                // loadBottomSheetDialog()
            }
        }


        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (isCameraPermissionGranted()) {
                barcodeView?.barcodeView?.decoderFactory = DefaultDecoderFactory(formats)
                barcodeView?.initializeFromIntent(intent)
                barcodeView?.decodeContinuous(callback)
                beepManager = BeepManager(this)
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

       /* if (requestCode == PERMISSION_ID) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getLastLocation()
            }
        }*/
    }

    private fun loadBottomSheetDialog() {
        val fragment = CameraBottomSheetDialogFragment().newInstance()
        fragment.bottomSheetItemClick = this
        fragment.show(supportFragmentManager, "Bottomsheet_Media_Selection")
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

            resultUri = Utils.instance.reSizeImg(image,this)
            val  edtDesc = binding.includedContent.edtCustomerDesc.text.toString()
            if (!edtDesc.isEmpty() && currentScannedProduct != null) {
                //  Toast.makeText(this, "Yes", Toast.LENGTH_SHORT).show()


            } else {

            }

            //validationUtil()

        } catch (ex: java.lang.Exception) {
            ex.printStackTrace()
        }
        buildMutilPart()
        //binding.includedContent.ivUploadImage.setImageURI(resultUri)
        //binding.ivSelectedImage.setImageURI(resultUri)
    }


    private fun buildMutilPart() {
        val params = MultipartBody.Builder().setType(MultipartBody.FORM)


        if (!resultUri?.path.isNullOrBlank()) {
            val newUri = Uri.parse(resultUri?.path)
            val files = File(newUri.path ?: "")
            val requestFile: RequestBody = files.asRequestBody("multipart/form-data".toMediaType())
            params.addFormDataPart("file", files.name, requestFile)

        }

        viewModel.hitUploadFile(params.build(),
        )
    }
    private fun onFileUploaded(resources: Resource<UploadFileResponse>) {
        when (resources.status) {
            Status.LOADING -> {
               // showLoadingUI()

            }
            Status.ERROR -> {
                //hideLoadingUI()

                Utils.instance.popupPinUtil(
                    this@AddTaskActivity,
                    resources.error?.message.toString(),
                    null,
                    false
                )
            }
            else -> {
               // hideLoadingUI()

                val data = resources.data

                data.let {
                    if (it?.success == true) {
                        invoiceUrl = it.data.invoice_url

                        binding.includedContent.tvErrorUploadInvoice.visibility = View.GONE
                        binding.includedContent.btnUploadInvoiceFile.visibility = View.VISIBLE
                        binding.includedContent.btnUploadInvoiceFile.setText(" ${invoiceUrl.toString()}")

                    } else {
                        it?.message?.let { msg ->
                            Utils.instance.popupPinUtil(
                                this@AddTaskActivity,
                                msg,
                                null,
                                true
                            )
                        }
                        val r = Runnable {
                            barcodeView?.resume()
                        }
                        Handler().postDelayed(r, 1000)
                    }
                }.run { }
            }
        }
    }
}