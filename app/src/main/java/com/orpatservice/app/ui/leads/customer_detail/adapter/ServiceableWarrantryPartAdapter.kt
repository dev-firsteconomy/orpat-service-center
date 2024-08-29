package com.orpatservice.app.ui.leads.customer_detail.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.orpatservice.app.R
import com.orpatservice.app.data.model.requests_leads.WarrantryCondition
import com.orpatservice.app.data.model.requests_leads.WarrantryPart
import com.orpatservice.app.utils.Constants

class ServiceableWarrantryPartAdapter(private val mList: MutableList<WarrantryPart>,
                                      /*private val itemClickListener: (Int, View, AdapterServiceableWarrantyPartsBinding) -> Unit,*/
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return if(viewType== Constants.PARENT){
            val rowView: View = LayoutInflater.from(parent.context).inflate(R.layout.adapter_serviceable_warranty_parts, parent,false)

            GroupViewHolder(rowView)
        } else {
            val rowView: View = LayoutInflater.from(parent.context).inflate(R.layout.adapter_serviceable_warranty_child_parts, parent,false)

            ChildViewHolder(rowView)
        }
    }

    override fun getItemCount(): Int = mList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val dataList = mList[position]
        //println("warranty_conditions"+mList[position].warranty_conditions.size)
       // println("position"+position)


        if (dataList.type == Constants.PARENT) {
            holder as GroupViewHolder


            holder.apply {
                parentTV?.text = dataList.name

                if(dataList.isExpanded){
                    notCoveredCondition?.visibility = VISIBLE

                }else{
                    notCoveredCondition?.visibility = GONE

                }

                if(dataList.warranty_conditions.count() == 0){
                    down_iv?.visibility = GONE
                }else {
                    down_iv?.visibility = VISIBLE
                }
                    parentTV?.setOnClickListener {
                        //println("warranty_conditions"+mList[position].warranty_conditions.size)
                        println("position"+position)
                        expandOrCollapseParentItem(dataList, position, notCoveredCondition, down_iv)
                    }
                    down_iv?.setOnClickListener {

                        expandOrCollapseParentItem(dataList, position, notCoveredCondition, down_iv)
                    }
            }

        } else {

            holder as ChildViewHolder


            holder.apply {
                val singleService = dataList.warranty_conditions

                for(i in singleService)
                    childTV?.text = "-" + " " + i.title

            }
        }
    }
    private fun expandOrCollapseParentItem(singleBoarding: WarrantryPart,position: Int,notCoveredCondition: TextView?,downImage: ImageView?) {

        if (singleBoarding.isExpanded) {
            notCoveredCondition?.visibility = GONE
            collapseParentRow(position)

        } else {
            notCoveredCondition?.visibility = VISIBLE
            expandParentRow(position)
        }
    }

    private fun expandParentRow(position: Int){
        val currentBoardingRow = mList[position]
        val services = currentBoardingRow.warranty_conditions
        currentBoardingRow.isExpanded = true
        var nextPosition = position
        if(currentBoardingRow.type==Constants.PARENT){

            services.forEach { service ->
                val parentModel =  WarrantryPart()
                parentModel.type = Constants.CHILD

                val subList : ArrayList<WarrantryCondition> = ArrayList()
                val subList1 : ArrayList<WarrantryCondition> = ArrayList()
                subList.add(service)
                //parentModel.warranty_conditions = subList
                for (i in subList) {
                    subList1.add(i)
                    parentModel.warranty_conditions = subList1
                }
                mList.add(++nextPosition,parentModel)
            }
            notifyDataSetChanged()
        }
    }

    private fun collapseParentRow(position: Int){
        val currentBoardingRow = mList[position]
        val services = currentBoardingRow.warranty_conditions
        mList[position].isExpanded = false
        if(mList[position].type==Constants.PARENT){
            services.forEach { _ ->
                mList.removeAt(position + 1)
            }
            notifyDataSetChanged()
        }
    }

    override fun getItemViewType(position: Int): Int = mList[position].type

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    class GroupViewHolder(row: View) : RecyclerView.ViewHolder(row) {
        val parentTV = row.findViewById(R.id.tv_serviceable_warranty_parts_name) as TextView?
        val down_iv = row.findViewById(R.id.down_iv) as ImageView?
        val notCoveredCondition  = row.findViewById(R.id.tv_not_covered_condition) as TextView?
    }
    class ChildViewHolder(row: View) : RecyclerView.ViewHolder(row) {
        val childTV = row.findViewById(R.id.tv_serviceable_warranty_child_parts_name) as TextView?

    }
} /*: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val binding: AdapterServiceableWarrantyPartsBinding =
            AdapterServiceableWarrantyPartsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )

        return ComplaintViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ComplaintViewHolder -> {
                holder.onBind(
                    mList[position],
                    //itemClickListener
                )
            }
        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    class ComplaintViewHolder(private val binding: AdapterServiceableWarrantyPartsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(
            list: WarrantryPart,
            //itemClickListener: (Int, View, AdapterServiceableWarrantyPartsBinding) -> Unit
        ) {
            binding.tvServiceableWarrantyPartsName.text = list.name
        }
    }
}*/