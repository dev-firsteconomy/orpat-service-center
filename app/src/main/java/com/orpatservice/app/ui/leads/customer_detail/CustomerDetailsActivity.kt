package com.orpatservice.app.ui.leads.customer_detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.orpatservice.app.R
import com.orpatservice.app.data.model.requests_leads.LeadData
import com.orpatservice.app.data.sharedprefs.SharedPrefs
import com.orpatservice.app.databinding.ActivityCustomerDetailsBinding
import com.orpatservice.app.ui.admin.technician.TechniciansActivity
import com.orpatservice.app.ui.leads.adapter.ComplaintAdapter
import com.orpatservice.app.utils.CommonUtils
import com.orpatservice.app.utils.Constants

/**
 * Created by Vikas Singh on 26/12/21.
 */
class CustomerDetailsActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityCustomerDetailsBinding
    private lateinit var leadData: LeadData
    private lateinit var complaintAdapter: ComplaintAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCustomerDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // set toolbar as support action bar
        setSupportActionBar(binding.toolbar)

        if (SharedPrefs.getInstance().getString(Constants.USER_TYPE, "")
                .equals(Constants.SERVICE_CENTER)
        ) {
            binding.includedContent.btnAssignTechnician.text =
                resources.getString(R.string.btn_assign_to_technician)

        } else {
            binding.includedContent.btnAssignTechnician.text =
                resources.getString(R.string.btn_close_complaint)

        }

        binding.includedContent.btnAssignTechnician.setOnClickListener(this)
        binding.includedContent.ivCall.setOnClickListener(this)

        supportActionBar?.apply {
            title = ""
            // show back button on toolbar
            // on back button press, it will navigate to parent activity
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        if (intent.getStringExtra(Constants.LEAD_TYPE).equals(Constants.LEAD_NEW)) {
            binding.includedContent.btnAssignTechnician.visibility = View.VISIBLE

        } else {
            binding.includedContent.btnAssignTechnician.visibility = View.GONE

        }

        leadData = intent?.getParcelableExtra<LeadData>(Constants.LEAD_DATA) as LeadData

        complaintAdapter = ComplaintAdapter(leadData.enquiries,itemClickListener = onItemClickListener,)

        binding.includedContent.rvComplaint.apply {
            adapter = complaintAdapter
        }

        bindUserDetails(leadData)


    }

    private fun bindUserDetails(leadData: LeadData) {
        binding.includedContent.tvRequestIdValue.text = leadData.id?.toString()
        binding.includedContent.tvInProgress.text = leadData.status
        binding.includedContent.tvCustomerNameValue.text = leadData.name
        binding.includedContent.tvRequestDateValue.text =
            CommonUtils.dateFormat(leadData.created_at)
        binding.includedContent.tvContactNumberValue.text = leadData.mobile
        binding.includedContent.tvPinCodeValue.text = leadData.pincode
        binding.includedContent.tvFullAddressValue.text = leadData.address
    }

    private fun openCallDialPad(contactNumber: String) {
        val i = Intent(Intent.ACTION_DIAL)
        val p = "tel:$contactNumber"
        i.data = Uri.parse(p)
        startActivity(i)
    }

    private fun goToFullScreenImageActivity(invoiceImage: String?) {
        val intent = Intent(this, FullScreenImageActivity::class.java)

        intent.putExtra(Constants.IMAGE_URL, invoiceImage)
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

    private val onItemClickListener: (Int, View) -> Unit = { position, view ->
        when (view.id) {
            R.id.iv_invoice_image -> {
                goToFullScreenImageActivity(leadData.enquiries[position].invoice_image)
            }
            R.id.iv_qr_code_image -> {
                goToFullScreenImageActivity(leadData.enquiries[position].qr_image)
            }
        }
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btn_assign_technician -> {
                if (SharedPrefs.getInstance().getString(Constants.USER_TYPE, "")
                        .equals(Constants.SERVICE_CENTER)
                ) {
                    val intent = Intent(this, TechniciansActivity::class.java)

                    intent.putExtra(Constants.IS_NAV, Constants.ComingFrom.CUSTOMER_DETAILS)
                    intent.putExtra(Constants.LEADS_ID, leadData.id)
                    startActivity(intent)

                } else if (SharedPrefs.getInstance().getString(Constants.USER_TYPE, "")
                        .equals(Constants.TECHNICIAN)
                ) {
                    val intent = Intent(this, CloseComplaintActivity::class.java)

                    intent.putExtra(Constants.IS_NAV, Constants.ComingFrom.CUSTOMER_DETAILS)
                    intent.putExtra(Constants.LEADS_ID, leadData.id)
                    startActivity(intent)
                }

            }
            R.id.iv_call -> {
                openCallDialPad(leadData.mobile.toString())
            }
        }
    }
}