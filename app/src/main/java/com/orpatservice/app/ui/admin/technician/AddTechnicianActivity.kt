package com.orpatservice.app.ui.admin.technician

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.MenuItem
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.orpatservice.app.R
import com.orpatservice.app.databinding.ActivityAddTechnicianBinding
import com.orpatservice.app.ui.data.Resource
import com.orpatservice.app.ui.data.Status
import com.orpatservice.app.ui.data.model.TechnicianData
import com.orpatservice.app.ui.data.model.TechnicianResponse
import com.orpatservice.app.utils.Utils
import com.tapadoo.alerter.Alerter
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileDescriptor
import java.io.IOException

const val ADD = "ADD"
const val UPDATE = "UPDATE"
const val PARCELABLE_TECHNICIAN = "PARCELABLE_TECHNICIAN"
const val MY_PERMISSIONS_WRITE_READ_REQUEST_CODE = 1000

class AddTechnicianActivity : AppCompatActivity(), View.OnClickListener,
    CameraBottomSheetDialogFragment.BottomSheetItemClick {
    private lateinit var binding: ActivityAddTechnicianBinding
    private lateinit var viewModel: TechniciansViewModel


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

        binding.includedContent.btnSubmitMobile.setOnClickListener(this)
        binding.includedContent.vImage.setOnClickListener(this)

        if (intent.getStringExtra(UPDATE).equals(UPDATE)) {
            bindUpdateTechnician()

        }

    }

    private var imageUrl : String? = ""
    private var technicianID : Int? = 0
    private fun bindUpdateTechnician() {
        val technicianData = intent.getParcelableExtra<TechnicianData>(
            PARCELABLE_TECHNICIAN
        )
        binding.includedContent.etFirstName.setText(technicianData?.first_name)
        binding.includedContent.etLastName.setText(technicianData?.last_name)
        binding.includedContent.etMobileNo.setText(technicianData?.mobile)
        binding.includedContent.etPinCode.setText(technicianData?.area)
        imageUrl = technicianData?.image
        technicianID = technicianData?.id

        //binding.includedContent.ivUploadImage

    }

    private fun loadAddTechnician(): Observer<Resource<TechnicianResponse>> {
        return Observer { it ->
            when (it?.status) {
                Status.LOADING -> {
                    binding.cpiLoading.visibility = View.VISIBLE

                }
                Status.ERROR -> {
                    binding.cpiLoading.visibility = View.GONE
                    Alerter.create(this@AddTechnicianActivity)
                        .setTitle("")
                        .setText("" + it.error?.message.toString())
                        .setBackgroundColorRes(R.color.orange)
                        .setDuration(1000)
                        .show()
                }
                else -> {
                    binding.cpiLoading.visibility = View.GONE
                    val data = it?.data

                    data?.let {
                        if (it.success) {
                            Alerter.create(this@AddTechnicianActivity)
                                .setTitle("")
                                .setText(it.message)
                                .setBackgroundColorRes(R.color.orange)
                                .setDuration(1000)
                                .show()
                            finish()

                        }
                    } ?: run {
                        Alerter.create(this@AddTechnicianActivity)
                            .setTitle("")
                            .setText(it.data?.message.toString())
                            .setBackgroundColorRes(R.color.orange)
                            .setDuration(1000)
                            .show()
                    }
                }
            }
        }
    }

    private fun hitAPIAddTechnician(){
        val params = MultipartBody.Builder().setType(MultipartBody.FORM)

        params.addFormDataPart("first_name",binding.includedContent.etFirstName.text.toString())
        params.addFormDataPart("last_name",binding.includedContent.etLastName.text.toString())
        params.addFormDataPart("mobile",binding.includedContent.etMobileNo.text.toString())
        params.addFormDataPart("pincode",binding.includedContent.etPinCode.text.toString())

        val files = File(resultUri?.path ?: "")
        val requestFile: RequestBody = files.asRequestBody("multipart/form-data".toMediaType())
        params.addFormDataPart("image", files.name, requestFile)

        viewModel.hitAPIAddTechnician(params.build()
        ).observe(this, loadAddTechnician())
    }

    private fun hitAPIUpdateTechnician(){
        val params = MultipartBody.Builder().setType(MultipartBody.FORM)

        params.addFormDataPart("first_name",binding.includedContent.etFirstName.text.toString())
        params.addFormDataPart("last_name",binding.includedContent.etLastName.text.toString())
        params.addFormDataPart("mobile",binding.includedContent.etMobileNo.text.toString())
        params.addFormDataPart("pincode",binding.includedContent.etPinCode.text.toString())

        val files = File(resultUri?.path ?: "")
        val requestFile: RequestBody = files.asRequestBody("multipart/form-data".toMediaType())
        params.addFormDataPart("image", files.name, requestFile)

        viewModel.hitAPIUpdateTechnician(params.build(),technicianID
        ).observe(this, loadAddTechnician())
    }

    private fun isValidAddTechnician(): Boolean {
        return (Utils.instance.validateFirstName(binding.includedContent.etFirstName)
                && Utils.instance.validateLastName(binding.includedContent.etLastName)
                && Utils.instance.validatePhoneNumber(binding.includedContent.etMobileNo))

    }

    private fun loadBottomSheetDialog() {
        val fragment = CameraBottomSheetDialogFragment().newInstance()
        fragment.bottomSheetItemClick = this
        fragment.show(supportFragmentManager, "Bottomsheet_Media_Selection")
    }

    override fun bottomSheetItemClick(clickAction: String?) {
        when (clickAction) {
            CAMERA -> {
                startImageCapture()
            }
            GALLERY -> {
                getImageGallery()
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

            resultUri = Utils.instance.reSizeImg(image)

        } catch (ex: java.lang.Exception) {
            ex.printStackTrace()
        }
        binding.includedContent.ivUploadImage.setImageURI(resultUri)
        //binding.ivSelectedImage.setImageURI(resultUri)
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
                loadBottomSheetDialog()
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

            R.id.v_image->{
                if (checkCameraPermission()) {
                    loadBottomSheetDialog()
                }
            }


        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

}