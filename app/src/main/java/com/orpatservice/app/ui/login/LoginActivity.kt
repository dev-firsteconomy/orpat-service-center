package com.orpatservice.app.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.orpatservice.app.R
import com.orpatservice.app.databinding.ActivityLoginBinding
import com.orpatservice.app.ui.data.Resource
import com.orpatservice.app.ui.data.Status
import com.orpatservice.app.ui.data.model.TechnicianResponse
import com.orpatservice.app.ui.data.model.otp.OTPSendResponse
import com.orpatservice.app.utils.Constants
import com.tapadoo.alerter.Alerter


class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding : ActivityLoginBinding
    private lateinit var viewModel : UserLoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
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

        //Listeners
        binding.btnContinueMobile.setOnClickListener(this)

        viewModel = ViewModelProvider(this)[UserLoginViewModel::class.java]

        setObserver()
    }

    private fun setObserver() {
        viewModel.OTPData.observe(this, this::onLogin)
    }

    private fun onLogin(resources: Resource<OTPSendResponse>) {
        when (resources.status) {
                Status.LOADING -> {
                    binding.cpiLoading.visibility = View.VISIBLE
                }
                Status.ERROR -> {
                    binding.cpiLoading.visibility = View.GONE
                }
                else -> {
                    binding.cpiLoading.visibility = View.GONE

                    val data = resources.data

                    data.let {
                        if(it?.success == true){
                            val intent = Intent(this, OTPVerificationActivity::class.java)
                            intent.putExtra(Constants.MOBILE_NUMBER, binding.edtMobile.text.toString())
                            startActivity(intent)
                        }
                    }.run {  }
                }
            }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btn_continue_mobile -> {
                val mobileNumber = binding.edtMobile.text ?: ""
                if (mobileNumber.length < 10) {
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
    }

    private fun signUp() {
        val mobileNumber = binding.edtMobile.text.toString()
        viewModel.hitOTPApi(mobileNumber)
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