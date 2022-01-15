package com.orpatservice.app.ui.technician

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.orpatservice.app.R
import com.orpatservice.app.data.Resource
import com.orpatservice.app.data.Status
import com.orpatservice.app.data.model.RepairPartResponse
import com.orpatservice.app.data.model.TechnicianResponse
import com.orpatservice.app.databinding.ActivityHappyCodeBinding
import com.orpatservice.app.ui.admin.technician.TechniciansViewModel
import com.orpatservice.app.ui.login.technician.OTPVerificationActivity
import com.orpatservice.app.utils.Constants
import com.tapadoo.alerter.Alerter

class HappyCodeActivity : AppCompatActivity(), View.OnClickListener, TextWatcher {

    private lateinit var viewModel: TechniciansViewModel

    private val editTextArray: ArrayList<EditText> = ArrayList(NUM_OF_DIGITS)
    private var numTemp = ""

    companion object {
        const val NUM_OF_DIGITS = 4
    }

    private lateinit var binding: ActivityHappyCodeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHappyCodeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[TechniciansViewModel::class.java]

        createOTPUI()
        resendOTPTimer()
        binding.btnContinueOtp.setOnClickListener(this)

        setObserver()
    }

    private fun setObserver() {
    }

    private fun hitAPISendHappyCode(){
        viewModel.hitAPISendHappyCode(intent.getIntExtra(Constants.LEADS_ID,0).toString()).observe(this,loadHappyCodeData())
    }

    private fun loadHappyCodeData(): Observer<Resource<TechnicianResponse>> {
        return Observer { it ->
            when (it?.status) {
                Status.LOADING -> {
                    binding.cpiLoadingResend.visibility = View.VISIBLE

                }
                Status.ERROR -> {
                    binding.cpiLoadingResend.visibility = View.GONE

                    Alerter.create(this@HappyCodeActivity)
                        .setTitle("")
                        .setText("" + it.error?.message.toString())
                        .setBackgroundColorRes(R.color.orange)
                        .setDuration(1000)
                        .show()

                }
                else -> {
                    binding.cpiLoadingResend.visibility = View.GONE
                    val data = it?.data

                    data?.let {
                        if (it.success) {
                            Alerter.create(this@HappyCodeActivity)
                                .setTitle("")
                                .setText(""+it.message)
                                .setBackgroundColorRes(R.color.orange)
                                .setDuration(1000)
                                .show()

                        }
                    } ?: run {


                        Alerter.create(this@HappyCodeActivity)
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

    override fun onClick(v: View) {
        when(v.id) {
            R.id.btn_continue_otp -> {
                validateOTP()
            }
            R.id.tv_resend_otp_timer -> {
                requestOTP()
            }
        }
    }

    private fun requestOTP() {
        binding.cpiLoadingResend.visibility = View.VISIBLE
        binding.tvResendOtpTimer.text = ""

        resendOTPTimer()
        // Write code to get Happy Code

        hitAPISendHappyCode()
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


    /**
     * resend OTP timer
     */
    private fun resendOTPTimer() {
        binding.tvResendOtpTimer.setTextColor(
            ContextCompat.getColor(
                this@HappyCodeActivity,
                R.color.brown
            )
        )
        binding.tvResendOtpTimer.setOnClickListener(null)

        object : CountDownTimer(30000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                binding.tvResendOtpTimer.text = String.format("%02d:%02d", 0, millisUntilFinished / 1000)
            }

            override fun onFinish() {
                binding.tvResendOtpTimer.setOnClickListener(this@HappyCodeActivity)
                binding.tvResendOtpTimer.text = getString(R.string.resend_otp)
                binding.tvResendOtpTimer.setTextColor(
                    ContextCompat.getColor(
                        this@HappyCodeActivity,
                        R.color.orange
                    )
                )
            }
        }.start()
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

    /**
     * Verify the code - you take it from here
     */
    private fun verifyOTPCode(verificationCode: String) {
        if (verificationCode.isNotEmpty()) {
            enableCodeEditTexts(false)
            //API trigger
            binding.btnContinueOtp.visibility = View.INVISIBLE
            binding.cpiLoading.visibility = View.VISIBLE

            //Write API call here
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
     * Returns the code if it has the correct number of digits, else ""
     */
    private fun testCodeValidity(): String {
        var verificationCode = ""
        for (i in editTextArray.indices) {
            val digit = editTextArray[i].text.toString().trim { it <= ' ' }
            verificationCode += digit
        }
        if (verificationCode.trim { it <= ' ' }.length == OTPVerificationActivity.NUM_OF_DIGITS) {
            return verificationCode
        }
        return ""
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