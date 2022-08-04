package com.orpatservice.app.ui.leads.new_lead_fragment.adapter

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.orpatservice.app.R
import com.orpatservice.app.data.model.requests_leads.LeadData
import com.orpatservice.app.data.sharedprefs.SharedPrefs
import com.orpatservice.app.databinding.AssignTechnicianAdapterBinding
import com.orpatservice.app.databinding.ItemRequestAssignedTechnicianBinding
import com.orpatservice.app.databinding.ItemRequestLeadCardBinding
import com.orpatservice.app.ui.leads.new_lead_fragment.new_lead_request.RequestData
import com.orpatservice.app.utils.Constants
import java.text.SimpleDateFormat
import java.util.ArrayList

class AssignTechnicianLeadAdapter constructor(
    private val leadDataArrayList: ArrayList<LeadData>,
    private val itemClickListener: (Int, View) -> Unit,
    private val fragmentType: String
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding: AssignTechnicianAdapterBinding =
            AssignTechnicianAdapterBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )

        return RequestLeadViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is RequestLeadViewHolder -> {
                holder.onBind(leadDataArrayList[position], itemClickListener, fragmentType)
            }
        }
    }

    override fun getItemCount(): Int {
        return leadDataArrayList.size
    }

    class RequestLeadViewHolder(private val binding: AssignTechnicianAdapterBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @RequiresApi(Build.VERSION_CODES.O)
        fun onBind(
            leadData: LeadData,
            itemClickListener: (Int, View) -> Unit,
            fragmentType: String
        ) {


            if(leadData.status.equals("Parts Verification Pending")){
                binding.btnViewDetails.visibility = VISIBLE
                binding.btnHideViewDetails.visibility = GONE
            }else{
                binding.btnViewDetails.visibility = GONE
                binding.btnHideViewDetails.visibility = VISIBLE
            }
            /*if(leadData.pending_parts_verification_status_count!! > "0"){
                binding.btnViewDetails.visibility = VISIBLE
                binding.btnHideViewDetails.visibility = GONE
            }else{
                binding.btnViewDetails.visibility = GONE
                binding.btnHideViewDetails.visibility = VISIBLE
            }*/

            /*if(leadData.status.equals("Otp Pending")){
                binding.btnUpdateOtp.visibility = View.VISIBLE
                binding.btnHideUpdateOtp.visibility = View.GONE

            }else{
                binding.btnUpdateOtp.visibility = View.GONE
                binding.btnHideUpdateOtp.visibility = View.VISIBLE
            }*/


           /* if(leadData.pending_parts_verification_status_count.equals("0")){
                binding.btnUpdateOtp.visibility = View.VISIBLE
                binding.btnHideUpdateOtp.visibility = View.GONE

            }else{
                binding.btnUpdateOtp.visibility = View.GONE
                binding.btnHideUpdateOtp.visibility = View.VISIBLE
            }*/

            binding.tvRequestId.text = leadData.complain_id.toString()
            binding.tvRequestCustomerNameValue.text = "Customer Name :"+" "+""+leadData.name
           // binding.tvRequestCustomerName.text = leadData.technician?.first_name+""+" "+""+leadData.technician?.last_name
            binding.tvRequestLocation.text = leadData.address1+""+" "+""+leadData.address2/*+""+" "+""+leadData.pincode*/
            binding.tvRequestTechnicianNameValue.text = "Technician Name :"+" "+""+leadData.technician?.first_name+""+" "+""+leadData.technician?.last_name
           // binding.tvRequestDateTime.text = leadData.created_at

            val str = leadData.created_at
            val delimiter = " "
            val parts = str?.split(delimiter)

            binding.tvRequestDateTime.text = parts?.get(0)+""+" "+""+ parts?.get(1)+""+" "+""+ parts?.get(2)+""+""+"\n"+ parts?.get(3)+""+" "+""+ parts?.get(4)+"\n"

            binding.tvRequestTimer.visibility = View.VISIBLE
            binding.tvRequestTimer.text = leadData.timer
            binding.tvRequestTimer.setTextColor(Color.parseColor(leadData.color_code))

            binding.btnViewDetails.setOnClickListener {
                itemClickListener(
                    adapterPosition,
                    binding.btnViewDetails
                )
            }
            binding.tvRequestLocation.setOnClickListener {
                itemClickListener(
                    adapterPosition,
                    binding.tvRequestLocation
                )
            }
            binding.btnUpdateOtp.setOnClickListener {
                itemClickListener(
                    adapterPosition,
                    binding.btnUpdateOtp
                )
            }
            binding.btnHideViewDetails.setOnClickListener {
                itemClickListener(
                    adapterPosition,
                    binding.btnHideViewDetails
                )
            }
            binding.btnHideUpdateOtp.setOnClickListener {
                itemClickListener(
                    adapterPosition,
                    binding.btnHideUpdateOtp
                )
            }
            /*binding.tvTechnicianName.text =
                leadData?.first_name + " " + " " + leadData?.last_name


            binding.btnCompletedTask.setOnClickListener {
                itemClickListener(
                    adapterPosition,kl
                    binding.btnCompletedTask
                )*/

          //  }
        }
    }
}