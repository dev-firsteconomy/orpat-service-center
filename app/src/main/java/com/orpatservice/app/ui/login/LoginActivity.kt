package com.orpatservice.app.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.orpatservice.app.R
import com.orpatservice.app.utils.Constants
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
                Toast.makeText(this, getString(R.string.warning_mobile_number), Toast.LENGTH_SHORT).show()
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