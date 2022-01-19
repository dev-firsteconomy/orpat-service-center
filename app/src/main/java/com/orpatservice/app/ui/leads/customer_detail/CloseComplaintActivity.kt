package com.orpatservice.app.ui.leads.customer_detail

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
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.orpatservice.app.R
import com.orpatservice.app.data.Resource
import com.orpatservice.app.data.Status
import com.orpatservice.app.data.model.AddTechnicianResponse
import com.orpatservice.app.data.model.RepairPartResponse
import com.orpatservice.app.data.model.RepairParts
import com.orpatservice.app.data.model.SaveEnquiryResponse
import com.orpatservice.app.data.model.login.LoginBaseData
import com.orpatservice.app.data.model.login.Technician
import com.orpatservice.app.data.sharedprefs.SharedPrefs
import com.orpatservice.app.databinding.ActivityCloseComplaintBinding
import com.orpatservice.app.ui.admin.technician.MY_PERMISSIONS_WRITE_READ_REQUEST_CODE
import com.orpatservice.app.ui.admin.technician.TechniciansViewModel
import com.orpatservice.app.ui.leads.customer_detail.adapter.RepairPartAdapter
import com.orpatservice.app.ui.technician.HappyCodeActivity
import com.orpatservice.app.utils.Constants
import com.orpatservice.app.utils.Utils
import com.tapadoo.alerter.Alerter
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileDescriptor
import java.io.IOException


class CloseComplaintActivity : AppCompatActivity(), AdapterView.OnItemClickListener {
    private lateinit var binding: ActivityCloseComplaintBinding
    private lateinit var viewModel: TechniciansViewModel

    private lateinit var repairPartAdapter: RepairPartAdapter

    private val repairPartsList: ArrayList<RepairParts> = ArrayList()
    private val suggestPartsList: ArrayList<RepairParts> = ArrayList()

    private val onItemClickListener: (Int, View) -> Unit = { position, view ->
        when (view.id) {
            R.id.iv_cancel -> {
                repairPartsList.also {
                    it.removeAt(position)
                }
                repairPartAdapter.notifyItemRemoved(position)
            }
            R.id.btn_submit -> {
                if (repairPartsList.isNotEmpty()){
                    hitAPIAddTechnician()

                }else{
                    Alerter.create(this@CloseComplaintActivity)
                        .setTitle("")
                        .setText("Select Parts")
                        .setBackgroundColorRes(R.color.orange)
                        .setDuration(1000)
                        .show()
                }
                /*Intent(this, HappyCodeActivity::class.java).apply {
                    putExtra(Constants.LEADS_ID,intent.getIntExtra(Constants.LEADS_ID,0))
                    putExtra(Constants.COMPLAINT_ID,intent.getStringExtra(Constants.COMPLAINT_ID))
                    startActivity(this)
                }*/
            }
            R.id.v_image -> {
                if (checkCameraPermission()) {
                    startImageCapture()
                }
            }
        }


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCloseComplaintBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d("TOTAL_ENQUIRY",""+intent.getIntExtra(Constants.TOTAL_ENQUIRY,0))

        viewModel = ViewModelProvider(this)[TechniciansViewModel::class.java]

        binding.includedContent.btnSubmit.setOnClickListener {
            onItemClickListener(
                0,
                binding.includedContent.btnSubmit
            )
        }
        binding.includedContent.vImage.setOnClickListener{
            onItemClickListener(
                0,
                binding.includedContent.vImage
            )
        }

        supportActionBar?.apply {
            title = ""
            // show back button on toolbar
            // on back button press, it will navigate to parent activity
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        repairPartAdapter = RepairPartAdapter(repairPartsList, onItemClickListener)

        binding.includedContent.rvRepairParts.apply {
            adapter = repairPartAdapter
        }

        binding.includedContent.mtvParts.threshold = 2
        binding.includedContent.mtvParts.onItemClickListener = this

        binding.includedContent.mtvParts.addTextChangedListener(textWatcher)


    }

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun afterTextChanged(text: Editable?) {
            if (binding.includedContent.mtvParts.isPerformingCompletion){

            }else{
                if (text.toString().isNotEmpty() && text.toString().trim().length > 2){
                    hitAPIPartsData(text.toString().trim())
                }

            }

        }

    }

    private fun hitAPIPartsData(partsText : String){
        viewModel.hitAPIParts(partsText).observe(this,loadPartsData())
    }
    private fun loadPartsData(): Observer<Resource<RepairPartResponse>> {
        return Observer { it ->
            when (it?.status) {
                Status.LOADING -> {
                    binding.cpiLoading.visibility = View.VISIBLE

                }
                Status.ERROR -> {
                    binding.cpiLoading.visibility = View.GONE

                    Alerter.create(this@CloseComplaintActivity)
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
                            suggestPartsList.clear()
                            suggestPartsList.addAll(it.data)

                            this.runOnUiThread{
                              val  partsArrayAdapter =
                                    ArrayAdapter(this, android.R.layout.simple_list_item_1, suggestPartsList)
                                binding.includedContent.mtvParts.setAdapter(partsArrayAdapter)
                                binding.includedContent.mtvParts.showDropDown()


                            }

                        }
                    } ?: run {


                        Alerter.create(this@CloseComplaintActivity)
                            .setTitle("")
                            .setText("it.data?.message.toString()")
                            .setBackgroundColorRes(R.color.orange)
                            .setDuration(1000)
                            .show()
                    }
                }
            }
        }
    }

    private fun hitAPIAddTechnician() {
        val gson = Gson()
        val jsonUserDetails = SharedPrefs.getInstance().getString(Constants.SERVICE_CENTER, "")
        val userDetails = gson.fromJson(jsonUserDetails, LoginBaseData::class.java) as LoginBaseData
        val technician = userDetails.technician as Technician

        val params = MultipartBody.Builder().setType(MultipartBody.FORM)
        params.addFormDataPart("replacement_part_id", repairPartsList[0].id.toString())

        if (!resultUri?.path.isNullOrBlank()) {
            val files = File(resultUri?.path ?: "")
            val requestFile: RequestBody = files.asRequestBody("multipart/form-data".toMediaType())
            params.addFormDataPart("replacement_image", files.name, requestFile)
        }

        viewModel.hitAPIRepairPartTechnician(
            params.build(),
            intent.getIntExtra(Constants.LEADS_ID,0).toString(),
            intent.getIntExtra(Constants.COMPLAINT_ID,0).toString()
        ).observe(this, loadAddTechnicianRepairPart())
    }

    private fun loadAddTechnicianRepairPart(): Observer<Resource<SaveEnquiryResponse>> {
        return Observer { it ->
            when (it?.status) {
                Status.LOADING -> {
                    binding.cpiLoading.visibility = View.VISIBLE

                }
                Status.ERROR -> {
                    binding.cpiLoading.visibility = View.GONE
                    Alerter.create(this@CloseComplaintActivity)
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
                            Toast.makeText(this, "" + it.message, Toast.LENGTH_LONG).show()
                            //Check if total enquiry count is one then navigate to happy code screen or previous activity
                            if (intent.getIntExtra(Constants.TOTAL_ENQUIRY,0) !=1){
                                val intent = Intent()
                                intent.putExtra(Constants.TOTAL_ENQUIRY, 1)
                                setResult(Activity.RESULT_OK,intent)
                                finish()

                            }else{
                                Intent(this, HappyCodeActivity::class.java).apply {
                                    putExtra(Constants.LEADS_ID,intent.getIntExtra(Constants.LEADS_ID,0))
                                    //putExtra(Constants.COMPLAINT_ID,intent.getStringExtra(Constants.COMPLAINT_ID))
                                    startActivity(this)
                                }

                            }

                        }
                    } ?: run {
                        Alerter.create(this@CloseComplaintActivity)
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
                startImageCapture()
            }
        }
    }
    private var mCurrentCaptureImage: String? = null
    private var resultUri: Uri? = null

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

    override fun onItemClick(p0: AdapterView<*>?, view: View?, position: Int, id: Long) {
        repairPartsList.clear()

        repairPartsList.add(suggestPartsList[position])
        repairPartAdapter.notifyItemInserted(position)

        binding.includedContent.mtvParts.setText("")

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