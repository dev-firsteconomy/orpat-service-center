package com.orpatservice.app.ui.leads.chargeable_request

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
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
import com.orpatservice.app.databinding.ChargeableNewRequestAdminBinding
import com.orpatservice.app.databinding.ItemNewRequestAdminBinding
import com.orpatservice.app.databinding.LayoutDialogCancelLeadBinding
import com.orpatservice.app.ui.leads.adapter.RequestsLeadsAdapter
import com.orpatservice.app.ui.leads.customer_detail.CustomerDetailsActivity
import com.orpatservice.app.ui.leads.new_lead_fragment.NewRequestsFragment
import com.orpatservice.app.ui.leads.viewmodel.RequestsLeadsViewModel
import com.orpatservice.app.utils.Constants
import com.orpatservice.app.utils.Utils

class ChargeableNewRequest : Fragment() {

    private lateinit var binding: ChargeableNewRequestAdminBinding
    private var leadDataArrayList: ArrayList<LeadData> = ArrayList()
    private var tempDataArrayList: ArrayList<LeadData> = ArrayList()
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var requestLeadsViewModel: RequestsLeadsViewModel
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
                val intent = Intent(activity, ChargeableRequestDetailsActivity::class.java)

                intent.putExtra(Constants.LEAD_DATA, leadDataArrayList[position])
                //No need to send new lead data because closing complaint perform through adapter
                intent.putExtra(Constants.LEAD_TYPE, Constants.LEAD_NEW)
                startActivity(intent)
            }
        }
    }
    /*private val requestsLeadsAdapter = RequestsLeadsAdapter(
        leadDataArrayList, itemClickListener = onItemClickListener, Constants.LEAD_NEW
    )*/

    private val requestsLeadsAdapter = ChargeableRequestsLeadsAdapter(
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
        binding = ChargeableNewRequestAdminBinding.inflate(inflater, container, false)

        requestLeadsViewModel = ViewModelProvider(this)[RequestsLeadsViewModel::class.java]


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
        //binding.edtSearch.visibility = GONE
        binding.edtSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                /*if(!text.isNullOrEmpty()){
                    filter(text.toString())
                }else{
                    leadDataArrayList.clear()
                    tempDataArrayList.clear()
                    filter("")

                }*/
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
                    requestLeadsViewModel.loadPendingLeads(pageNumber)
                    isLoading = true
                }
            }
        }
    }

    private fun setObserver() {
        requestLeadsViewModel.loadChargeableLeads(pageNumber,0)
        requestLeadsViewModel.chargeableLeadsData.observe(viewLifecycleOwner, this::getChargeableLeads)
       // requestLeadsViewModel.cancelLeadsData.observe(viewLifecycleOwner, this::doCancelLead)
    }

    fun filter(text: String) {
        //loadSearchLead(text)
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


    private var nextPage: String? = null
    private fun getChargeableLeads(resources: Resource<RequestLeadResponse>) {
        when (resources.status) {
            Status.LOADING -> {
                binding.cpiLoading.visibility = View.VISIBLE
            }
            Status.ERROR -> {
                binding.cpiLoading.visibility = View.GONE
                isLoading = false
                activity?.let {

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
                        total = response.data.pagination.total
                        nextPage = it.data.pagination.next_page_url
                        Constants.CHARGEABLE_NEW_REQUEST = total.toString()

                        totalCount?.text = Constants.CHARGEABLE_NEW_REQUEST

                        leadDataArrayList.clear()
                        tempDataArrayList.clear()
                        leadDataArrayList.addAll(response.data.data)
                        tempDataArrayList.addAll(response.data.data)
                        requestsLeadsAdapter.notifyDataSetChanged()

                        isLoading = false
                        if (pageNumber == 1) {
                            requestsLeadsAdapter.notifyDataSetChanged()
                        }else {
                            requestsLeadsAdapter.notifyItemInserted(leadDataArrayList.size - 1)
                        }
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
        requestLeadsViewModel.searchPendingLeads(query)
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

    private fun confirmationDialog(context: Context, id: Int?) {

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
    }

    fun loadTotalLead(toolbarTotalLead: TextView) {
        totalCount = toolbarTotalLead

        totalCount?.text = Constants.CHARGEABLE_NEW_REQUEST.toString()
    }

    private fun loadUI () {
        binding.tvNoLeads.visibility = View.GONE
        binding.cpiLoading.visibility = View.VISIBLE
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            NewRequestsFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}