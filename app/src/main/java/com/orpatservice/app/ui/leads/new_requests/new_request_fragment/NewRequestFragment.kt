package com.orpatservice.app.ui.leads.new_requests.new_request_fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.orpatservice.app.R
import com.orpatservice.app.databinding.FragmentNewRequestBinding
import com.orpatservice.app.ui.data.Resource
import com.orpatservice.app.ui.data.Status
import com.orpatservice.app.ui.data.model.requests_leads.CancelLeadResponse
import com.orpatservice.app.ui.data.model.requests_leads.LeadData
import com.orpatservice.app.ui.data.model.requests_leads.RequestLeadResponse
import com.orpatservice.app.ui.leads.customer_detail.CustomerDetailsActivity
import com.orpatservice.app.ui.leads.new_requests.RequestsLeadsAdapter
import com.orpatservice.app.ui.leads.new_requests.RequestsLeadsViewModel
import com.orpatservice.app.utils.Constants
import com.tapadoo.alerter.Alerter

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [NewRequestsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NewRequestsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding: FragmentNewRequestBinding
    private var leadDataArrayList: ArrayList<LeadData> = ArrayList()
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var requestLeadsViewModel: RequestsLeadsViewModel
    private var removeIndex = -1
    private var isLoading: Boolean = false
    private var pageNumber = 1
    private var totalPage = 1

    //Click listener for List Item
    private val onItemClickListener: (Int, View) -> Unit = { position, view ->
        when (view.id) {
            R.id.btn_view_details -> {
                val intent = Intent(activity, CustomerDetailsActivity::class.java)

                intent.putExtra(Constants.LEAD_DATA, leadDataArrayList[position])
                intent.putExtra(Constants.LEAD_TYPE, Constants.LEAD_NEW)
                startActivity(intent)
            }
            R.id.btn_view_decline -> {
                removeIndex = position
                val id = leadDataArrayList[position].id

                confirmationDialog(activity as Context, id)
            }
        }
    }
    private val requestsLeadsAdapter = RequestsLeadsAdapter(
        leadDataArrayList, itemClickListener = onItemClickListener, Constants.LEAD_NEW
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentNewRequestBinding.inflate(inflater, container, false)

        layoutManager = LinearLayoutManager(activity)
        binding.rvNewRequest.layoutManager = layoutManager
        binding.rvNewRequest.apply {
            adapter = requestsLeadsAdapter
        }

        requestLeadsViewModel = ViewModelProvider(this)[RequestsLeadsViewModel::class.java]

        setObserver()
        binding.cpiLoading.visibility = View.VISIBLE
        requestLeadsViewModel.loadPendingLeads(pageNumber)

        binding.rvNewRequest.addOnScrollListener(scrollListener)

        return binding.root
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
        requestLeadsViewModel.pendingLeadsData.observe(viewLifecycleOwner, this::getPendingLeads)
        requestLeadsViewModel.cancelLeadsData.observe(viewLifecycleOwner, this::doCancelLead)
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
                    Alerter.create(it)
                        .setText(resources.error?.message.toString())
                        .setBackgroundColorRes(R.color.orange)
                        .setDuration(1500)
                        .show()
                }
            }
            else -> {
                binding.cpiLoading.visibility = View.GONE

                val response = resources.data

                response?.let {
                    if (it.success) {
                        totalPage = response.data.pagination.last_page
                        leadDataArrayList.addAll(response.data.data)
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
                    Alerter.create(it)
                        .setText(resources.error?.message.toString())
                        .setBackgroundColorRes(R.color.orange)
                        .setDuration(1500)
                        .show()
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
                            Alerter.create(it)
                                .setText(resources.data.message)
                                .setBackgroundColorRes(R.color.orange)
                                .setDuration(1500)
                                .show()
                        }
                    }
                }
            }
        }
    }

    private fun confirmationDialog(context: Context, id: Int?) {

        MaterialAlertDialogBuilder(
            context,
            R.style.MyThemeOverlay_MaterialComponents_MaterialAlertDialog)

            .setTitle("Reject Lead!")
            .setMessage("Are you sure, you want to reject this lead?")
            .setPositiveButton("Reject Lead") { dialog, which ->
                    if(id != null) {
                        requestLeadsViewModel.doCancelLead(id)
                        binding.cpiLoading.visibility = View.VISIBLE
                    } else {
                        Toast.makeText(activity, "Lead Id not found. Please try again!", Toast.LENGTH_SHORT).show()
                    }
            }
            .setNegativeButton(
                "Cancel"
            ) { _, i -> }
            .show()

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment NewRequestsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            NewRequestsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}