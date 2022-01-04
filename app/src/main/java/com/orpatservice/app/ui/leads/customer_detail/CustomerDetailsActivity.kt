package com.orpatservice.app.ui.leads.customer_detail

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.orpatservice.app.R
import com.orpatservice.app.databinding.ActivityCustomerDetailsBinding
import com.orpatservice.app.ui.admin.technician.TechniciansActivity
import com.orpatservice.app.ui.data.model.requests_leads.LeadData
import com.orpatservice.app.ui.data.sharedprefs.SharedPrefs
import com.orpatservice.app.utils.CommonUtils
import com.orpatservice.app.utils.Constants

/**
 * Created by Vikas Singh on 26/12/21.
 */
class CustomerDetailsActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityCustomerDetailsBinding
    private lateinit var leadData : LeadData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCustomerDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // set toolbar as support action bar
        setSupportActionBar(binding.toolbar)

        binding.includedContent.btnAssignTechnician.setOnClickListener(this)
        binding.includedContent.ivInvoiceImage.setOnClickListener(this)
        binding.includedContent.ivQrCodeImage.setOnClickListener(this)

        supportActionBar?.apply {
            title = ""
            // show back button on toolbar
            // on back button press, it will navigate to parent activity
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        if (intent.getStringExtra(Constants.LEAD_TYPE).equals(Constants.LEAD_NEW)){
            binding.includedContent.btnAssignTechnician.visibility = View.VISIBLE

        }else{
            binding.includedContent.btnAssignTechnician.visibility = View.GONE

        }

        leadData  = intent?.getParcelableExtra<LeadData>(Constants.LEAD_DATA) as LeadData

        bindUserDetails(leadData)


    }

    private fun bindUserDetails(leadData : LeadData){
        binding.includedContent.tvRequestIdValue.text = leadData.id?.toString()
        binding.includedContent.tvInProgress.text = leadData.status
        binding.includedContent.tvCustomerNameValue.text = leadData.name
        binding.includedContent.tvRequestDateValue.text = CommonUtils.dateFormat(leadData.created_at)
        binding.includedContent.tvContactNumberValue.text = leadData.mobile
        binding.includedContent.tvPinCodeValue.text = leadData.pincode
        binding.includedContent.tvFullAddressValue.text = leadData.address
        binding.includedContent.tvModelNameValue.text = leadData.model_no
        binding.includedContent.tvDateOfPurchaseValue.text = CommonUtils.dateFormat(leadData.purchase_at)
        binding.includedContent.tvWarrantyStatusValue.text = leadData.nature_of_complain

        Log.d("invoice_image",""+leadData.invoice_image)
        Log.d("qr_image",""+leadData.qr_image)

        Glide.with(this)
            .load(leadData.invoice_image)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            //.circleCrop() // .error(R.drawable.active_dot)
            .placeholder(R.color.gray)
            .into(binding.includedContent.ivInvoiceImage)

        Glide.with(this)
            .load(leadData.qr_image)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            //.circleCrop() // .error(R.drawable.active_dot)
            .placeholder(R.color.gray)
            .into(binding.includedContent.ivQrCodeImage)
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

    override fun onClick(view: View?) {
        when(view?.id){
            R.id.btn_assign_technician->{
                if (SharedPrefs.getInstance().getString(Constants.USER_TYPE, "").equals(Constants.SERVICE_CENTER)) {
                    val intent = Intent(this, TechniciansActivity::class.java)

                    intent.putExtra(Constants.IS_NAV, Constants.ComingFrom.CUSTOMER_DETAILS)
                    intent.putExtra(Constants.LEADS_ID, leadData.id)
                    startActivity(intent)

                } else if (SharedPrefs.getInstance().getString(Constants.USER_TYPE, "").equals(Constants.TECHNICIAN)) {

                }

            }
            R.id.iv_invoice_image ->{
                goToFullScreenImageActivity(leadData.invoice_image)
            }
            R.id.iv_qr_code_image ->{
                goToFullScreenImageActivity(leadData.qr_image)
            }
        }
    }
}