package com.orpatservice.app.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import com.orpatservice.app.R
import com.orpatservice.app.databinding.ActivitySelectUserBinding
import com.orpatservice.app.data.sharedprefs.SharedPrefs
import com.orpatservice.app.utils.Constants
import com.tapadoo.alerter.Alerter

class SelectUserActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding : ActivitySelectUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectUserBinding.inflate(layoutInflater)
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

        binding.btnContinueUser.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btn_continue_user -> {
                selectUser()
            }
        }
    }

    private fun selectUser() {
        if (binding.rbAdmin.isChecked) {
            goToLogin(Constants.SERVICE_CENTER)
        } else if (binding.rbTechnician.isChecked) {
            goToLogin(Constants.TECHNICIAN)
        } else {
            Alerter.create(this)
                .setTitle("")
                .setText(getString(R.string.warning_select_admin_technician))
                .setBackgroundColorRes(R.color.orange)
                .setDuration(1000)
                .show()
        }
    }

    private fun goToLogin(userType: String) {
        SharedPrefs.getInstance().addString(Constants.USER_TYPE, userType)
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
       // finish()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                super.onBackPressed()
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {

        super.onBackPressed()
        finish()
    }
}