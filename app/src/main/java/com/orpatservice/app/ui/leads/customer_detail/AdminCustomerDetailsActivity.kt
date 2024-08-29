package com.orpatservice.app.ui.leads.customer_detail

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.orpatservice.app.data.model.requests_leads.LeadData
import com.orpatservice.app.databinding.ActivityAdminNewRequestDetailsBinding
import com.orpatservice.app.databinding.ActivityCustomerDetailsBinding
import com.orpatservice.app.ui.leads.adapter.ComplaintAdapter
import com.orpatservice.app.utils.Constants

class AdminCustomerDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminNewRequestDetailsBinding
    private lateinit var leadData: LeadData
    private lateinit var complaintAdapter: ComplaintAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminNewRequestDetailsBinding.inflate(layoutInflater)
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


        leadData = intent?.getParcelableExtra<LeadData>(Constants.LEAD_DATA) as LeadData

        if (leadData.enquiries.isNullOrEmpty()) {
            // binding.includedContent.tvEnquiryHeading.visibility = View.GONE
        }
       /* complaintAdapter = ComplaintAdapter(leadData.enquiries, leadData, itemClickListener = onItemClickListener)

        binding.includedContent.rvComplaint.apply {
            adapter = complaintAdapter
        }*/

        bindUserDetails(leadData)
    }



    private fun bindUserDetails(leadData: LeadData) {
        binding.includedContent.tvCustomerNameValue.text = leadData.name
        binding.includedContent.tvContactNumberValue.text = leadData.mobile
        binding.includedContent.tvPinCodeValue.text = leadData.pincode
        binding.includedContent.tvFullAddressValue.text = leadData.address1+""+" , "+""+leadData.address2/*+""+", "+""+leadData.state*/
        binding.includedContent.tvTvRequestIdValue.text = leadData.complain_id.toString()
        // binding.includedContent.tvRequestDateValue.text = leadData.service_center_assigned_at
        binding.includedContent.tvTimerValue.text = leadData.timer
        binding.includedContent.tvTimerValue.setTextColor(Color.parseColor(leadData.color_code))
        val str = leadData.created_at
        val delimiter = " "
        val parts = str?.split(delimiter)

        binding.includedContent.tvRequestDateValue.text = parts?.get(0)+""+" "+""+ parts?.get(1)+""+" "+""+ parts?.get(2)+""+""+"\n"+ parts?.get(3)+""+" "+""+ parts?.get(4)+"\n"

        binding.includedContent.btnAssignTechnician.setOnClickListener {

            val intent = Intent(this, AllTechnicianActivity::class.java)
            intent.putExtra(Constants.IS_NAV, Constants.ComingFrom.CUSTOMER_DETAILS)
            intent.putExtra(Constants.LEADS_ID, leadData.id.toString())
            intent.putExtra(Constants.TECHNICIAN_ID, leadData.technician?.first_name+""+" "+""+leadData.technician?.last_name)
            //No need to send new lead data because closing complaint perform through adapter
            startActivity(intent)
            finish()

        }

        binding.includedContent.tvContactNumber.setOnClickListener {
            openCallDialPad(leadData.mobile.toString())
        }
        binding.includedContent.tvFullAddressValue.setOnClickListener {
            // openDirection()
        }
        binding.includedContent.btnCreateTask.setOnClickListener {
            openCallDialPad(leadData.mobile.toString())
        }
    }


    private fun openCallDialPad(contactNumber: String) {
        val i = Intent(Intent.ACTION_DIAL)
        val p = "tel:$contactNumber"
        i.data = Uri.parse(p)
        startActivity(i)
    }
}