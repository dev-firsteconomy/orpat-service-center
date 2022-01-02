package com.orpatservice.app.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.widget.EditText
import androidx.constraintlayout.widget.ConstraintLayout
import com.orpatservice.app.R
import com.orpatservice.app.utils.Constants
import android.os.CountDownTimer
import android.view.MenuItem
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.orpatservice.app.databinding.ActivityOtpverificationBinding
import com.orpatservice.app.ui.admin.dashboard.DashboardActivity
import com.orpatservice.app.ui.data.Resource
import com.orpatservice.app.ui.data.Status
import com.orpatservice.app.ui.data.model.login.LoginResponse
import com.orpatservice.app.ui.data.model.login.OTPSendResponse
import com.orpatservice.app.ui.data.sharedprefs.SharedPrefs
import com.tapadoo.alerter.Alerter


class OTPVerificationActivity : AppCompatActivity(), TextWatcher, View.OnClickListener {

    private val editTextArray: ArrayList<EditText> = ArrayList(NUM_OF_DIGITS)
    private var numTemp = ""
    private var mobileNumber = ""

    companion object {
        const val NUM_OF_DIGITS = 4
    }

    lateinit var binding : ActivityOtpverificationBinding
    private lateinit var viewModel : UserLoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpverificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // set toolbar as support action bar
        setSupportActionBar(binding.toolbar)

        supportActionBar?.apply {
            title = ""
            // show back button on toolbar
            // on back button press, it will navigate to parent activity
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        viewModel = ViewModelProvider(this)[UserLoginViewModel::class.java]
        binding.btnContinueOtp.setOnClickListener(this)

        // OTP UI logic
        setObserver()
        createOTPUI()
        getIntentData()
        resendOTPTimer()
    }

    private fun setObserver() {
        viewModel.OTPData.observe(this, this::onGetOTP)
        viewModel.loginData.observe(this, this::onLogin)
    }

    /**
     * OTP response handling
     */
    private fun onGetOTP(resources: Resource<OTPSendResponse>) {
        when (resources.status) {
            Status.LOADING -> {
                binding.cpiLoadingResend.visibility = View.VISIBLE
            }
            Status.ERROR -> {
                binding.cpiLoadingResend.visibility = View.GONE

                Alerter.create(this)
                    .setText(resources.error.toString())
                    .setBackgroundColorRes(R.color.orange)
                    .setDuration(1000)
                    .show()
            }
            else -> {
                binding.cpiLoadingResend.visibility = View.GONE

                val data = resources.data

                data.let {
                    if(it?.success == true){

                        //When timer is running then make sure click listener is disable
                        binding.tvResendOtpTimer.setOnClickListener(null)
                        //Once opt resend to user, it will take 30 sec to enable resend OTP button
                        resendOTPTimer()
                        binding.tvResendOtpTimer.setTextColor(
                            ContextCompat.getColor(
                                this@OTPVerificationActivity,
                                R.color.brown
                            )
                        )

                        Alerter.create(this)
                            .setTitle(getString(R.string.otp_resend))
                            .setText(it.message)
                            .setBackgroundColorRes(R.color.orange)
                            .setDuration(2000)
                            .show()
                    }
                }.run {  }
            }
        }
    }

    //Login response handling
    private fun onLogin(resources: Resource<LoginResponse>) {
        when (resources.status) {
            Status.LOADING -> {
                binding.cpiLoading.visibility = View.VISIBLE
            }
            Status.ERROR -> {
                binding.btnContinueOtp.visibility = View.VISIBLE
                binding.cpiLoading.visibility = View.GONE
                enableCodeEditTexts(true)
                Alerter.create(this)
                    .setText(resources.error?.message.toString())
                    .setBackgroundColorRes(R.color.orange)
                    .setDuration(1000)
                    .show()
            }
            else -> {
                binding.cpiLoading.visibility = View.GONE

                val data = resources.data

                data.let {
                    if(it?.success == true){
                       //go to dashboard
                        dashboardLanding(it)
                    }
                }.run {  }
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

    private fun getIntentData() {
        val intent: Intent = intent
        mobileNumber = intent.getStringExtra(Constants.MOBILE_NUMBER).toString()
        setMobileNumber(mobileNumber)
    }

    private fun createOTPUI() {
        //create array
        val layout: ConstraintLayout = binding.clOtpLayout
        for (index in 0 until (layout.childCount)) {
            val view: View = layout.getChildAt(index)
            if (view is EditText) {
                editTextArray.add(index, view)
                editTextArray[index].addTextChangedListener(this)
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

    private fun setMobileNumber(mobileNumber: String) {
        val customMessage = binding.tvSubheading.text.toString() + " " + mobileNumber.substring(
            0,
            1
        ) + "XXXX X" + mobileNumber.substring(6, 10)
        binding.tvSubheading.text = customMessage
    }

    /**
     * resend OTP timer
     */
    private fun resendOTPTimer() {
        object : CountDownTimer(30000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                binding.tvResendOtpTimer.text = String.format("%02d:%02d", 0, millisUntilFinished / 1000)
            }

            override fun onFinish() {
                binding.tvResendOtpTimer.setOnClickListener(this@OTPVerificationActivity)
                binding.tvResendOtpTimer.text = getString(R.string.resend_otp)
                binding.tvResendOtpTimer.setTextColor(
                    ContextCompat.getColor(
                        this@OTPVerificationActivity,
                        R.color.orange
                    )
                )
            }
        }.start()
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btn_continue_otp -> {
                validateOTP()
            }
            R.id.tv_resend_otp_timer -> {
                requestOTP()
            }
        }
    }

    private fun validateOTP() {

        (0 until editTextArray.size)
            .forEach { i ->
                if (editTextArray[i].text.isEmpty()) {
                    Alerter.create(this)
                        .setText(getString(R.string.warning_enter_OTP))
                        .setBackgroundColorRes(R.color.orange)
                        .setDuration(1000)
                        .show()
                    return
                }
            }
        verifyOTPCode(testCodeValidity())
    }

    //After successful login user get land on dashboard activity
    private fun dashboardLanding(loginResponse: LoginResponse) {

        //Now save token and basic details in shared pref
        SharedPrefs.getInstance().addString(Constants.TOKEN, loginResponse.data.token)
        //Storing service center data in form of JSON
        val gson = Gson()
        SharedPrefs.getInstance().addString(Constants.SERVICE_CENTER_DATA, gson.toJson(loginResponse.data))

        val intent = Intent(this, DashboardActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }


    /**
     * API to get OTP
     */
    private fun signUp() {
        viewModel.hitOTPApi(mobileNumber)
    }

    private fun requestOTP() {
        binding.cpiLoadingResend.visibility = View.VISIBLE
        binding.tvResendOtpTimer.text = ""

        signUp()
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

    /**
     * Set the edittext views to be editable / uneditable
     */
    private fun enableCodeEditTexts(enable: Boolean) {
        for (i in 0 until editTextArray.size)
            editTextArray[i].isEnabled = enable
    }


    /**
     * Initialize all views back to blanks and focus on first view
     */
    private fun initCodeEditTexts() {
        for (i in 0 until editTextArray.size)
            editTextArray[i].setText("")
        editTextArray[0].requestFocus()
    }

    /**
     * Use this function to set the views text from a string i.e using an sms listener to read the code off an sms
     */
    private fun setVerificationCode(verificationCode: String) {
        for (i in 0 until editTextArray.size)
            editTextArray[i].setText(verificationCode.substring(i, i))
    }

    /**
     * Returns the code if it has the correct number of digits, else ""
     */
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

    /**
     * Verify the code - you take it from here
     */
    private fun verifyOTPCode(verificationCode: String) {
        if (verificationCode.isNotEmpty()) {
            enableCodeEditTexts(false)
            //API trigger
            binding.btnContinueOtp.visibility = View.INVISIBLE
            binding.cpiLoading.visibility = View.VISIBLE
            viewModel.hitVerifyOTPLoginApi(mobileNumber, verificationCode)
        }
    }
}