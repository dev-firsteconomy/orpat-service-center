package com.orpatservice.app.ui.leads.technician.adapter

import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.orpatservice.app.R
import com.orpatservice.app.data.model.requests_leads.LeadData
import com.orpatservice.app.data.sharedprefs.SharedPrefs
import com.orpatservice.app.databinding.ItemRequestLeadCardBinding
import com.orpatservice.app.ui.leads.technician.response.TechnicianLeadData
import com.orpatservice.app.utils.Constants
import java.text.SimpleDateFormat
import java.util.ArrayList

class TechnicianRequestLeadAdapter constructor(
    private val leadDataArrayList: ArrayList<TechnicianLeadData>,
    private val itemClickListener: (Int, View) -> Unit,
    private val fragmentType: String
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding: ItemRequestLeadCardBinding =
            ItemRequestLeadCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)

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

    class RequestLeadViewHolder(private val binding: ItemRequestLeadCardBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @RequiresApi(Build.VERSION_CODES.O)
        fun onBind(
            leadData: TechnicianLeadData,
            itemClickListener: (Int, View) -> Unit,
            fragmentType: String) {

            //binding.tvRequestStatus.text = leadData.status
            binding.tvRequestId.text = leadData.complain_id.toString()
            binding.tvRequestCustomerNameValue.text = leadData.name

            val str = leadData.created_at
            val delimiter = " "
            val parts = str?.split(delimiter)

            binding.tvRequestDateTime.text = parts?.get(0)+""+" "+""+ parts?.get(1)+""+" "+""+ parts?.get(2)+""+""+"\n"+ parts?.get(3)+""+" "+""+ parts?.get(4)+"\n"

            //binding.tvRequestDateTime.text = leadData.service_center_assigned_at

            binding.tvRequestTimer.text = leadData.timer
            binding.tvRequestTimer.setTextColor(Color.parseColor(leadData.color_code))

            if (fragmentType.equals(Constants.LEAD_ASSIGN_TECHNICIAN) ||
                fragmentType.equals(Constants.LEAD_COMPLETED_REQUEST) ||
                fragmentType.equals(Constants.LEAD_CANCELLED_REQUEST) ||
                SharedPrefs.getInstance().getString(Constants.USER_TYPE, "").equals(Constants.TECHNICIAN)) {
                //  binding.btnViewDecline.visibility = View.GONE
            } else {
                /*binding.btnViewDecline.visibility = View.VISIBLE
                binding.btnViewDecline.setOnClickListener {
                    itemClickListener(
                        adapterPosition,
                        binding.btnViewDecline
                    )
                }*/
            }
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
            binding.btnDirection.setOnClickListener {
                itemClickListener(
                    adapterPosition,
                    binding.btnDirection
                )
            }

            (leadData.address1+""+" "+""+leadData.address2+ " , " + leadData.landmark).also {
                binding.tvRequestLocation.text = it
            }

          /*  if (leadData.status.equals(Constants.PENDING, ignoreCase = true)) {
                binding.tvRequestStatus.setTextColor(
                    ContextCompat.getColor(
                        binding.tvRequestStatus.context,
                        R.color.orange
                    )
                )
            } else if(leadData.status.equals(Constants.TECHNICIAN_ASSIGNED, ignoreCase = true )) {
                binding.tvRequestStatus.setTextColor(
                    ContextCompat.getColor(
                        binding.tvRequestStatus.context,
                        R.color.green
                    )
                )
            } else {
                binding.tvRequestStatus.setTextColor(
                    ContextCompat.getColor(
                        binding.tvRequestStatus.context,
                        R.color.brown
                    )
                )
            }*/

            val parser =  SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val formatter = SimpleDateFormat("dd MMM HH:mm")
            //   val formattedDate = formatter.format(parser.parse(leadData.created_at))
            // binding.tvRequestDateTime.text = formattedDate
        }
    }
}