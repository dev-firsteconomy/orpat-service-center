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
import kotlinx.android.synthetic.main.activity_otpverification.*
import android.os.CountDownTimer
import android.view.MenuItem
import androidx.core.content.ContextCompat
import com.tapadoo.alerter.Alerter


class OTPVerificationActivity : AppCompatActivity(), TextWatcher, View.OnClickListener {

    private val editTextArray: ArrayList<EditText> = ArrayList(NUM_OF_DIGITS)
    private var numTemp = ""
    var index = 0

    companion object {
        const val NUM_OF_DIGITS = 4
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otpverification)

        // set toolbar as support action bar
        setSupportActionBar(toolbar)

        supportActionBar?.apply {
            title = ""
            // show back button on toolbar
            // on back button press, it will navigate to parent activity
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        btn_continue_otp.setOnClickListener(this)

        // OTP UI logic
        createOTPUI()
        getIntentData()
        resendOTPTimer()
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
        val mobileNumber = intent.getStringExtra(Constants.MOBILE_NUMBER)
        if (mobileNumber != null) {
            setMobileNumber(mobileNumber)
        }
    }

    private fun createOTPUI() {
        //create array
        val layout: ConstraintLayout = cl_otp_layout
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
        val customMessage = tv_subheading.text.toString() + " " + mobileNumber.substring(
            0,
            1
        ) + "XXXX X" + mobileNumber.substring(6, 10)
        tv_subheading.text = customMessage
    }

    //resend OTP timer
    private fun resendOTPTimer() {
        object : CountDownTimer(30000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                tv_resend_otp_timer.text = String.format("%02d:%02d", 0, millisUntilFinished / 1000)
            }

            override fun onFinish() {
                tv_resend_otp_timer.text = getString(R.string.resend_otp)
                tv_resend_otp_timer.setTextColor(
                    ContextCompat.getColor(
                        this@OTPVerificationActivity,
                        R.color.orange
                    )
                )
                tv_resend_otp_timer.setOnClickListener(this@OTPVerificationActivity)
            }
        }.start()
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btn_continue_otp -> {
                verifyOTP()
            }
            R.id.tv_resend_otp_timer -> {
                requestOTP()
            }
        }
    }

    private fun verifyOTP() {
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
    }

    private fun requestOTP() {
        resendOTPTimer()
        tv_resend_otp_timer.setTextColor(
            ContextCompat.getColor(
                this@OTPVerificationActivity,
                R.color.brown
            )
        )

        Alerter.create(this)
            .setTitle(getString(R.string.otp_resend))
            .setText(getString(R.string.message_resend_otp))
            .setBackgroundColorRes(R.color.orange)
            .setDuration(2000)
            .show()
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
                        //will verify code the moment the last character is inserted and all digits have a number
                        //verifyCode(testCodeValidity())
                    }
                }
            }
    }

    /** Set the edittext views to be editable / uneditable
     *
     */
    private fun enableCodeEditTexts(enable: Boolean) {
        for (i in 0 until editTextArray.size)
            editTextArray[i].isEnabled = enable
    }


    /** Initialize all views back to blanks and focus on first view
     *
     */
    private fun initCodeEditTexts() {
        for (i in 0 until editTextArray.size)
            editTextArray[i].setText("")
        editTextArray[0].requestFocus()
    }

    /** Use this function to set the views text from a string i.e using an sms listener to read the code off an sms
     *
     */
    private fun setVerificationCode(verificationCode: String) {
        for (i in 0 until editTextArray.size)
            editTextArray[i].setText(verificationCode.substring(i, i))
    }

    /** Returns the code if it has the correct number of digits, else ""
     *
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

    /**Verify the code - you take it from here
     *
     */
    private fun verifyCode(verificationCode: String) {
        if (verificationCode.isNotEmpty()) {
            enableCodeEditTexts(false)
            //Your code
        }
    }
}