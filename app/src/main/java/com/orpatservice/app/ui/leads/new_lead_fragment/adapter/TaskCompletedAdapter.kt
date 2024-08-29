package com.orpatservice.app.ui.leads.new_lead_fragment.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.orpatservice.app.R
import com.orpatservice.app.data.model.requests_leads.LeadData
import com.orpatservice.app.data.model.requests_leads.RequestLeadResponse
import com.orpatservice.app.data.sharedprefs.SharedPrefs
import com.orpatservice.app.databinding.AdapterTaskCompletedBinding
import com.orpatservice.app.databinding.ItemRequestAssignedTechnicianBinding
import com.orpatservice.app.databinding.ItemRequestLeadCardBinding
import com.orpatservice.app.ui.leads.adapter.RequestsLeadsAdapter
import com.orpatservice.app.ui.leads.new_lead_fragment.new_lead_request.RequestData
import com.orpatservice.app.ui.leads.new_lead_fragment.new_lead_request.TaskCompletedRequestData
import com.orpatservice.app.utils.Constants
import java.util.ArrayList

class TaskCompletedAdapter constructor(
    private val leadDataArrayList: ArrayList<TaskCompletedRequestData>,
    private val itemClickListener: (Int, View) -> Unit,
    private val fragmentType: String
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding: AdapterTaskCompletedBinding =
            AdapterTaskCompletedBinding.inflate(LayoutInflater.from(parent.context), parent, false)

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

    class RequestLeadViewHolder(private val binding: AdapterTaskCompletedBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @RequiresApi(Build.VERSION_CODES.O)
        fun onBind(
            leadData: TaskCompletedRequestData,
            itemClickListener: (Int, View) -> Unit,
            fragmentType: String
        ) {

            binding.tvTaskCompletedRequestId.text = leadData.id.toString()
            binding.tvTaskCompletedRequestCustomerNameValue.text = "Customer Name :"+" "+""+leadData.name

            //binding.tvTaskCompletedRequestDateTime.text = leadData.created_at
            val str = leadData.created_at
            val delimiter = " "
            val parts = str?.split(delimiter)

            binding.tvTaskCompletedRequestDateTime.text = parts?.get(0)+""+" "+""+ parts?.get(1)+""+" "+""+ parts?.get(2)+""+""+"\n"+ parts?.get(3)+""+" "+""+ parts?.get(4)+"\n"

            binding.tvStatusValue.text = "Task Complete on"+""+" "+""+leadData.created_at

            if(leadData.status =="Technician  Assigned"){
                binding.tvTaskCompletedRequestStatus.text = "Assigned"
            }else{
                binding.tvTaskCompletedRequestStatus.text = "Pending"
            }
            //binding.tvTaskCompletedRequestStatus.text = leadData.status

            binding.btnViewDetails.setOnClickListener {
                itemClickListener(
                    adapterPosition,
                    binding.btnViewDetails
                )
            }

            (leadData.city + " - " + leadData.state).also {
                binding.tvTaskCompletedRequestLocation.text = it
            }

            if (leadData.status.equals(Constants.PENDING, ignoreCase = true)) {
                binding.tvTaskCompletedRequestStatus.setTextColor(
                    ContextCompat.getColor(
                        binding.tvTaskCompletedRequestStatus.context,
                        R.color.orange
                    )
                )
            } else if (leadData.status.equals(Constants.TECHNICIAN_ASSIGNED, ignoreCase = true)) {
                binding.tvTaskCompletedRequestStatus.setTextColor(
                    ContextCompat.getColor(
                        binding.tvTaskCompletedRequestStatus.context,
                        R.color.green
                    )
                )
            } else {
                binding.tvTaskCompletedRequestStatus.setTextColor(
                    ContextCompat.getColor(
                        binding.tvTaskCompletedRequestStatus.context,
                        R.color.brown
                    )
                )
            }
        }
    }
}