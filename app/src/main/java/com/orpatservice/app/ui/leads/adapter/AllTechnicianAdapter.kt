package com.orpatservice.app.ui.leads.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.orpatservice.app.R
import com.orpatservice.app.base.Callback
import com.orpatservice.app.databinding.AdapterAllTechnicianBinding
import com.orpatservice.app.ui.leads.new_lead_fragment.new_lead_request.RequestData

class AllTechnicianAdapter (
    private val techList: ArrayList<RequestData>,
    private val itemClickListener: (Int, View) -> Unit,
    private val is_nav: String?,
    private val technician_user: String?,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var callback: Callback? = null
    private var selectedPosition = -1

    private var lastCheckedPosition = -1

    //private var rbChecked: RadioButton? = null
    //private var rbPosoition = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding: AdapterAllTechnicianBinding =
            AdapterAllTechnicianBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return TechnicianViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is TechnicianViewHolder -> {
                holder.bind(techList[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return techList.size
    }

    inner class TechnicianViewHolder(private val binding: AdapterAllTechnicianBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
           // binding.btnAssignAlltechnician.setOnClickListener(this)
        }

        fun bind(technicianData: RequestData) {

               /* Glide.with(binding.ivProfileImage)
                    .load(technicianData.technician?.image)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .circleCrop() // .error(R.drawable.active_dot)
                    .placeholder(R.drawable.ic_tech_avatar)
                    .into(binding.ivProfileImage)*/


                    binding.tvTechnicianName.text =
                        technicianData?.first_name + " " + technicianData?.last_name
                   /* binding.tvAreaCode.text =
                        "Area Code:" + " " + "" + technicianData?.pincode*/
            if( technicianData.active_leads_count == null){
                binding.tvTechnicianLeadCount.text = "Lead Count: " + "" + " " + "" + "0"
            }else{
                binding.tvTechnicianLeadCount.text = "Lead Count: " + "" + " " + "" + technicianData.active_leads_count
            }

            binding.radioTechnician.setChecked(position == lastCheckedPosition);
            binding.liTechnicianName.setOnClickListener {
                val copyOfLastCheckedPosition = lastCheckedPosition
                lastCheckedPosition = adapterPosition
                notifyItemChanged(copyOfLastCheckedPosition)
                notifyItemChanged(lastCheckedPosition)

                itemClickListener(
                    lastCheckedPosition,
                    binding.liTechnicianName)
            }
           /* if(technicianData.active_leads_count != null) {
                binding.tvLeadCount.text =
                    "Lead Count: " + "" + " " + "" + technicianData.active_leads_count
            }*/
            binding.radioTechnician.setOnClickListener {
                /*if(binding.radioTechnician.isChecked){
                    itemClickListener(
                        adapterPosition,
                        binding.radioTechnician)
                }else{
                    binding.radioTechnician.isChecked = false
                }*/

                val copyOfLastCheckedPosition = lastCheckedPosition
                lastCheckedPosition = adapterPosition
                notifyItemChanged(copyOfLastCheckedPosition)
                notifyItemChanged(lastCheckedPosition)

                itemClickListener(
                    lastCheckedPosition,
                    binding.radioTechnician)
            }

           // binding.radioTechnician.isChecked = lastCheckedPosition == adapterPosition
                   /* binding.btnAssignAlltechnician.isChecked =
                        lastCheckedPosition == adapterPosition*/

        }


        override fun onClick(view: View?) {
            when (view?.id) {
              /*  R.id.btn_assign_alltechnician -> {
                    val copyOfLastCheckedPosition: Int = lastCheckedPosition
                    lastCheckedPosition = adapterPosition
                    notifyItemChanged(copyOfLastCheckedPosition)
                    notifyItemChanged(lastCheckedPosition)

                    view.let {
                        if (it != null) {
                            callback?.onItemClick(it, adapterPosition)
                        }
                    }
                }
                else -> {
                    view?.let { callback?.onItemClick(it, adapterPosition) }
                }*/
                R.id.radio_technician -> {
                    val copyOfLastCheckedPosition: Int = lastCheckedPosition
                    lastCheckedPosition = adapterPosition
                    notifyItemChanged(copyOfLastCheckedPosition)
                    notifyItemChanged(lastCheckedPosition)

                    view.let {
                        if (it != null) {
                            callback?.onItemClick(it, adapterPosition)
                        }
                    }
                }
                else -> {
                    view?.let { callback?.onItemClick(it, adapterPosition) }
                }
            }
        }
    }

}