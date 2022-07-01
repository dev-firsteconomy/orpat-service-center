package com.orpatservice.app.ui.leads.service_center

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.EditText
import android.widget.ExpandableListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.orpatservice.app.R
import com.orpatservice.app.data.Resource
import com.orpatservice.app.data.Status
import com.orpatservice.app.data.model.TechnicianResponse
import com.orpatservice.app.data.model.requests_leads.LeadData
import com.orpatservice.app.databinding.ActivityTaskCompletedDetailsBinding
import com.orpatservice.app.databinding.ItemCompleteTaskBinding
import com.orpatservice.app.databinding.ItemPartsTaskCompletedBinding
import com.orpatservice.app.ui.admin.technician.TechniciansViewModel
import com.orpatservice.app.ui.leads.new_lead_fragment.adapter.TaskCompletedDetailsAdapter
import com.orpatservice.app.ui.leads.new_lead_fragment.adapter.WarrantyListAdapter
import com.orpatservice.app.ui.leads.new_lead_fragment.new_lead_request.*
import com.orpatservice.app.ui.leads.technician.adapter.TaskCompletedDataAdapter
import com.orpatservice.app.ui.leads.technician.section.TechnicianTaskAdapter
import com.orpatservice.app.ui.login.UserLoginViewModel
import com.orpatservice.app.ui.login.technician.OTPVerificationActivity
import com.orpatservice.app.utils.Constants
import com.orpatservice.app.utils.DividerItemDecorator
import com.orpatservice.app.utils.Utils
import com.tapadoo.alerter.Alerter


class TaskCompletedDetailsActivity  : AppCompatActivity(), TextWatcher {
    private lateinit var binding: ActivityTaskCompletedDetailsBinding
    private lateinit var taskCompletedRequestData: LeadData
    lateinit var layoutManager: LinearLayoutManager
    private var numTemp = ""
    private val editTextArray: ArrayList<EditText> = ArrayList(OTPVerificationActivity.NUM_OF_DIGITS)
    private val NUM_OF_DIGITS = 4
    private lateinit var viewModel : TechniciansViewModel
    private var alertDialog : Dialog? = null


    private lateinit var taskCompletedDetailsAdapter: TaskCompletedDetailsAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTaskCompletedDetailsBinding.inflate(layoutInflater)
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
        taskCompletedRequestData =
            intent?.getParcelableExtra<LeadData>(Constants.TASK_DATA) as LeadData


        taskCompletedDetailsAdapter = TaskCompletedDetailsAdapter(this@TaskCompletedDetailsActivity,taskCompletedRequestData.enquiries,
            itemClickListener = onItemClickListener)

        linearLayoutManager = LinearLayoutManager(this@TaskCompletedDetailsActivity)

        val dividerItemDecoration: RecyclerView.ItemDecoration =
            DividerItemDecorator(ContextCompat.getDrawable(this@TaskCompletedDetailsActivity, R.drawable.rv_divider))

        binding.includedContent.rvTaskCompleted.apply {
            adapter = taskCompletedDetailsAdapter
            //addItemDecoration(dividerItemDecoration)
            layoutManager = linearLayoutManager

        }
        utilDetails()
    }

    private fun utilDetails() {

        if(taskCompletedRequestData.pending_parts_verification_status_count.equals("0")){
            binding.includedContent.btnHappyCode.visibility = View.VISIBLE
            binding.includedContent.btnHideHappyCode.visibility = View.GONE

        }else{
            binding.includedContent.btnHappyCode.visibility = View.GONE
            binding.includedContent.btnHideHappyCode.visibility = View.VISIBLE
        }

        binding.includedContent.btnHappyCode.setOnClickListener {
            viewModel.hitAPITaskSendHappyCode(taskCompletedRequestData.id.toString()).observe(this, loadHappyCodeData())

            // showOTPVarificationPopUp()
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


    private val onItemClickListener: (Int, View) -> Unit = { position, view ->
        //partsBinding =  binding
       // _parentPosition = parentPosition
        when (view.id) {
            /*R.id.tv_otp_verification ->{
                showOTPVarificationPopUp()

            }*/
        }
    }

    private fun showOTPVarificationPopUp() {

        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View = inflater.inflate(R.layout.otp_varification_popup_layout, null)

        val alertDialogBuilder = Dialog(this@TaskCompletedDetailsActivity)
        alertDialogBuilder.setContentView(view)
        alertDialog = alertDialogBuilder
        createOTPUI(view)
        val btn_varify_otp = view.findViewById<TextView>(R.id.btn_varify_otp)
        val subTitle = view.findViewById<TextView>(R.id.tv_subheading)
        val tv_resend_happy_code = view.findViewById<TextView>(R.id.tv_resend_happy_code)

        subTitle.text = "Please verify the code sent to your mobile number +91"+" "+taskCompletedRequestData.mobile
        tv_resend_happy_code.setOnClickListener {
            viewModel.hitAPITaskSendHappyCode(taskCompletedRequestData.id.toString()).observe(this, loadHappyCodeData())

        }
        btn_varify_otp.setOnClickListener {
            validateOTP()
        }
        //alertDialogBuilder.setCancelable(false)
        alertDialogBuilder.show()

    }
    private fun validateOTP() {

        (0 until editTextArray.size)
            .forEach { i ->
                if (editTextArray[i].text.isEmpty()) {
                   /* Alerter.create(this)
                        .setText(getString(R.string.warning_enter_OTP))
                        .setBackgroundColorRes(R.color.orange)
                        .setDuration(1000)
                        .show()*/
                    Utils.instance.popupPinUtil(this,
                        getString(R.string.warning_enter_OTP),
                        "",
                        false)

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
        val layout: ConstraintLayout = views.findViewById(R.id.cl_otp_layout)
        for (index in 0 until (layout.childCount)) {
            val view: View = layout.getChildAt(index)
            if (view is EditText) {
                editTextArray.add(index, view)
                editTextArray[index].addTextChangedListener(this@TaskCompletedDetailsActivity)
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
                            editTextArray[i - 1]
                                .setSelection(editTextArray[i - 1].length())
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
            println("taskCompletedRequestData.id"+taskCompletedRequestData.id + verificationCode)
            viewModel.hitAPITaskSendHappyCodeVerification(taskCompletedRequestData.id.toString(),verificationCode).observe(this, loadHappyCodeVerificationData())

        }
    }

    private fun loadHappyCodeData(): Observer<Resource<TechnicianResponse>> {
        return Observer { it ->
            when (it?.status) {
                Status.LOADING -> {
                    //binding.cpiLoadingResend.visibility = View.VISIBLE

                }
                Status.ERROR -> {
                    //binding.cpiLoadingResend.visibility = View.GONE

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
                   // binding.cpiLoadingResend.visibility = View.GONE
                    val data = it?.data

                    data?.let {
                        if (it.success) {
                            showOTPVarificationPopUp()

                          /*  it.message.let { it1 ->
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

    private fun loadHappyCodeVerificationData(): Observer<Resource<UpdatePartsRequestData>> {
        return Observer { it ->
            when (it?.status) {
                Status.LOADING -> {
                    //binding.cpiLoadingResend.visibility = View.VISIBLE

                }
                Status.ERROR -> {
                    //binding.cpiLoadingResend.visibility = View.GONE

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
                    // binding.cpiLoadingResend.visibility = View.GONE
                    val data = it?.data

                    data?.let {
                        if (it.success) {
                            /*Alerter.create(this@TaskCompletedDetailsActivity)
                                .setTitle("")
                                .setText("" + it.message)
                                .setBackgroundColorRes(R.color.orange)
                                .setDuration(1000)
                                .show()
*/
                            it.message.let { it1 ->
                                Utils.instance.popupPinUtil(this,
                                    it1,
                                    "",
                                    true)
                            }
                            //alertDialog?.dismiss()
                            Handler(Looper.getMainLooper()).postDelayed({
                                val intent = Intent(this, RequestLeadActivity::class.java)
                                startActivity(intent)
                                finish()
                            }, 3000)

                        }
                    } ?: run {


                        /* Alerter.create(this@TaskCompletedDetailsActivity)
                             .setTitle("")
                             .setText("it.data?.message.toString()")
                             .setBackgroundColorRes(R.color.orange)
                             .setDuration(1000)
                             .show()*/

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


    private fun enableCodeEditTexts(enable: Boolean) {
        for (i in 0 until editTextArray.size)
            editTextArray[i].isEnabled = enable
    }

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
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
    }
}

