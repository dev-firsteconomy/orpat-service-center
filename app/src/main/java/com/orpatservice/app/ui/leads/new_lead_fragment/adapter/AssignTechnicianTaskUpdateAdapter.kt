package com.orpatservice.app.ui.leads.new_lead_fragment.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.orpatservice.app.R
import com.orpatservice.app.data.model.requests_leads.TaskUpdateData
import com.orpatservice.app.ui.leads.new_lead_fragment.new_lead_request.TaskEnquiry
import com.orpatservice.app.ui.leads.new_lead_fragment.new_lead_request.TaskListData

class AssignTechnicianTaskUpdateAdapter (context: Context, enquaryList: ArrayList<TaskEnquiry>,list: ArrayList<TaskUpdateData>, val itemClickListener: (Int, View, Int, Int) -> Unit) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val THE_FIRST_VIEW = 1
        const val THE_SECOND_VIEW = 2
        const val THE_THIRD_VIEW = 3
    }

    private val yourContext: Context = context
    var list: ArrayList<TaskUpdateData> = list
    var enquaryList: ArrayList<TaskEnquiry> = enquaryList
    var first_item : Int = 0
    var second_item : Int = 0
    var third_item : Int = 0

    private inner class View1ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var model_no: TextView = itemView.findViewById(R.id.tv_model_name_value)
        var in_warranty: TextView = itemView.findViewById(R.id.tv_warranty_date_year)
        var radiobtn_yes: RadioButton = itemView.findViewById(R.id.radiobtn_yes)
        var radiobtn_no: RadioButton = itemView.findViewById(R.id.radiobtn_no)
        var radiobtn_change_part_yes: RadioButton = itemView.findViewById(R.id.radiobtn_change_part_yes)
        var radiobtn_change_part_no: RadioButton = itemView.findViewById(R.id.radiobtn_change_part_no)
        var taskCount : TextView = itemView.findViewById(R.id.tv_task)
        fun bind(position: Int,itemPosition:TaskUpdateData) {

            val recyclerViewModel = list[position]
            val pos = recyclerViewModel.position

            model_no.text = recyclerViewModel.model_no

            in_warranty.text = recyclerViewModel.in_warranty



            radiobtn_yes.setOnClickListener {
                first_item = position
                itemClickListener(
                    third_item,
                    radiobtn_yes,second_item,first_item
                )
            }

            radiobtn_no.setOnClickListener {
                first_item = position
                itemClickListener(
                    third_item,
                    radiobtn_no,second_item,first_item
                )
            }

            radiobtn_change_part_yes.setOnClickListener {
                first_item = position
                itemClickListener(
                    third_item,
                    radiobtn_change_part_yes,second_item,first_item
                )
            }
            radiobtn_change_part_no.setOnClickListener {
                first_item = position
                itemClickListener(
                    third_item,
                    radiobtn_change_part_no,second_item,first_item
                )
            }
        }
    }

    private inner class View2ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var warranty_parts: TextView = itemView.findViewById(R.id.tv_warranty_parts)
        var check_warranty_parts: CheckBox = itemView.findViewById(R.id.check_warranty_parts)

        fun bind(position: Int,itemPosition:TaskUpdateData) {
            val recyclerViewModel = list[position]

            warranty_parts.text = recyclerViewModel.warranty_list?.name


            check_warranty_parts.setOnClickListener {
                second_item = position
                itemClickListener(
                    third_item,
                    check_warranty_parts,second_item,first_item
                )
            }
        }
    }

    private inner class View3ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var task_update: Button = itemView.findViewById(R.id.btn_task_update)
        var btn_hide_task_update: Button = itemView.findViewById(R.id.btn_hide_task_update)
        fun bind(position: Int,itemPosition:TaskUpdateData) {

            val recyclerViewModel = list[position]


            if(recyclerViewModel.pending_count.equals("0")){
                btn_hide_task_update.visibility = VISIBLE
                task_update.visibility = GONE

            }else{
                task_update.visibility = VISIBLE
                btn_hide_task_update.visibility = GONE

            }

            task_update.text = recyclerViewModel.update
            task_update.setOnClickListener {
                third_item = position
                println("third_item"+adapterPosition)
                itemClickListener(
                    third_item,
                    task_update,second_item,first_item
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == THE_FIRST_VIEW) {
            return View1ViewHolder(
                LayoutInflater.from(yourContext).inflate(R.layout.fragment_task_update, parent, false)
            )
        }else if(viewType == THE_SECOND_VIEW){
            return View2ViewHolder(
                LayoutInflater.from(yourContext).inflate(R.layout.under_warranty_parts_adapter, parent, false))
        }else if(viewType == THE_THIRD_VIEW) {
            return View3ViewHolder(
                LayoutInflater.from(yourContext).inflate(R.layout.footer_adapter, parent, false)
            )
        }
        return View1ViewHolder(
            LayoutInflater.from(yourContext).inflate(R.layout.fragment_task_update, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (list[position].theView == THE_FIRST_VIEW) {
            val first_item = list.get(holder.adapterPosition)
            (holder as View1ViewHolder).bind(position, first_item)
        } else if(list[position].theView == THE_SECOND_VIEW) {
            val second_item = list.get(holder.adapterPosition)
            (holder as View2ViewHolder).bind(position, second_item)

        }else if(list[position].theView == THE_THIRD_VIEW) {
            val third_item = list.get(holder.adapterPosition)
            (holder as View3ViewHolder).bind(position, third_item)
        }else{
            val first_item = list.get(holder.adapterPosition)
            (holder as View2ViewHolder).bind(position,first_item)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return list[position].theView
    }
}