package com.orpatservice.app.ui.leads.new_lead_fragment.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.orpatservice.app.R
import com.orpatservice.app.data.model.requests_leads.WarrantryPart
import com.orpatservice.app.ui.leads.new_lead_fragment.new_lead_request.*

class TaskUpdateAdapter(context: Context, list: ArrayList<TaskListData>, val itemClickListener: (Int, View) -> Unit) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val THE_FIRST_VIEW = 1
        const val THE_SECOND_VIEW = 2
        const val THE_THIRD_VIEW = 3
    }

    private val yourContext: Context = context
    var list: ArrayList<TaskListData> = list

    private inner class GfgViewOne(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var model_no: TextView = itemView.findViewById(R.id.tv_task_completed_model_name_value)
        var in_warranty: TextView = itemView.findViewById(R.id.tv_task_completed_warranty_date_year)
        var taskCount : TextView = itemView.findViewById(R.id.tv_task)
        fun bind(position: Int) {

            val recyclerViewModel = list[position]
            val pos = recyclerViewModel.position
            model_no.text = recyclerViewModel.parentText
            println("recyclerViewModel.count"+recyclerViewModel.count)
            in_warranty.text = recyclerViewModel.warrantyText

            taskCount.text = "Task"+" "+  recyclerViewModel.position+"/"+recyclerViewModel.count

        }
    }

    private inner class View2ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var tv_warranty_parts: TextView = itemView.findViewById(R.id.tv_warranty_parts)

        fun bind(position: Int) {
            val recyclerViewModel = list[position]
            tv_warranty_parts.text = recyclerViewModel.parentText
        }
    }

    private inner class View3ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var btn_otp_varification: TextView = itemView.findViewById(R.id.btn_otp_varification)
        fun bind(position: Int) {
            val recyclerViewModel = list[position]
            btn_otp_varification.text = recyclerViewModel.parentText
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == THE_FIRST_VIEW) {
            return GfgViewOne(
                LayoutInflater.from(yourContext).inflate(R.layout.item_complete_task, parent, false)
            )
        }else if(viewType == THE_SECOND_VIEW){
            return View2ViewHolder(
                LayoutInflater.from(yourContext).inflate(R.layout.item_parts_task_completed, parent, false))
        }else if(viewType == THE_THIRD_VIEW) {
            return View3ViewHolder(
                LayoutInflater.from(yourContext).inflate(R.layout.rv_footer, parent, false)
            )
        }
        return GfgViewOne(
            LayoutInflater.from(yourContext).inflate(R.layout.item_complete_task, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (list[position].theView == THE_FIRST_VIEW) {
            (holder as GfgViewOne).bind(position)
        } else if(list[position].theView == THE_SECOND_VIEW) {
            (holder as View2ViewHolder).bind(position)
        }else if(list[position].theView == THE_THIRD_VIEW) {
            (holder as View3ViewHolder).bind(position)
        }else{
            (holder as View2ViewHolder).bind(position)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return list[position].theView
    }
}