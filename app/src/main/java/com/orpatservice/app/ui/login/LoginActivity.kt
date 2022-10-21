package com.orpatservice.app.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.orpatservice.app.R
import com.orpatservice.app.databinding.ActivityLoginBinding
import com.orpatservice.app.ui.admin.dashboard.DashboardActivity
import com.orpatservice.app.data.Resource
import com.orpatservice.app.data.Status
import com.orpatservice.app.data.model.login.LoginResponse
import com.orpatservice.app.data.model.login.OTPSendResponse
import com.orpatservice.app.data.remote.ApiClient
import com.orpatservice.app.data.sharedprefs.SharedPrefs
import com.orpatservice.app.ui.login.service_center.ServiceCenterLoginFragment
import com.orpatservice.app.ui.login.technician.OTPVerificationActivity
import com.orpatservice.app.ui.login.technician.TechnicianLoginFragment
import com.orpatservice.app.utils.Constants
import com.tapadoo.alerter.Alerter


class LoginActivity : AppCompatActivity(){

    private lateinit var binding : ActivityLoginBinding
    private lateinit var viewModel : UserLoginViewModel
    private var fragmentLoginUI = Fragment()

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

        viewModel = ViewModelProvider(this)[UserLoginViewModel::class.java]

        if(SharedPrefs.getInstance().getString(Constants.USER_TYPE, "").equals(Constants.SERVICE_CENTER)) {
            fragmentLoginUI = ServiceCenterLoginFragment()
            loadFragment(fragmentLoginUI)
        } else if(SharedPrefs.getInstance().getString(Constants.USER_TYPE, "").equals(Constants.TECHNICIAN)) {
            fragmentLoginUI = TechnicianLoginFragment()
            loadFragment(fragmentLoginUI)
        }

        setObserver()
    }

    private fun setObserver() {
        viewModel.OTPData.observe(this, this::onGetOTP)
        viewModel.loginData.observe(this, this::onLogin)
    }

    private fun loadFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fl_login_container, fragment)
        transaction.disallowAddToBackStack()
        transaction.commit()
    }

    private fun onGetOTP(resources: Resource<OTPSendResponse>) {
        when (resources.status) {
                Status.LOADING -> {
                    if(fragmentLoginUI is TechnicianLoginFragment) {
                        (fragmentLoginUI as TechnicianLoginFragment).showLoadingUI()
                    }
                }
                Status.ERROR -> {
                    if(fragmentLoginUI is TechnicianLoginFragment) {
                        (fragmentLoginUI as TechnicianLoginFragment).hideLoadingUI()
                    }
                    println("getTechnicianOtpAPI"+ resources.error?.message.toString())

                    Alerter.create(this)
                        .setText(resources.error?.message.toString())
                        .setBackgroundColorRes(R.color.orange)
                        .setDuration(1500)
                        .show()
                }
                else -> {
                    if(fragmentLoginUI is TechnicianLoginFragment) {
                        (fragmentLoginUI as TechnicianLoginFragment).hideLoadingUI()
                    }

                    val data = resources.data

                    data.let {
                        if(it?.success == true){
                            val intent = Intent(this, OTPVerificationActivity::class.java)
                            intent.putExtra(Constants.MOBILE_NUMBER, it.data.mobile)

                            startActivity(intent)
                        }
                    }.run {  }
                }
            }
    }

    //Login response handling for service center
    private fun onLogin(resources: Resource<LoginResponse>) {
        when (resources.status) {
            Status.LOADING -> {
                if(fragmentLoginUI is ServiceCenterLoginFragment) {
                    (fragmentLoginUI as ServiceCenterLoginFragment).showLoadingUI()
                }
            }
            Status.ERROR -> {
                if(fragmentLoginUI is ServiceCenterLoginFragment) {
                    (fragmentLoginUI as ServiceCenterLoginFragment).hideLoadingUI()
                }
                Alerter.create(this)
                    .setText(resources.error?.message.toString())
                    .setBackgroundColorRes(R.color.orange)
                    .setDuration(1000)
                    .show()
            }
            else -> {
                if(fragmentLoginUI is ServiceCenterLoginFragment) {
                    (fragmentLoginUI as ServiceCenterLoginFragment).hideLoadingUI()
                }

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

    override fun onResume() {
        super.onResume()
        //After getting user to next screen send OTP button should be visible
        if(fragmentLoginUI is TechnicianLoginFragment) {
            (fragmentLoginUI as TechnicianLoginFragment).hideLoadingUI()
        }
    }

    fun signUpTechnician(mobileNumber: String) {
        viewModel.hitOTPApi(mobileNumber)
    }

    fun signUpServiceCenter(email: String, password: String) {
        viewModel.hitServiceCenterLoginApi(email, password)
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