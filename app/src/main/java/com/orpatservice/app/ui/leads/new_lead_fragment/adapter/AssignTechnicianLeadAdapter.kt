package com.orpatservice.app.ui.leads.new_lead_fragment.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.orpatservice.app.R
import com.orpatservice.app.data.sharedprefs.SharedPrefs
import com.orpatservice.app.databinding.ItemRequestAssignedTechnicianBinding
import com.orpatservice.app.databinding.ItemRequestLeadCardBinding
import com.orpatservice.app.ui.leads.new_lead_fragment.new_lead_request.RequestData
import com.orpatservice.app.utils.Constants
import java.text.SimpleDateFormat
import java.util.ArrayList

class AssignTechnicianLeadAdapter constructor(
    private val leadDataArrayList: ArrayList<RequestData>,
    private val itemClickListener: (Int, View) -> Unit,
    private val fragmentType: String
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding: ItemRequestAssignedTechnicianBinding =
            ItemRequestAssignedTechnicianBinding.inflate(LayoutInflater.from(parent.context), parent, false)

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

    class RequestLeadViewHolder(private val binding: ItemRequestAssignedTechnicianBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @RequiresApi(Build.VERSION_CODES.O)
        fun onBind(
            leadData: RequestData,
            itemClickListener: (Int, View) -> Unit,
            fragmentType: String) {

            binding.tvRequestId.text = leadData.id.toString()
            binding.tvTechnicianName.text = leadData.first_name+" "+" "+leadData.last_name

            binding.btnViewDetails.setOnClickListener {
                itemClickListener(
                    adapterPosition,
                    binding.btnViewDetails
                )
            }

        }
    }
}