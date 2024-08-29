package com.orpatservice.app.ui.leads.new_lead_fragment.adapter

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.orpatservice.app.R
import com.orpatservice.app.base.Callback
import com.orpatservice.app.data.model.requests_leads.Enquiry
import com.orpatservice.app.databinding.TaskCompletedDetailsAdapterBinding
import com.orpatservice.app.databinding.TechnicianTaskUpdateBinding
import com.orpatservice.app.ui.leads.new_lead_fragment.new_lead_request.TaskEnquiry
import com.orpatservice.app.ui.leads.technician.section.WarrantryPartAdapter
import com.orpatservice.app.utils.DividerItemDecorator

class TaskCompletedDetailsAdapter (
    private val context: Context,
    private val techList: ArrayList<Enquiry>,
    private val itemClickListener: (Int, View) -> Unit,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var callback: Callback? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding: TaskCompletedDetailsAdapterBinding =
            TaskCompletedDetailsAdapterBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return TechnicianViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is TechnicianViewHolder -> {
                holder.bind(techList[position],context)
            }
        }
    }

    override fun getItemCount(): Int {
        return techList.size
    }

    inner class TechnicianViewHolder(private val binding: TaskCompletedDetailsAdapterBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        private lateinit var linearLayoutManager: LinearLayoutManager
        init {
            // binding.btnAssignAlltechnician.setOnClickListener(this)
        }

        fun bind(technicianData: Enquiry, context : Context) {

            val  pos = position +1
            val  count  = techList.count()

           /* if(technicianData.pending_parts_verification_status_count.equals("0")){
                binding.tvOtpVerification.visibility = View.VISIBLE
                binding.tvHideOtpVerification.visibility = View.GONE

            }else{
                binding.tvOtpVerification.visibility = View.GONE
                binding.tvHideOtpVerification.visibility = View.VISIBLE
            }*/

            binding.tvTaskCompletedModelNameValue.text = technicianData?.model_no
            binding.tvTaskCompletedWarranty.text = technicianData?.in_warranty
            binding.tvTask.text = "Task"+""+pos+""+"/"+""+count

            val warrantryPart = TaskCompletedWarrantyAdapter(technicianData.lead_enquiry_images)
            val dividerItemDecoration: RecyclerView.ItemDecoration =
                DividerItemDecorator(ContextCompat.getDrawable(context, R.drawable.rv_divider))

            linearLayoutManager = LinearLayoutManager(context)
            binding.rvWarrantyList.apply {
                adapter = warrantryPart
                //addItemDecoration(dividerItemDecoration)
                layoutManager = linearLayoutManager
            }

            /*binding.tvOtpVerification.setOnClickListener {
                itemClickListener(
                    adapterPosition,
                    binding.tvOtpVerification
                )
            }*/
        }

        override fun onClick(view: View?) {
            when (view?.id) {
            }
        }
    }
}