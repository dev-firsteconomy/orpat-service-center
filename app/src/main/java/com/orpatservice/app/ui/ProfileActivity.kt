package com.orpatservice.app.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson
import com.orpatservice.app.R
import com.orpatservice.app.databinding.ActivityProfileBinding
import com.orpatservice.app.ui.data.model.login.LoginBaseData
import com.orpatservice.app.ui.data.sharedprefs.SharedPrefs
import com.orpatservice.app.ui.login.SelectUserActivity
import com.orpatservice.app.utils.Constants

class ProfileActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var binding : ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
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

        val gson = Gson()
        val jsonUserDetails = SharedPrefs.getInstance().getString(Constants.SERVICE_CENTER, "")
        val userDetails = gson.fromJson(jsonUserDetails, LoginBaseData::class.java) as LoginBaseData
        setUserDetails(userDetails)

        binding.mcvLogout.setOnClickListener(this)
    }

    private fun setUserDetails(userDetails: LoginBaseData) {
        if (SharedPrefs.getInstance().getString(Constants.USER_TYPE, "").equals(Constants.SERVICE_CENTER)) {
            binding.tvName.text = userDetails.serviceCenter?.name
            binding.tvMobile.text = userDetails.serviceCenter?.mobile
        } else if (SharedPrefs.getInstance().getString(Constants.USER_TYPE, "").equals(Constants.TECHNICIAN)) {
            (userDetails.technician?.firstName + " " + userDetails.technician?.lastName).also { binding.tvName.text = it }
            binding.tvMobile.text = userDetails.technician?.mobile

            Glide.with(binding.ivProfileImage)
                .load(userDetails.technician?.image)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .circleCrop() // .error(R.drawable.active_dot)
                .placeholder(R.drawable.avtar)
                .into(binding.ivProfileImage)
        }
    }

    override fun onClick(v: View) {
        when(v.id) {
            R.id.mcv_setting -> {

            }
            R.id.mcv_payment -> {

            }
            R.id.mcv_notification -> {

            }
            R.id.mcv_supports -> {

            }
            R.id.mcv_logout -> {
                confirmationDialog()
            }
        }
    }

    private fun confirmationDialog() {

        MaterialAlertDialogBuilder(
            this,
            R.style.MyThemeOverlay_MaterialComponents_MaterialAlertDialog
        )
            .setTitle("Logout")
            .setMessage("Are you sure, you want to logout?")
            .setPositiveButton(
                "Logout"
            ) { _, i ->
                SharedPrefs.getInstance().removeAll()
                startActivity(Intent(this, SelectUserActivity::class.java))
                finish()
            }
            .setNegativeButton(
                "CANCEL"
            ) { _, i -> }
            .show()
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