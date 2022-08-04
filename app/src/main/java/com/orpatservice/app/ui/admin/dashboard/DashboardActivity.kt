package com.orpatservice.app.ui.admin.dashboard

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.orpatservice.app.R
import com.orpatservice.app.data.Resource
import com.orpatservice.app.data.Status
import com.orpatservice.app.data.model.requests_leads.RequestLeadResponse
import com.orpatservice.app.databinding.ActivityDashboardBinding
import com.orpatservice.app.ui.ProfileActivity
import com.orpatservice.app.ui.admin.technician.TechniciansActivity
import com.orpatservice.app.ui.leads.service_center.LeadHistoryActivity
import com.orpatservice.app.ui.leads.service_center.RequestLeadActivity
import com.orpatservice.app.utils.Constants
import com.orpatservice.app.data.sharedprefs.SharedPrefs
import com.orpatservice.app.ui.leads.chargeable_request.ChargeableRequestActivity
import com.orpatservice.app.ui.leads.technician.TechnicianHistoryLeadActivity
import com.orpatservice.app.ui.login.SelectUserActivity
import com.orpatservice.app.ui.leads.technician.TechnicianRequestLeadActivity
import com.orpatservice.app.ui.leads.viewmodel.RequestsLeadsViewModel
import com.orpatservice.app.utils.Utils


class DashboardActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityDashboardBinding
    private lateinit var requestLeadsViewModel: RequestsLeadsViewModel
    private lateinit var notificationCount: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        requestLeadsViewModel = ViewModelProvider(this)[RequestsLeadsViewModel::class.java]

        binding.includedContent.mcvRequest.setOnClickListener(this)
        binding.includedContent.mcvAddTechnician.setOnClickListener(this)
        binding.includedContent.mcvHistory.setOnClickListener(this)
        binding.includedContent.mcvProfile.setOnClickListener(this)
        binding.includedContent.mcvPayment.setOnClickListener(this)
        binding.includedContent.mcvMore.setOnClickListener(this)
        binding.includedContent.mcvChargeableRequest.setOnClickListener(this)
        binding.ivLogout.setOnClickListener(this)

        updateUI()
        setObserver()

    }

    private fun setObserver() {

        /*requestLeadsViewModel.synAppConfig.observe(this, this::getSynAppConfig)
        requestLeadsViewModel.loadSynAppConfig()
        requestLeadsViewModel.technicianSynAppConfig.observe(this, this::getTechnicianSynAppConfig)
        requestLeadsViewModel.loadTechnicianSynAppConfig()*/

    }

    private fun getSynAppConfig(resources: Resource<RequestSynAppResponse>) {
        when (resources.status) {
            Status.LOADING -> {

            }
            Status.ERROR -> {

                    Utils.instance.popupPinUtil(this,
                        resources.error?.message.toString(),
                        "",
                        false)
            }
            else -> {

                val response = resources.data

                response?.let {
                    if (it.success) {
                        notificationCount = response.data.notification_badge_count.barcode_request_tab

                        if (SharedPrefs.getInstance().getString(Constants.USER_TYPE, "").equals(Constants.SERVICE_CENTER)) {
                            binding.includedContent.tvCount.visibility = VISIBLE
                            binding.includedContent.tvCount.text = response.data.notification_badge_count.barcode_request_tab
                            binding.includedContent.tvChargeableCount.text = response.data.notification_badge_count.no_barcode_request_tab

                        } else if (SharedPrefs.getInstance().getString(Constants.USER_TYPE, "").equals(Constants.TECHNICIAN)) {
                            binding.includedContent.tvCount.visibility = VISIBLE
                            binding.includedContent.tvCount.text = response.data.notification_badge_count.barcode_request_tab

                        }
                    }
                }
            }
        }
    }

    private fun getTechnicianSynAppConfig(resources: Resource<RequestTechnicianSynAppResponse>) {
        when (resources.status) {
            Status.LOADING -> {

            }
            Status.ERROR -> {

                Utils.instance.popupPinUtil(this,
                    resources.error?.message.toString(),
                    "",
                    false)
            }
            else -> {

                val response = resources.data

                response?.let {
                    if (it.success) {
                        notificationCount = response.data.notification_badge_count.barcode_request_tab

                        if (SharedPrefs.getInstance().getString(Constants.USER_TYPE, "").equals(Constants.SERVICE_CENTER)) {
                            binding.includedContent.tvCount.visibility = VISIBLE
                            binding.includedContent.tvCount.text = response.data.notification_badge_count.barcode_request_tab
                         //   binding.includedContent.tvChargeableCount.text = response.data.notification_badge_count.no_barcode_request_tab

                        } else if (SharedPrefs.getInstance().getString(Constants.USER_TYPE, "").equals(Constants.TECHNICIAN)) {
                            binding.includedContent.tvCount.visibility = VISIBLE
                            binding.includedContent.tvCount.text = response.data.notification_badge_count.barcode_request_tab

                        }
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (SharedPrefs.getInstance().getString(Constants.USER_TYPE, "").equals(Constants.SERVICE_CENTER)) {
            binding.tvTitle.setText("Admin")

            requestLeadsViewModel.synAppConfig.observe(this, this::getSynAppConfig)
            requestLeadsViewModel.loadSynAppConfig()

        } else if (SharedPrefs.getInstance().getString(Constants.USER_TYPE, "").equals(Constants.TECHNICIAN)) {
            binding.tvTitle.setText("Technician")

            requestLeadsViewModel.technicianSynAppConfig.observe(this, this::getTechnicianSynAppConfig)
            requestLeadsViewModel.loadTechnicianSynAppConfig()
        }
        if (SharedPrefs.getInstance().getString(Constants.USER_TYPE, "").equals(Constants.TECHNICIAN, ignoreCase = true)) {
            binding.includedContent.mcvAddTechnician.visibility = View.GONE
            binding.includedContent.mcvPayment.visibility = View.GONE
        }

    }
    private fun updateUI() {
        if (SharedPrefs.getInstance().getString(Constants.USER_TYPE, "").equals(Constants.SERVICE_CENTER)) {
            binding.tvTitle.setText("Admin")

            requestLeadsViewModel.synAppConfig.observe(this, this::getSynAppConfig)
            requestLeadsViewModel.loadSynAppConfig()

        } else if (SharedPrefs.getInstance().getString(Constants.USER_TYPE, "").equals(Constants.TECHNICIAN)) {
            binding.tvTitle.setText("Technician")

            requestLeadsViewModel.technicianSynAppConfig.observe(this, this::getTechnicianSynAppConfig)
            requestLeadsViewModel.loadTechnicianSynAppConfig()
        }
        if (SharedPrefs.getInstance().getString(Constants.USER_TYPE, "").equals(Constants.TECHNICIAN, ignoreCase = true)) {
            binding.includedContent.mcvAddTechnician.visibility = View.GONE
            binding.includedContent.mcvPayment.visibility = View.GONE
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
                val intent = Intent(this, ChargeableRequestActivity::class.java)
                intent.putExtra(Constants.MODULE_TYPE, Constants.REQUEST)
                startActivity(intent)

            }
            R.id.mcv_chargeable_request -> {

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