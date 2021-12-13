package com.orpatservice.app.ui.addtechnician

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.orpatservice.app.databinding.ItemTechnicianBinding
import com.orpatservice.app.ui.data.model.TechnicianData

class TechnicianAdapter(private val techList: ArrayList<TechnicianData>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding: ItemTechnicianBinding =
            ItemTechnicianBinding.inflate(LayoutInflater.from(parent.context))
        return TechnicianViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is TechnicianViewHolder -> {
            }
        }
    }

    override fun getItemCount(): Int {
        return techList.size
    }

    inner class TechnicianViewHolder(private val binding: ItemTechnicianBinding) :
        RecyclerView.ViewHolder(binding.root)
}