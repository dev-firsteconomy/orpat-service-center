package com.orpatservice.app.ui.addtechnician

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.orpatservice.app.R
import com.orpatservice.app.databinding.ItemTechnicianBinding
import com.orpatservice.app.ui.data.model.TechnicianData

class TechnicianAdapter(private val techList: ArrayList<TechnicianData>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding: ItemTechnicianBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_technician, parent, false

        )

        return TechnicianViewHolder(binding)
    }

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

    inner class TechnicianViewHolder(private val binding: ItemTechnicianBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(technicianData: TechnicianData) {
            binding.tvTechName.text = technicianData.name
            binding.tvLocation.text = technicianData.email
            if (technicianData.status==1){
                binding.tvIsAvailable.text = "Available"
            }
        }
    }
}