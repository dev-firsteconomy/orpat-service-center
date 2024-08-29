package com.orpatservice.app.ui.leads.chargeable_request

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.gson.JsonObject
import com.orpatservice.app.R
import com.orpatservice.app.data.Resource
import com.orpatservice.app.data.Status
import com.orpatservice.app.data.model.requests_leads.LeadData
import com.orpatservice.app.databinding.ActivityChargeableRequestDetailsBinding
import com.orpatservice.app.databinding.ActivityCustomerDetailsBinding
import com.orpatservice.app.ui.leads.customer_detail.CancelRequestResponse
import com.orpatservice.app.ui.leads.customer_detail.CustomerDetailsModel
import com.orpatservice.app.ui.leads.service_center.RequestLeadActivity
import com.orpatservice.app.utils.Constants
import com.orpatservice.app.utils.Utils
import org.json.JSONException

class ChargeableRequestDetailsActivity: AppCompatActivity() {
    private lateinit var binding: ActivityChargeableRequestDetailsBinding
    private lateinit var leadData: LeadData
    lateinit var customerDetailsViewModel: CustomerDetailsModel
    private lateinit var alertDialogBuilder:Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChargeableRequestDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        customerDetailsViewModel = ViewModelProvider(this)[CustomerDetailsModel::class.java]
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

        userDetails()

    }

    private fun userDetails() {

        binding.tvCustomerNameValue.text = leadData.name
        binding.tvContactNumValue.text = leadData.mobile
        binding.tvRequestIdValue.text = leadData.complain_id
        binding.tvRequestDateValue.text = leadData.created_at
        binding.tvPincodeValue.text = leadData.pincode
        binding.tvStatusValue.text = leadData.status
        binding.tvAddressValue.text = leadData.address1+""+" "+""+leadData.address2+""+", "+""+leadData.landmark

        if(leadData.is_open.equals("false")){
            binding.btnCancel.visibility = GONE
            binding.btnFinish.visibility = GONE

          //  binding.btnCancelHide.visibility = VISIBLE
          //  binding.btnFinishHide.visibility = VISIBLE
        }else{
            binding.btnCancel.visibility = VISIBLE
            binding.btnFinish.visibility = VISIBLE

          //  binding.btnCancelHide.visibility = GONE
          //  binding.btnFinishHide.visibility = GONE
        }

        binding.tvContactNumValue.setOnClickListener {

            openCallDialPad(leadData.mobile.toString())
        }

        binding.btnCancel.setOnClickListener {

            showCancelLeadPopUp()
        }

        binding.btnFinish.setOnClickListener {

            hitFinishApi()
        }
    }

    private fun hitFinishApi() {

        val jsonObject = JsonObject()

        try {
            jsonObject.addProperty("message", "")
            jsonObject.addProperty("type", "1")

        } catch (e: JSONException) {
            e.printStackTrace()
        }

        customerDetailsViewModel.hitChargeableCancelRequest(jsonObject, leadData.id)
            .observe(this, this::getCancelRequestLead)

        //alertDialogBuilder.dismiss()

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                 onBackPressed()
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun openCallDialPad(contactNumber: String) {
        val i = Intent(Intent.ACTION_DIAL)
        val p = "tel:$contactNumber"
        i.data = Uri.parse(p)
        startActivity(i)
    }

    private fun showCancelLeadPopUp() {

        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View = inflater.inflate(R.layout.cancel_lead_popup_layout, null)
        var tv_cancel_lead_cancel = view.findViewById<TextView>(R.id.tv_cancel_lead_cancel)
        val tv_cancel_lead_ok = view.findViewById<TextView>(R.id.tv_cancel_lead_ok)
        val et_cancel_lead_reason = view.findViewById<EditText>(R.id.et_cancel_lead_reason)
        alertDialogBuilder = Dialog(this@ChargeableRequestDetailsActivity)
        alertDialogBuilder.setContentView(view)
        //alertDialogBuilder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialogBuilder.setCancelable(false)
        tv_cancel_lead_cancel.setOnClickListener {
            alertDialogBuilder.dismiss()
        }
        tv_cancel_lead_ok.setOnClickListener {

            hitCancelLead(et_cancel_lead_reason, alertDialogBuilder)
        }
        alertDialogBuilder.show()

    }

    private fun hitCancelLead(etCancelLeadReason: EditText?,alertDialogBuilder: Dialog) {
        if (Utils.instance.validateReason(etCancelLeadReason)

        ) {
            val jsonObject = JsonObject()

            try {
                //  val jsArray  = JsonArray()
                jsonObject.addProperty("message", etCancelLeadReason?.text.toString())
                jsonObject.addProperty("type", "2")

            } catch (e: JSONException) {
                e.printStackTrace()
            }

            customerDetailsViewModel.hitChargeableCancelRequest(jsonObject, leadData.id)
                .observe(this, this::getCancelRequestLead)

            alertDialogBuilder.dismiss()
        }else{

        }
    }


    private fun getCancelRequestLead(resources: Resource<CancelRequestResponse>) {
        when (resources.status) {
            Status.LOADING -> {
                binding.cpiLoading.visibility = View.VISIBLE
            }
            Status.ERROR -> {
                binding.cpiLoading.visibility = View.GONE
            }
            else -> {
                binding.cpiLoading.visibility = View.GONE
                val response = resources.data

                response?.let {
                    if (it.success) {

                        it.message?.toString()?.let { it1 ->
                            Utils.instance.popupUtil(this,
                                it1,
                                "",
                                true)
                        }
                        Handler(Looper.getMainLooper()).postDelayed({
                            val intent = Intent(this, ChargeableRequestActivity::class.java)
                            startActivity(intent)
                            finish()
                        }, 5000)

                    } else {

                    }
                }
            }
        }
    }
}