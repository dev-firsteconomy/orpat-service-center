package com.orpatservice.app.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.orpatservice.app.R
import com.orpatservice.app.utils.Constants
import com.tapadoo.alerter.Alerter
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // set toolbar as support action bar
        setSupportActionBar(toolbar)

        supportActionBar?.apply {
            title = ""
            // show back button on toolbar
            // on back button press, it will navigate to parent activity
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        btn_continue_mobile.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        if(v.id == R.id.btn_continue_mobile) {
            val mobileNumber = edt_mobile.text ?: ""
            if(mobileNumber.length < 10) {
                Alerter.create(this)
                    .setText(getString(R.string.warning_mobile_number))
                    .setBackgroundColorRes(R.color.orange)
                    .setDuration(1000)
                    .show()
            } else {
                signUp()
            }
        }
    }

    private fun signUp() {
        val intent = Intent(this, OTPVerificationActivity::class.java)
        intent.putExtra(Constants.MOBILE_NUMBER, edt_mobile.text.toString())
        startActivity(intent)
    }
}