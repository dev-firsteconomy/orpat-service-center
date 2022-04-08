package com.orpatservice.app.ui.leads.adapter

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
import com.orpatservice.app.utils.Constants
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import java.time.LocalDate as LocalDate1

/**
 * Created by Ajay Yadav on 22/12/21.
 */
class RequestsLeadsAdapter constructor(
    private val leadDataArrayList: ArrayList<LeadData>,
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
            leadData: LeadData,
            itemClickListener: (Int, View) -> Unit,
            fragmentType: String) {

            //binding.tvRequestStatus.text = leadData.status
            binding.tvRequestId.text = leadData.id.toString()
            binding.tvRequestCustomerName.text = leadData.name

           // var datetime = leadData.created_at
         //   val ldt: LocalDateTime = LocalDateTime.parse(datetime)
           // println("datetime"+ldt.toString())
          //  val date = LocalDate1.parse(datetime, DateTimeFormatter.ISO_DATE)

            binding.tvRequestDateTime.text = leadData.created_at

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

            (leadData.city + " - " + leadData.state).also {
                binding.tvRequestLocation.text = it
            }

            if (leadData.status.equals(Constants.PENDING, ignoreCase = true)) {
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
            }

            val parser =  SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val formatter = SimpleDateFormat("dd MMM HH:mm")
         //   val formattedDate = formatter.format(parser.parse(leadData.created_at))
           // binding.tvRequestDateTime.text = formattedDate
        }
    }
}