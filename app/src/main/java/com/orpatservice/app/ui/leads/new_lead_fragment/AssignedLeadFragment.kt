package com.orpatservice.app.ui.leads.new_lead_fragment

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.orpatservice.app.R
import com.orpatservice.app.data.Resource
import com.orpatservice.app.data.Status
import com.orpatservice.app.data.model.requests_leads.CancelLeadResponse
import com.orpatservice.app.data.model.requests_leads.LeadData
import com.orpatservice.app.data.model.requests_leads.RequestLeadResponse
import com.orpatservice.app.databinding.FragmentAssignedLeadBinding
import com.orpatservice.app.databinding.ItemNewRequestAdminBinding
import com.orpatservice.app.databinding.LayoutDialogCancelLeadBinding
import com.orpatservice.app.ui.admin.technician.TechniciansViewModel
import com.orpatservice.app.ui.leads.adapter.RequestsLeadsAdapter
import com.orpatservice.app.ui.leads.customer_detail.CustomerDetailsActivity
import com.orpatservice.app.ui.leads.new_lead_fragment.activity.ChangeTechnicianActivity
import com.orpatservice.app.ui.leads.new_lead_fragment.adapter.AssignedLeadAdapter
import com.orpatservice.app.ui.leads.service_center.AssignDetailsActivity
import com.orpatservice.app.ui.leads.service_center.RequestLeadActivity
import com.orpatservice.app.ui.leads.viewmodel.RequestsLeadsViewModel
import com.orpatservice.app.utils.Constants
import com.orpatservice.app.utils.Utils

class AssignedLeadFragment  : Fragment() {

    private lateinit var binding: FragmentAssignedLeadBinding
    private var leadDataArrayList: ArrayList<LeadData> = ArrayList()
    private var tempDataArrayList: ArrayList<LeadData> = ArrayList()
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var requestLeadsViewModel: TechniciansViewModel
    private var removeIndex = -1
    private var isLoading: Boolean = false
    private var pageNumber = 1
    private var totalPage = 1
    private var total = 0
    private  var totalCount :TextView? = null

    //Click listener for List Item
    private val onItemClickListener: (Int, View) -> Unit = { position, view ->
        when (view.id) {
            R.id.btn_view_details -> {
                val intent = Intent(activity, AssignDetailsActivity::class.java)

                intent.putExtra(Constants.LEAD_DATA, leadDataArrayList[position])
                //No need to send new lead data because closing complaint perform through adapter
                intent.putExtra(Constants.LEAD_TYPE, Constants.LEAD_NEW)
                startActivity(intent)
            }
            R.id.btn_change_technician -> {

                val intent = Intent(activity, ChangeTechnicianActivity::class.java)
                intent.putExtra(Constants.LEADS_ID, leadDataArrayList[position].id.toString())
                intent.putExtra(Constants.SELECTED_PARTS, position.toString())
                intent.putExtra(Constants.TECHNICIAN_ID, leadDataArrayList[position].technician?.first_name+""+" "+""+leadDataArrayList[position].technician?.last_name)
                startActivity(intent)
            }
            R.id.tv_request_location -> {
                //openDirection(position)
            }
            R.id.btn_hide_change_technician ->{

                Utils.instance.popupPinUtil(requireActivity(),
                    "Technician cannot be updated at this stage",
                    "",
                    false)
            }
        }
    }
    private val requestsLeadsAdapter = AssignedLeadAdapter(
        leadDataArrayList, itemClickListener = onItemClickListener, Constants.LEAD_NEW
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentAssignedLeadBinding.inflate(inflater, container, false)

        requestLeadsViewModel = ViewModelProvider(this)[TechniciansViewModel::class.java]


        setObserver()
        loadUI()
        utilBindData()

        binding.rvNewRequest.addOnScrollListener(scrollListener)

        return binding.root
    }

    private fun utilBindData() {

        layoutManager = LinearLayoutManager(activity)
        binding.rvNewRequest.layoutManager = layoutManager
        binding.rvNewRequest.apply {
            adapter = requestsLeadsAdapter
        }

        binding.edtSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {

            }

            override fun beforeTextChanged(char: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(charSeq: CharSequence?, p1: Int, p2: Int, p3: Int) {

                if(!charSeq.isNullOrEmpty()){
                    leadDataArrayList.clear()
                    tempDataArrayList.clear()
                    filter(charSeq.toString())
                }else{
                    leadDataArrayList.clear()
                    tempDataArrayList.clear()
                    filter("")

                }
            }
        })
    }

    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            if (!isLoading) {
                if (layoutManager.findLastCompletelyVisibleItemPosition() == leadDataArrayList.size - 1 && totalPage > pageNumber) {
                    pageNumber++
                    binding.cpiLoading.visibility = View.VISIBLE
                    requestLeadsViewModel.assignTechnicianLead(pageNumber)
                    isLoading = true
                }
            }
        }
    }

    private fun setObserver() {
        requestLeadsViewModel.assignTechnicianLead(pageNumber)
        requestLeadsViewModel.assignTechnicianData.observe(viewLifecycleOwner, this::getAssignedLeads)
    }

    private fun getAssignedLeads(resources: Resource<RequestLeadResponse>) {
        when (resources.status) {
            Status.LOADING -> {
                // binding.cpiLoading.visibility = View.VISIBLE
            }
            Status.ERROR -> {
                binding.cpiLoading.visibility = View.GONE
                isLoading = false
                /*activity?.let {
                    Alerter.create(it)
                        .setText(resources.error?.message.toString())
                        .setBackgroundColorRes(R.color.orange)
                        .setDuration(1500)
                        .show()
                }*/

                Utils.instance.popupPinUtil(requireActivity(),
                    resources.error?.message.toString(),
                    "",
                    false)
            }
            else -> {
                binding.cpiLoading.visibility = View.GONE

                val response = resources.data

                response?.let {
                    if (it.success) {
                        totalPage = response.data.pagination.last_page
                        total = response.data.pagination.total

                        Constants.ASSIGN_TOTAL = total.toString()
                        
                        totalCount?.text = Constants.ASSIGN_TOTAL

                        leadDataArrayList.clear()
                        tempDataArrayList.clear()
                        leadDataArrayList.addAll(response.data.data)
                        tempDataArrayList.addAll(response.data.data)
                        requestsLeadsAdapter.notifyDataSetChanged()

                        isLoading = false

                        if(leadDataArrayList.isNullOrEmpty()){
                            binding.tvNoLeads.visibility = View.VISIBLE
                        } else {
                            binding.tvNoLeads.visibility = View.GONE
                        }
                    }
                }
            }
        }
    }
    fun filter(text: String) {
        loadSearchLead(text)
    }

    override fun onResume() {
        super.onResume()
        //totalCount?.text = Constants.ASSIGN_TOTAL.toString()
        setObserver()

    }

    fun loadTotalLead(toolbarTotalLead: TextView) {
        totalCount = toolbarTotalLead
        println("toolbarTotalLead"+toolbarTotalLead)
        totalCount?.text = Constants.ASSIGN_TOTAL.toString()
    }


    private fun openDirection(position: Int) {
        val dir: String
        if(!leadDataArrayList[position].latitude.isNullOrEmpty() && !leadDataArrayList[position].longitude.isNullOrEmpty()) {
            dir = leadDataArrayList[position].latitude + "" + "," + "" + leadDataArrayList[position].longitude
        }else{
            dir = "" + "" + "," + "" +""
        }
        val intentUri = Uri.Builder().apply {
            scheme("https")
            authority("www.google.com")
            appendPath("maps")
            appendPath("dir")
            appendPath("")
            appendQueryParameter("api", "1")
            appendQueryParameter("destination", dir)
            // appendQueryParameter("destination", "${leadDataArrayList[position].latitude},-${(leadDataArrayList[position].longitude)}")
        }.build()
        startActivity(Intent(Intent.ACTION_VIEW).apply {
            data = intentUri
        })
    }



    private fun getPendingLeads(resources: Resource<RequestLeadResponse>) {
        when (resources.status) {
            Status.LOADING -> {
                binding.cpiLoading.visibility = View.VISIBLE
            }
            Status.ERROR -> {
                binding.cpiLoading.visibility = View.GONE
                isLoading = false
                activity?.let {
                    /* Alerter.create(it)
                         .setText(resources.error?.message.toString())
                         .setBackgroundColorRes(R.color.orange)
                         .setDuration(1500)
                         .show()*/

                    Utils.instance.popupPinUtil(requireActivity(),
                        resources.error?.message.toString(),
                        "",
                        false)
                }
            }
            else -> {
                binding.cpiLoading.visibility = View.GONE

                val response = resources.data

                response?.let {
                    if (it.success) {
                        totalPage = response.data.pagination.last_page
                        leadDataArrayList.addAll(response.data.data)
                        tempDataArrayList.addAll(response.data.data)
                        requestsLeadsAdapter.notifyDataSetChanged()

                        isLoading = false

                        if(leadDataArrayList.isNullOrEmpty()){
                            binding.tvNoLeads.visibility = View.VISIBLE
                        } else {
                            binding.tvNoLeads.visibility = View.GONE
                        }
                    }
                }
            }
        }
    }

    private fun doCancelLead(resources: Resource<CancelLeadResponse>) {
        when (resources.status) {
            Status.LOADING -> {
                binding.cpiLoading.visibility = View.VISIBLE
            }
            Status.ERROR -> {
                binding.cpiLoading.visibility = View.GONE
                activity?.let {
                    /* Alerter.create(it)
                         .setText(resources.error?.message.toString())
                         .setBackgroundColorRes(R.color.orange)
                         .setDuration(1500)
                         .show()*/

                    Utils.instance.popupPinUtil(requireActivity(),
                        resources.error?.message.toString(),
                        "",
                        false)
                }
            }
            else -> {
                binding.cpiLoading.visibility = View.GONE

                val response = resources.data

                response?.let {
                    if (it.success) {

                        if (removeIndex != -1) {
                            leadDataArrayList.removeAt(removeIndex)
                            requestsLeadsAdapter.notifyItemRemoved(removeIndex)

                        }
                        activity?.let {
                            /* Alerter.create(it)
                                 .setText(resources.data.message)
                                 .setBackgroundColorRes(R.color.orange)
                                 .setDuration(1500)
                                 .show()*/

                            Utils.instance.popupPinUtil(requireActivity(),
                                resources.data.message,
                                "",
                                true)
                        }
                    }
                }
            }
        }
    }

    fun loadSearchLead(query: String) {
        leadDataArrayList.clear()
        tempDataArrayList.clear()
        requestLeadsViewModel.searchAssignedLeads(query)
        tempDataArrayList.clear()
        tempDataArrayList.addAll(leadDataArrayList)
        leadDataArrayList.clear()
        requestsLeadsAdapter.notifyDataSetChanged()
        isLoading = true
        loadUI()
    }

    fun loadOldLeadData() {
        leadDataArrayList.clear()
        leadDataArrayList.addAll(tempDataArrayList)
        requestsLeadsAdapter.notifyDataSetChanged()
    }

   /* private fun confirmationDialog(context: Context, id: Int?) {

        val dialogBinding = LayoutDialogCancelLeadBinding.inflate(layoutInflater)
        val dialog = MaterialAlertDialogBuilder(context)
            .setView(dialogBinding.root)
        dialog.setCancelable(false)
        val alertDialog = dialog.show() as AlertDialog

        dialogBinding.btnReject.setOnClickListener {
            if (id != null && Utils.instance.validateEditText(dialogBinding.edtRejectionReason)) {
                requestLeadsViewModel.doCancelLead(id, dialogBinding.edtRejectionReason.text.toString())
                binding.cpiLoading.visibility = View.VISIBLE
                alertDialog.dismiss()
            } else {
                Toast.makeText(activity, "Lead Id not found. Please try again!", Toast.LENGTH_SHORT)
                    .show()
                alertDialog.dismiss()
            }
        }
        dialogBinding.btnCancel.setOnClickListener{
            alertDialog.dismiss()
        }
    }*/



    private fun loadUI () {
        binding.tvNoLeads.visibility = View.GONE
        binding.cpiLoading.visibility = View.VISIBLE
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AssignedLeadFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}