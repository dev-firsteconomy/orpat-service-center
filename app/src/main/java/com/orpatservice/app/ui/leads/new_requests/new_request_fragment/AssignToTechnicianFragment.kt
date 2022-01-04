package com.orpatservice.app.ui.leads.new_requests.new_request_fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.orpatservice.app.R
import com.orpatservice.app.databinding.FragmentAssignToTechnicianBinding
import com.orpatservice.app.ui.data.Resource
import com.orpatservice.app.ui.data.Status
import com.orpatservice.app.ui.data.model.requests_leads.LeadData
import com.orpatservice.app.ui.data.model.requests_leads.RequestLeadResponse
import com.orpatservice.app.ui.leads.new_requests.RequestsLeadsAdapter
import com.orpatservice.app.ui.leads.new_requests.RequestsLeadsViewModel
import com.orpatservice.app.ui.leads.customer_detail.CustomerDetailsActivity
import com.orpatservice.app.utils.Constants
import com.tapadoo.alerter.Alerter

class AssignToTechnicianFragment : Fragment() {

    private lateinit var binding: FragmentAssignToTechnicianBinding
    private var leadDataArrayList: ArrayList<LeadData> = ArrayList()
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var requestLeadsViewModel: RequestsLeadsViewModel
    private var isLoading: Boolean = false
    private var pageNumber = 1
    private var totalPage = 1

    private val onItemClickListener: (Int, View) -> Unit = { position, view ->
        when (view.id) {
            R.id.btn_view_details -> {
                Intent(activity, CustomerDetailsActivity::class.java).apply {

                    putExtra(Constants.LEAD_DATA, leadDataArrayList[position])
                    startActivity(this)
                }
            }
        }
    }
    private val requestsLeadsAdapter = RequestsLeadsAdapter(
        leadDataArrayList,
        itemClickListener = onItemClickListener,
        Constants.LEAD_ASSIGN_TECHNICIAN
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
        binding = FragmentAssignToTechnicianBinding.inflate(inflater, container, false)

        layoutManager = LinearLayoutManager(activity)
        binding.rvAssignTechnician.layoutManager = layoutManager
        binding.rvAssignTechnician.apply {
            adapter = requestsLeadsAdapter
        }
        requestLeadsViewModel = ViewModelProvider(this)[RequestsLeadsViewModel::class.java]

        setObserver()
        loadUI()
        requestLeadsViewModel.loadAssignedLeads(pageNumber)

        binding.rvAssignTechnician.addOnScrollListener(scrollListener)
        setHasOptionsMenu(true)

        return binding.root
    }

    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            if (!isLoading) {
                if (layoutManager.findLastCompletelyVisibleItemPosition() == leadDataArrayList.size - 1 && totalPage > pageNumber) {
                    pageNumber++
                    binding.cpiLoading.visibility = View.VISIBLE
                    requestLeadsViewModel.loadAssignedLeads(pageNumber)
                    isLoading = true
                }
            }
        }
    }

    private fun setObserver() {
        requestLeadsViewModel.assignedLeadsData.observe(viewLifecycleOwner, this::getAssignedLeads)
    }

    private fun getAssignedLeads(resources: Resource<RequestLeadResponse>) {
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


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_search, menu)

        val searchItem = menu.findItem(R.id.action_search)
        val searchView: SearchView = searchItem.actionView as SearchView
        searchView.imeOptions = EditorInfo.IME_ACTION_DONE
        searchView.queryHint = "Search leads"

        //This is where you find the edittext and set its background resource
        val searchPlate: View = searchView.findViewById(androidx.appcompat.R.id.search_src_text)
        //searchPlate.setBackgroundResource(R.drawable.rounded_search)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                requestLeadsViewModel.searchAssignedLeads(query)
                leadDataArrayList.clear()
                requestsLeadsAdapter.notifyDataSetChanged()
                isLoading = true
                loadUI()
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
    }

    private fun loadUI () {
        binding.tvNoLeads.visibility = View.GONE
        binding.cpiLoading.visibility = View.VISIBLE
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AssignToTechnicianFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}