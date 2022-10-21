package com.orpatservice.app.ui.leads.service_center

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.orpatservice.app.R
import com.orpatservice.app.data.Resource
import com.orpatservice.app.data.Status
import com.orpatservice.app.data.model.TechnicianResponse
import com.orpatservice.app.data.model.login.SendHappyCodeResponse
import com.orpatservice.app.data.model.requests_leads.*
import com.orpatservice.app.databinding.*
import com.orpatservice.app.ui.leads.adapter.AllTechnicianAdapter
import com.orpatservice.app.ui.leads.customer_detail.CancelRequestResponse
import com.orpatservice.app.ui.leads.customer_detail.CustomerDetailsModel
import com.orpatservice.app.ui.leads.customer_detail.UpdateRequestResponse
import com.orpatservice.app.ui.leads.customer_detail.productListData
import com.orpatservice.app.ui.leads.new_lead_fragment.adapter.TechnicianTaskUpdateAdapter
import com.orpatservice.app.ui.leads.new_lead_fragment.new_lead_request.LeadList
import com.orpatservice.app.ui.leads.new_lead_fragment.new_lead_request.UpdatePartsRequestData
import com.orpatservice.app.ui.leads.technician.ValidateProductResponse
import com.orpatservice.app.ui.leads.technician.adapter.AdapterSectionRecycler
import com.orpatservice.app.ui.leads.technician.section.*
import com.orpatservice.app.ui.login.technician.OTPVerificationActivity
import com.orpatservice.app.utils.CommonUtils
import com.orpatservice.app.utils.Constants
import com.orpatservice.app.utils.DividerItemDecorator
import com.orpatservice.app.utils.Utils
import com.tapadoo.alerter.Alerter
import org.json.JSONException


class TechnicianTaskUpdateActivity : AppCompatActivity() , TextWatcher {
    private lateinit var binding: ContentTechnicianTaskUpdateBinding
    private lateinit var requestData: LeadData
    private var requestEnquiry: Int? = 0

    lateinit var customerDetailsViewModel: CustomerDetailsModel
    private  lateinit var bindingChild : UnderWarrantyPartsAdapterBinding
    private lateinit var alertDialogBuilder:Dialog
    private var radiobtn_yes: String?= null
    private var radiobtn_change_part_yes: String?= null
    private lateinit var technicianTaskAdapter: TechnicianTaskAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager
    private  lateinit var bindingAdapter : TechnicianTaskUpdateBinding
    private var alertDialog : Dialog? = null
    private lateinit var tv_resend_happy_code:Button
    private val editTextArray: ArrayList<EditText> = ArrayList(OTPVerificationActivity.NUM_OF_DIGITS)
    private val NUM_OF_DIGITS = 4
    private var numTemp = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ContentTechnicianTaskUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        customerDetailsViewModel = ViewModelProvider(this)[CustomerDetailsModel::class.java]

        setSupportActionBar(binding.toolbar)

        supportActionBar?.apply {
            title = ""
            // show back button on toolbar
            // on back button press, it will navigate to parent activity
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)

            val expandableListView = binding.includedContent.rvTaskCompleted

            requestData =
                intent?.getParcelableExtra<LeadData>(Constants.LEAD_DATA) as LeadData
            requestEnquiry =intent?.getIntExtra(Constants.POSITION, 0);


            initFormComponent()
            userDetailsUtil()
            //userClick()


            technicianTaskAdapter = TechnicianTaskAdapter(this@TechnicianTaskUpdateActivity,requestData.enquiries,requestData,
                itemClickListener = OnItemClick)

            linearLayoutManager = LinearLayoutManager(this@TechnicianTaskUpdateActivity)

            val dividerItemDecoration: RecyclerView.ItemDecoration =
                DividerItemDecorator(ContextCompat.getDrawable(this@TechnicianTaskUpdateActivity, R.drawable.rv_divider))

            binding.includedContent.rvTaskCompleted.apply {
                adapter = technicianTaskAdapter
                //addItemDecoration(dividerItemDecoration)
                layoutManager = linearLayoutManager

            }
        }
    }

    private fun userDetailsUtil() {

        binding.includedContent.tvCustomerNameValue.text = requestData.name
       // binding.tvRequestDateValue.text = requestData.service_center_assigned_at
        binding.includedContent.tvContactNumberValue.text = requestData.mobile
        binding.includedContent.tvPinCodeValue.text = requestData.pincode
        binding.includedContent.tvTvRequestIdValue.text = requestData.complain_id.toString()
        binding.includedContent.tvTimerValue.text = requestData.timer
        binding.includedContent.tvTimerValue.setTextColor(Color.parseColor(requestData.color_code))
        binding.includedContent.tvCustomerFullAddressValues.text = requestData.address1+""+" ,"+""+requestData.address2/*+""+" ,"+""+requestData.state*/
        binding.includedContent.tvContactNumber.setOnClickListener {
            openCallDialPad(requestData.mobile.toString())
        }
        binding.includedContent.tvTechnicianNameValues.text = requestData.technician?.first_name+""+" "+""+requestData.technician?.last_name
        binding.includedContent.tvTechnicianNumberValues.text = requestData.technician?.mobile
        binding.includedContent.tvTechnicianNumberValues.setOnClickListener {
            openCallDialPad(requestData.technician?.mobile.toString())
        }
        binding.includedContent.tvTechnicianNumber.setOnClickListener {
            openCallDialPad(requestData.technician?.mobile.toString())
        }
        val str = requestData.created_at
        val delimiter = " "
        val parts = str?.split(delimiter)

        binding.includedContent.tvRequestDateValue.text = parts?.get(0)+""+" "+""+ parts?.get(1)+""+" "+""+ parts?.get(2)+""+""+"\n"+ parts?.get(3)+""+" "+""+ parts?.get(4)+"\n"

        /*if(requestData.pending_parts_verification_status_count.equals("0")){
            binding.includedContent.btnSubmit.visibility = VISIBLE
            binding.includedContent.btnHideSubmit.visibility = GONE
        }else{
            binding.includedContent.btnSubmit.visibility = GONE
            binding.includedContent.btnHideSubmit.visibility = VISIBLE
        }*/

        if(requestData.pending_parts_verification_status_count.equals("0")){
            binding.includedContent.btnVerifyOtp.visibility = VISIBLE
            binding.includedContent.btnHideVerifyOtp.visibility = GONE
        }else{
            binding.includedContent.btnVerifyOtp.visibility = GONE
            binding.includedContent.btnHideVerifyOtp.visibility = VISIBLE
        }

        binding.includedContent.btnVerifyOtp.setOnClickListener {

            println("requestData.id.toString()"+requestData.id.toString())
            customerDetailsViewModel.hitAPITaskSendHappyCode(requestData.id.toString()).observe(this, loadHappyCodeData("verify"))

        }

        binding.includedContent.btnSubmit.setOnClickListener {

            println("requestData.id"+requestData.id)
            requestData.id?.let { it1 ->
                customerDetailsViewModel.hitAPIServiceMarkAsCompleteLead(
                    it1,"1234")
            }/*.observe(this, loadCompleteLead()*/
        }
    }

    override fun onResume() {
        super.onResume()
        initFormComponent()
    }

    private fun loadHappyCodeData(flag:String): Observer<Resource<SendHappyCodeResponse>> {
        return Observer { it ->
            when (it?.status) {
                Status.LOADING -> {
                    binding.cpiLoading.visibility = View.VISIBLE

                }
                Status.ERROR -> {
                    binding.cpiLoading.visibility = View.GONE
                    /* Alerter.create(this@TaskCompletedDetailsActivity)
                         .setTitle("")
                         .setText("" + it.error?.message.toString())
                         .setBackgroundColorRes(R.color.orange)
                         .setDuration(1000)
                         .show()*/

                    Utils.instance.popupPinUtil(this,
                        it.error?.message.toString(),
                        "",
                        false)

                }
                else -> {
                     binding.cpiLoading.visibility = View.GONE
                    val data = it?.data

                    data?.let {
                        if (it.success) {
                            if(flag == "verify") {
                                showOTPVarificationPopUp()
                            }else{
                                it.message.let { it1 ->
                                    Utils.instance.popupPinUtil(this,
                                        it1,
                                        "",
                                        true)
                                }
                                editTextArray.clear()
                            }

                              /*it.message.let { it1 ->
                                  Utils.instance.popupPinUtil(this,
                                      it1,
                                      "",
                                      true)
                              }*/
                            //alertDialog?.dismiss()
                            /* Handler(Looper.getMainLooper()).postDelayed({
                                 val intent = Intent(this, RequestLeadActivity::class.java)
                                 startActivity(intent)
                                 finish()
                             }, 3000)*/

                        }
                    } ?: run {

                        Utils.instance.popupPinUtil(this,
                            it.data?.message.toString(),
                            "",
                            false)
                        alertDialog?.dismiss()
                    }
                }
            }
        }

    }

    private fun showOTPVarificationPopUp() {

        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View = inflater.inflate(R.layout.otp_varification_popup_layout, null)

        val alertDialogBuilder = Dialog(this@TechnicianTaskUpdateActivity)
        alertDialogBuilder.setContentView(view)
        alertDialog = alertDialogBuilder
        val layout: ConstraintLayout = view.findViewById(R.id.cl_otp_layout)
        createOTPUI(layout)
        val btn_varify_otp = view.findViewById<Button>(R.id.btn_varify_otp)
        val subTitle = view.findViewById<TextView>(R.id.tv_subheading)
        tv_resend_happy_code = view.findViewById<Button>(R.id.tv_resend_happy_code)

        val close_btn = view.findViewById<ImageView>(R.id.close_btn)

        subTitle.text = "Please verify the code sent to your mobile number +91"+" "+requestData.mobile
        tv_resend_happy_code.setOnClickListener {

            customerDetailsViewModel.hitAPITaskSendHappyCode(requestData.id.toString()).observe(this, loadHappyCodeData("resend"))

        }
        close_btn.setOnClickListener {
            alertDialog!!.dismiss()
        }
        btn_varify_otp.setOnClickListener {
            validateOTP()
        }
       // alertDialogBuilder.setCancelable(false)
        alertDialogBuilder.show()

    }

    private fun validateOTP() {
     //   println("sizesizesize"+editTextArray.size)

      /*  for(i in editTextArray){
            println("toStringtoStringtoString"+i.text.toString())
        }*/
      //  println("editTextArray.size"+editTextArray[1].text.toString()+editTextArray[2].text.toString()+editTextArray[3].text.toString())
        (0 until editTextArray.size)
            .forEach { i ->
                if (editTextArray[i].text.isEmpty()) {
                    println("texttexttext"+editTextArray[i].text.toString())
                    Utils.instance.popupPinUtil(this,
                        getString(R.string.warning_enter_OTP),
                        "",
                        false)

                       // userClick()
                       // initFormComponent()
                        editTextArray.clear()
                        alertDialog?.dismiss()


                    return
                }
            }
        verifyOTPCode(testCodeValidity())
    }

    private fun testCodeValidity(): String {
        var verificationCode = ""
        for (i in editTextArray.indices) {
            val digit = editTextArray[i].text.toString().trim { it <= ' ' }
            verificationCode += digit
        }
        if (verificationCode.trim { it <= ' ' }.length == NUM_OF_DIGITS) {
            return verificationCode
        }
        return ""
    }

    private fun createOTPUI(views: View) {
        //create array

        val layout: ConstraintLayout = views as ConstraintLayout
        for (index in 0 until (layout.childCount)) {
            val view: View = layout.getChildAt(index)
            if (view is EditText) {
                editTextArray.add(index, view)
                editTextArray[index].addTextChangedListener(this@TechnicianTaskUpdateActivity)
            }
        }

        editTextArray[0].requestFocus() //After the views are initialized we focus on the first view

        (0 until editTextArray.size)
            .forEach { i ->
                editTextArray[i].setOnKeyListener { _, keyCode, event ->
                    if (keyCode == KeyEvent.KEYCODE_DEL && event.action == KeyEvent.ACTION_DOWN) {
                        //backspace
                        if (i != 0) { //Don't implement for first digit
                            editTextArray[i - 1].requestFocus()
                            editTextArray[i - 1].setSelection(editTextArray[i - 1].length())
                        }
                    }
                    false
                }
            }
        }

    private fun verifyOTPCode(verificationCode: String) {
        if (verificationCode.isNotEmpty()) {
            enableCodeEditTexts(false)
            //API trigger
            println("taskCompletedRequestData.id"+requestData.id + verificationCode)
            customerDetailsViewModel.hitAPITaskSendHappyCodeVerification(requestData.id.toString(),verificationCode).observe(this, loadHappyCodeVerificationData())

        }
    }

    private fun loadHappyCodeVerificationData(): Observer<Resource<UpdatePartsRequestData>> {
        return Observer { it ->
            when (it?.status) {
                Status.LOADING -> {
                    binding.cpiLoading.visibility = View.VISIBLE

                }
                Status.ERROR -> {
                    binding.cpiLoading.visibility = View.GONE

                   /* Utils.instance.popupPinUtil(this,
                        it.error?.message.toString(),
                        "",
                        false)
                    editTextArray.clear()
                    alertDialog?.dismiss()*/

                }
                else -> {
                    binding.cpiLoading.visibility = View.GONE
                    val data = it?.data

                    data?.let {
                        if (it.success) {

                            it.message.let { it1 ->
                                Utils.instance.popupPinUtil(this,
                                    it1,
                                    "",
                                    true)
                            }
                            alertDialog?.dismiss()
                            Handler(Looper.getMainLooper()).postDelayed({
                                val intent = Intent(this, RequestLeadActivity::class.java)
                                startActivity(intent)
                                finish()
                            }, 5000)

                        }else{
                            it.message.let { it1 ->
                                Utils.instance.popupPinUtil(this,
                                    it1,
                                    "",
                                    false)
                            }
                            for (i in 0 until editTextArray.size)
                                editTextArray[i].setText("")
                            enableCodeEditTexts(true)
                            tv_resend_happy_code.visibility = VISIBLE
                        }
                    } ?: run {


                        Utils.instance.popupPinUtil(this,
                            it.data?.message.toString(),
                            "",
                            false)
                        for (i in 0 until editTextArray.size)
                            editTextArray[i].setText("")
                        enableCodeEditTexts(true)
                        alertDialog?.dismiss()
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

    private val OnItemClick: (Int, View, TechnicianTaskUpdateBinding) -> Unit = {
            position, view,binding ->
        bindingAdapter = binding

        when (view.id) {
            R.id.tv_task_update -> {
                println("CommonUtils.warrantyListData.count()"+CommonUtils.warrantyListData.count())
                println("CommonUtils.imageData.count()"+CommonUtils.imageData.count())
                if(CommonUtils.warrantyListData.count() == CommonUtils.imageData.count()) {
                    val jsonObject = JsonObject()

                    try {

                        val jsArray = JsonArray()
                        for (i in CommonUtils.productListData) {
                            val jsonObj = JsonObject()
                            // jsonObj.addProperty("",i.part_ids)
                            // jsonObj.addProperty("",i.lead_enquiry_image_id)

                            jsonObj.addProperty(i.part_ids, i.lead_enquiry_image_id)
                            jsArray.add(jsonObj)

                        }

                        if (!requestData.enquiries[position].lead_enquiry_images.isEmpty()) {
                            jsonObject.addProperty("lead_enquiry_image_id", "")
                        } else {
                            jsonObject.addProperty("lead_enquiry_image_id", "null")
                        }

                        jsonObject.addProperty("in_warranty", radiobtn_yes)
                       // jsonObject.addProperty("is_any_part_changed", radiobtn_change_part_yes)
                        jsonObject.addProperty("other_reasons", "No reason")
                        jsonObject.add("part_ids", jsArray)


                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }

                    println("jsonObject" + jsonObject + "" + "" + "" + requestData.enquiries[position].id)
                    customerDetailsViewModel.hitTaskUpdateRequest(jsonObject ,requestData.enquiries[position].id)
                }else{

                    Utils.instance.popupPinUtil(this@TechnicianTaskUpdateActivity,
                        "Please Select Image!","",
                        false)
                }
            }
            R.id.tv_cancel_task_update -> {

                showCancelEnquiryPopUp(position)
            }

            R.id.radiobtn_yes -> {
                radiobtn_yes = "Yes"
            }
            R.id.radiobtn_no -> {
                radiobtn_yes = "No"
            }
            R.id.radiobtn_change_part_yes -> {
                radiobtn_change_part_yes = "Yes"
            }
            R.id.radiobtn_change_part_no -> {
                radiobtn_change_part_yes = "No"

            }
        }
    }

    private fun showCancelEnquiryPopUp(position: Int) {

        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View = inflater.inflate(R.layout.cancel_lead_popup_layout, null)
        val tv_cancel_lead_cancel = view.findViewById<TextView>(R.id.tv_cancel_lead_cancel)
        val tv_cancel_lead_ok = view.findViewById<TextView>(R.id.tv_cancel_lead_ok)
        val et_cancel_lead_reason = view.findViewById<EditText>(R.id.et_cancel_lead_reason)
        alertDialogBuilder = Dialog(this@TechnicianTaskUpdateActivity)
        alertDialogBuilder.setContentView(view)
        //alertDialogBuilder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialogBuilder.setCancelable(false)
        tv_cancel_lead_cancel.setOnClickListener {
            alertDialogBuilder.dismiss()
        }
        tv_cancel_lead_ok.setOnClickListener {
            if (Utils.instance.validateReason(et_cancel_lead_reason)) {
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
            jsonObject.addProperty("lead_id", requestData.id)
            jsonObject.addProperty("enquiry_id", requestData.enquiries[position].id)
            jsonObject.addProperty("cancellation_type", type)

        } catch (e: JSONException) {
            e.printStackTrace()
        }

        customerDetailsViewModel.hitTaskCancelRequest(jsonObject)
            .observe(this, this::getCancelRequestTask)

    }

    private fun getCancelRequestTask(resources: Resource<CancelRequestResponse>) {
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
                           /* val intent = Intent(this, RequestLeadActivity::class.java)
                            startActivity(intent)*/
                            onBackPressed()
                            finish()
                        }, 5000)

                    } else {

                    }
                }
            }
        }
    }
   /* private fun loadCompleteLead(): Observer<Resource<RequestLeadResponse>> {
        return Observer { it ->
            when (it?.status) {
                Status.LOADING -> {
                     binding.cpiLoading.visibility = View.VISIBLE

                }
                Status.ERROR -> {
                    //isLoading = false
                     binding.cpiLoading.visibility = View.GONE

                    Utils.instance.popupUtil(this@TechnicianTaskUpdateActivity,
                        it.error?.message.toString(),"",
                        false)
                   *//* Alerter.create(this@TechnicianTaskUpdateActivity)
                        .setTitle("")
                        .setText("" + it.error?.message.toString())
                        .setBackgroundColorRes(R.color.orange)
                        .setDuration(1000)
                        .show()*//*

                }
                else -> {
                     binding.cpiLoading.visibility = View.GONE
                    val data = it?.data

                    data?.let {
                        if (it.success) {
                            //confirmationDialog(it.message)
                            *//* Alerter.create(this@AllTechnicianActivity)
                                 .setTitle("")
                                 .setText(data.message.toString())
                                 .setBackgroundColorRes(R.color.orange)
                                 .setDuration(1000)
                                 .show()*//*

                            it.message?.toString()?.let { it1 ->
                                Utils.instance.popupUtil(
                                    this,
                                    it1,
                                    "",
                                    true
                                )
                            }
                            Handler(Looper.getMainLooper()).postDelayed({
                                val intent = Intent(this, RequestLeadActivity::class.java)
                                startActivity(intent)
                                finish()

                            }, 3000)
                        }
                    } ?: run {
                    }
                }
            }
        }
    }
*/

    private fun initFormComponent() {

        customerDetailsViewModel.submitLeadData.observe(this, this::onTaskUpload)

        customerDetailsViewModel.updatePartsLeadData.observe(this, this::loadCompleteLead)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()

                CommonUtils.imageData.clear()
                CommonUtils.warrantyListData.clear()

                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

 /*   private val onItemClickListener: (Int, View, UnderWarrantyPartsAdapterBinding, Int, FragmentTaskUpdateBinding) -> Unit =
        { position, view, binding,parentPosition ,groupBinding->
            bindingChild =  binding

            when (view.id) {
                R.id.check_warranty_parts -> {
                    val warrantyPartData = productListData(requestData.enquiries[parentPosition].warranty_parts[position].id.toString())
                    CommonUtils.productListData.add(warrantyPartData)


                }
                R.id.btn_update ->{
                    in_warranty = requestData.enquiries[parentPosition].warranty_parts[position].in_warranty
                    val jsonObject = JsonObject()

                    try {

                        val jsArray  = JsonArray()
                        for (i in CommonUtils.productListData){
                            val jsonObj = JsonObject()
                            jsonObj.addProperty("part_ids",i.part_ids)
                            jsArray.add(jsonObj)
                        }

                        if(groupBinding.radiobtnChangePartYes.isChecked){
                            jsonObject.add("part_ids",jsArray)
                        }else{
                            jsonObject.add("part_ids",null)
                        }

                        if(groupBinding.radiobtnYes.isChecked){
                            selectedWarranty = "Yes"
                        }else{
                            selectedWarranty = "No"
                        }
                        if(!requestData.enquiries[parentPosition].lead_enquiry_images.isEmpty()){
                            jsonObject.addProperty("lead_enquiry_image_id", requestData.enquiries[parentPosition].lead_enquiry_images[0].id)
                        }else{
                            jsonObject.addProperty("lead_enquiry_image_id", "null")
                        }

                        jsonObject.addProperty("in_warranty", requestData.enquiries[parentPosition].in_warranty)
                        jsonObject.addProperty("is_any_part_changed", selectedWarranty)
                        jsonObject.addProperty("other_reasons", "No reason")


                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }

                    println("jsonObject"+jsonObject+""+""+""+requestData.enquiries[parentPosition].id)
                    customerDetailsViewModel.hitTaskUpdateRequest(jsonObject ,requestData.enquiries[parentPosition].id)

                }
                R.id.radio_btn_change_part ->{
                    if(groupBinding.radiobtnChangePartYes.isClickable){
                        selectedWarranty = "Yes"
                    }else{
                        selectedWarranty = "No"
                    }
                }
            }
        }*/

    private fun loadCompleteLead(resources: Resource<UpdatePartsRequestData>) {
        when (resources.status) {
            Status.LOADING -> {
                binding.cpiLoading.visibility = View.VISIBLE
            }

            Status.ERROR -> {
                binding.cpiLoading.visibility = View.GONE
                Utils.instance.popupPinUtil(this@TechnicianTaskUpdateActivity,
                    resources.error?.message.toString(),"",
                    false)
                /*Alerter.create(this)
                    .setText(resources.error?.message.toString())
                    .setBackgroundColorRes(R.color.orange)
                    .setDuration(1500)
                    .show()*/
            }
            else -> {
                binding.cpiLoading.visibility = View.GONE

                val data = resources.data

                data.let {
                    if(it?.success == true){
                        alertDialog?.dismiss()
                        Utils.instance.popupUtil(this@TechnicianTaskUpdateActivity,
                            it.message,"",
                            true)

                        //bindingAdapter.tvTaskUpdate.visibility = GONE
                        //bindingAdapter.tvHideTaskUpdate.visibility = View.VISIBLE

                       // println("it.data.data"+it.data.data.get(0));

                        Handler(Looper.getMainLooper()).postDelayed({
                            val intent = Intent(this, RequestLeadActivity::class.java)
                            startActivity(intent)
                            //onBackPressed()
                            finish()

                        }, 5000)

                    }else{
                        it?.message?.let { msg ->
                            Utils.instance.popupPinUtil(this@TechnicianTaskUpdateActivity,
                                msg,
                                "",
                                false)
                        }
                    }
                }.run {  }
            }
        }
    }

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
        println("beforeTextChanged"+"beforeTextChanged"+s)

        numTemp = s.toString()
        println("numTempnumTemp"+numTemp)
    }//store the previous digit

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        println("onTextChanged"+"onTextChanged"+s)
    }

    override fun afterTextChanged(s: Editable) {
        println("afterTextChanged"+"afterTextChanged"+s)
        (0 until editTextArray.size)
            .forEach { i ->
                if (s === editTextArray[i].editableText) {

                    if (s.isBlank()) {
                        return
                    }
                    if (s.length >= 2) {//if more than 1 char
                        val newTemp = s.toString().substring(s.length - 1, s.length)
                        if (newTemp != numTemp) {
                            editTextArray[i].setText(newTemp)
                        } else {
                            editTextArray[i].setText(s.toString().substring(0, s.length - 1))
                        }
                    } else if (i != editTextArray.size - 1) { //not last char
                        editTextArray[i + 1].requestFocus()
                        editTextArray[i + 1].setSelection(editTextArray[i + 1].length())
                        return
                    } else {
                        //THIS BLOCK FOR AUTOMATIC OTP VERIFY API TRIGGER BUT FOR NOW WE DO IT MANUALLY
                        //will verify code the moment the last character is inserted and all digits have a number
                        //verifyCode(testCodeValidity())
                    }
                }
            }
    }

    private fun onTaskUpload(resources: Resource<UpdateRequestResponse>) {
        when (resources.status) {
            Status.LOADING -> {
                binding.cpiLoading.visibility = View.VISIBLE
            }

            Status.ERROR -> {
                binding.cpiLoading.visibility = View.GONE
                Utils.instance.popupPinUtil(this@TechnicianTaskUpdateActivity,
                    resources.error?.message.toString(),"",
                    false)
                /*Alerter.create(this)
                    .setText(resources.error?.message.toString())
                    .setBackgroundColorRes(R.color.orange)
                    .setDuration(1500)
                    .show()*/
            }
            else -> {
                binding.cpiLoading.visibility = View.GONE

                val data = resources.data

                data.let {
                    if(it?.success == true){
                        Utils.instance.popupPinUtil(this@TechnicianTaskUpdateActivity,
                            it.message,"",
                            true)
                        CommonUtils.imageData.clear()
                        CommonUtils.warrantyListData.clear()

                        bindingAdapter.tvTaskUpdate.visibility = GONE
                        bindingAdapter.tvHideTaskUpdate.visibility = View.VISIBLE

                        //println("it.data.data"+it.data.data.get(0));

                        /*if(data?.data?.pending_lead_enqury_detail_count.equals("0")){
                            binding.includedContent.btnSubmit.visibility = VISIBLE
                            binding.includedContent.btnHideSubmit.visibility = GONE
                        }else{

                            binding.includedContent.btnSubmit.visibility = GONE
                            binding.includedContent.btnHideSubmit.visibility = VISIBLE
                        }
*/

                        if(data?.data?.pending_lead_enqury_detail_count.equals("0")){
                            binding.includedContent.btnVerifyOtp.visibility = VISIBLE
                            binding.includedContent.btnHideVerifyOtp.visibility = GONE
                        }else{
                            binding.includedContent.btnVerifyOtp.visibility = GONE
                            binding.includedContent.btnHideVerifyOtp.visibility = VISIBLE
                        }
                      /*  Handler(Looper.getMainLooper()).postDelayed({
                            val intent = Intent(this, RequestLeadActivity::class.java)
                            startActivity(intent)
                            finish()
                        }, 3000)*/

                    }else{
                        it?.message?.let { msg ->
                            Utils.instance.popupPinUtil(this@TechnicianTaskUpdateActivity,
                                msg,
                                null,
                                false)
                        }
                    }
                }.run {  }
            }
        }
    }

    private fun enableCodeEditTexts(enable: Boolean) {
        for (i in 0 until editTextArray.size)
            editTextArray[i].isEnabled = enable
    }

   /* override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
        numTemp = s.toString()
    }//store the previous digit

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }

    override fun afterTextChanged(s: Editable) {

        (0 until editTextArray.size)
            .forEach { i ->
                if (s === editTextArray[i].editableText) {

                    if (s.isBlank()) {
                        return
                    }
                    if (s.length >= 2) {//if more than 1 char
                        val newTemp = s.toString().substring(s.length - 1, s.length)
                        if (newTemp != numTemp) {
                            editTextArray[i].setText(newTemp)
                        } else {
                            editTextArray[i].setText(s.toString().substring(0, s.length - 1))
                        }
                    } else if (i != editTextArray.size - 1) { //not last char
                        editTextArray[i + 1].requestFocus()
                        editTextArray[i + 1].setSelection(editTextArray[i + 1].length())
                        return
                    } else {
                        //THIS BLOCK FOR AUTOMATIC OTP VERIFY API TRIGGER BUT FOR NOW WE DO IT MANUALLY
                        //will verify code the moment the last character is inserted and all digits have a number
                        //verifyCode(testCodeValidity())
                    }
                }
            }
    }*/
}
