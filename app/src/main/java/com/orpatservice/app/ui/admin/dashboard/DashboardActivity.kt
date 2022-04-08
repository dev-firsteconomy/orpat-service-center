package com.orpatservice.app.ui.admin.dashboard

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.orpatservice.app.R
import com.orpatservice.app.databinding.ActivityDashboardBinding
import com.orpatservice.app.ui.ProfileActivity
import com.orpatservice.app.ui.admin.technician.TechniciansActivity
import com.orpatservice.app.ui.leads.service_center.LeadHistoryActivity
import com.orpatservice.app.ui.leads.service_center.RequestLeadActivity
import com.orpatservice.app.utils.Constants
import com.orpatservice.app.data.sharedprefs.SharedPrefs
import com.orpatservice.app.ui.leads.technician.TechnicianHistoryLeadActivity
import com.orpatservice.app.ui.login.SelectUserActivity
import com.orpatservice.app.ui.leads.technician.TechnicianRequestLeadActivity


class DashboardActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        binding.includedContent.mcvRequest.setOnClickListener(this)
        binding.includedContent.mcvAddTechnician.setOnClickListener(this)
        binding.includedContent.mcvHistory.setOnClickListener(this)
        binding.includedContent.mcvProfile.setOnClickListener(this)
        binding.includedContent.mcvPayment.setOnClickListener(this)
        binding.includedContent.mcvMore.setOnClickListener(this)
        binding.ivLogout.setOnClickListener(this)

        updateUI()
    }

    private fun updateUI() {
        if (SharedPrefs.getInstance().getString(Constants.USER_TYPE, "").equals(Constants.TECHNICIAN, ignoreCase = true)) {
            binding.includedContent.mcvAddTechnician.visibility = View.VISIBLE
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
                startActivity(Intent(this,SelectUserActivity::class.java))
                finish()
            }
            .setNegativeButton(
                "CANCEL"
            ) { _, i -> }
            .show()

    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.mcv_request -> {
                if (SharedPrefs.getInstance().getString(Constants.USER_TYPE, "").equals(Constants.SERVICE_CENTER)) {
                    val intent = Intent(this, RequestLeadActivity::class.java)
                    intent.putExtra(Constants.MODULE_TYPE, Constants.REQUEST)
                    startActivity(intent)
                } else if (SharedPrefs.getInstance().getString(Constants.USER_TYPE, "").equals(Constants.TECHNICIAN)) {
                    val intent = Intent(this, TechnicianRequestLeadActivity::class.java)
                    intent.putExtra(Constants.MODULE_TYPE, Constants.REQUEST)
                    startActivity(intent)
                }
            }
            R.id.mcv_add_technician -> {
                val intent = Intent(this, TechniciansActivity::class.java)

                intent.putExtra(Constants.IS_NAV, Constants.ComingFrom.DASHBOARD)
                startActivity(intent)

            }
            R.id.mcv_history -> {

                if (SharedPrefs.getInstance().getString(Constants.USER_TYPE, "").equals(Constants.SERVICE_CENTER)) {
                    val intent = Intent(this, LeadHistoryActivity::class.java)
                    intent.putExtra(Constants.MODULE_TYPE, Constants.HISTORY)
                    startActivity(intent)
                } else if (SharedPrefs.getInstance().getString(Constants.USER_TYPE, "").equals(Constants.TECHNICIAN)) {
                    val intent = Intent(this, TechnicianHistoryLeadActivity::class.java)
                    intent.putExtra(Constants.MODULE_TYPE, Constants.HISTORY)
                    startActivity(intent)
                }
            }
            R.id.mcv_profile -> {
                startActivity(Intent(this, ProfileActivity::class.java))
            }
            R.id.mcv_payment -> {
            }
            R.id.mcv_more -> {

            }
            R.id.iv_logout -> {
                confirmationDialog()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        val item: MenuItem = menu.findItem(R.id.action_settings)
        item.isVisible = false
        super.onPrepareOptionsMenu(menu)
        return true
    }
}