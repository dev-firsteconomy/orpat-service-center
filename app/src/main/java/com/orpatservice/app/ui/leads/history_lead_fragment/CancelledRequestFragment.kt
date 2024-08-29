package com.orpatservice.app.ui.leads.history_lead_fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.orpatservice.app.R
import com.orpatservice.app.databinding.FragmentCancelledRequestBinding
import com.orpatservice.app.data.Resource
import com.orpatservice.app.data.Status
import com.orpatservice.app.data.model.requests_leads.LeadData
import com.orpatservice.app.data.model.requests_leads.RequestLeadResponse
import com.orpatservice.app.ui.leads.customer_detail.CustomerDetailsActivity
import com.orpatservice.app.ui.leads.adapter.RequestsLeadsAdapter
import com.orpatservice.app.ui.leads.service_center.AssignDetailsActivity
import com.orpatservice.app.ui.leads.viewmodel.RequestsLeadsViewModel
import com.orpatservice.app.ui.login.LoginActivity
import com.orpatservice.app.utils.Constants
import com.tapadoo.alerter.Alerter

class CancelledRequestFragment : Fragment() {

    private lateinit var binding: FragmentCancelledRequestBinding
    private var leadDataArrayList: ArrayList<LeadData> = ArrayList()
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var requestLeadsViewModel: RequestsLeadsViewModel
    private var isLoading: Boolean = false
    private var pageNumber = 1
    private var totalPage = 1
    private var total = 0
    private  var totalCount :TextView? = null

    private val onItemClickListener: (Int, View) -> Unit = { position, view ->
        when (view.id) {
            R.id.btn_view_details -> {
                when (view.id) {
                    R.id.btn_view_details -> {
                        /*val intent = Intent(activity, CustomerDetailsActivity::class.java)

                        intent.putExtra(Constants.LEAD_DATA, leadDataArrayList[position])
                        startActivity(intent)*/
                        val intent = Intent(activity, AssignDetailsActivity::class.java)

                        intent.putExtra(Constants.LEAD_DATA, leadDataArrayList[position])
                        intent.putExtra(Constants.LEAD_TYPE, Constants.LEAD_NEW)
                        startActivity(intent)

                    }
                    R.id.btn_view_decline -> {
                        Toast.makeText(activity, "In-Progress", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
    private val requestsLeadsAdapter = RequestsLeadsAdapter(
        leadDataArrayList,
        itemClickListener = onItemClickListener,
        Constants.LEAD_CANCELLED_REQUEST
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
        binding = FragmentCancelledRequestBinding.inflate(inflater, container, false)

        layoutManager = LinearLayoutManager(activity)
        binding.rvCancelledRequest.layoutManager = layoutManager
        binding.rvCancelledRequest.apply {
            adapter = requestsLeadsAdapter
        }

        requestLeadsViewModel = ViewModelProvider(this)[RequestsLeadsViewModel::class.java]

        setObserver()
        loadUI()
        requestLeadsViewModel.loadCancelledLeads(pageNumber,"2")

        binding.rvCancelledRequest.addOnScrollListener(scrollListener)

        return binding.root
    }

    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            if (!isLoading) {
                if (layoutManager.findLastCompletelyVisibleItemPosition() == leadDataArrayList.size - 1 && totalPage > pageNumber) {
                    pageNumber++
                    binding.cpiLoading.visibility = View.VISIBLE
                    requestLeadsViewModel.loadCancelledLeads(pageNumber,"2")
                    isLoading = true
                }
            }
        }
    }

    private fun setObserver() {
        requestLeadsViewModel.cancelledLeadsData.observe(viewLifecycleOwner, this::getCancelledLeads)
    }

    private fun getCancelledLeads(resources: Resource<RequestLeadResponse>) {
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

                        total = response.data.pagination.total

                        Constants.CANCELLED_REQ = total.toString()

                        totalCount?.text = Constants.CANCELLED_REQ

                        leadDataArrayList.clear()
                        leadDataArrayList.addAll(response.data.data)
                        requestsLeadsAdapter.notifyDataSetChanged()

                        isLoading = false

                        if(leadDataArrayList.isNullOrEmpty()){
                            binding.tvNoLeads.visibility = View.VISIBLE
                        } else {
                            binding.tvNoLeads.visibility = View.GONE
                        }
                    }else{
                        if(it.code == 401){
                            val intent = Intent(requireActivity(), LoginActivity::class.java)
                            startActivity(intent)
                            requireActivity().finish()
                        }
                    }
                }
            }
        }
    }

    private fun loadUI () {
        binding.tvNoLeads.visibility = View.GONE
        binding.cpiLoading.visibility = View.VISIBLE
    }

    override fun onResume() {
        super.onResume()
        setObserver()

    }

    fun loadTotalLead(toolbarTotalLead: TextView) {
        totalCount = toolbarTotalLead
        totalCount?.text = Constants.CANCELLED_REQ.toString()
    }



    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CancelledRequestFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}