package com.orpatservice.app.ui.leads.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.orpatservice.app.R
import com.orpatservice.app.databinding.ItemRequestLeadCardBinding
import com.orpatservice.app.ui.data.model.requests_leads.LeadData
import com.orpatservice.app.ui.data.sharedprefs.SharedPrefs
import com.orpatservice.app.utils.Constants
import java.text.SimpleDateFormat

/**
 * Created by Ajay Yadav on 22/12/21.
 */
class ComplaintAdapter constructor(
    private val leadDataArrayList: ArrayList<LeadData>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding: ItemRequestLeadCardBinding =
            ItemRequestLeadCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return RequestLeadViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is RequestLeadViewHolder -> {
                holder.onBind(leadDataArrayList[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return leadDataArrayList.size
    }

    class RequestLeadViewHolder(private val binding: ItemRequestLeadCardBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(leadData: LeadData ) {

            binding.tvRequestStatus.text = leadData.status
            binding.tvRequestId.text = leadData.id.toString()
            binding.tvRequestCustomerName.text = leadData.name


            (leadData.address + " - " + leadData.pincode).also {
                binding.tvRequestLocation.text = it
            }

            if (leadData.status.equals(Constants.PENDING, ignoreCase = true)) {
                binding.tvRequestStatus.setTextColor(
                    ContextCompat.getColor(
                        binding.tvRequestStatus.context,
                        R.color.orange
                    )
                )
            } else {
                binding.tvRequestStatus.setTextColor(
                    ContextCompat.getColor(
                        binding.tvRequestStatus.context,
                        R.color.brown
                    )
                )
            }

            val parser =  SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val formatter = SimpleDateFormat("dd MMM HH:mm")
            val formattedDate = formatter.format(parser.parse(leadData.created_at))
            binding.tvRequestDateTime.text = formattedDate
        }
    }
}