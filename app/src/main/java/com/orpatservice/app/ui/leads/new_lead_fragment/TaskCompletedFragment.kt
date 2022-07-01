package com.orpatservice.app.ui.leads.new_lead_fragment

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.orpatservice.app.R
import com.orpatservice.app.data.Resource
import com.orpatservice.app.data.Status
import com.orpatservice.app.databinding.FragmentCompletedRequestBinding
import com.orpatservice.app.ui.leads.new_lead_fragment.adapter.TaskCompletedAdapter
import com.orpatservice.app.ui.leads.new_lead_fragment.new_lead_request.RequestData
import com.orpatservice.app.ui.leads.new_lead_fragment.new_lead_request.TakCompletedResponse
import com.orpatservice.app.ui.leads.new_lead_fragment.new_lead_request.TaskCompletedRequestData
import com.orpatservice.app.ui.leads.service_center.TaskCompletedDetailsActivity
import com.orpatservice.app.ui.leads.viewmodel.RequestsLeadsViewModel
import com.orpatservice.app.utils.Constants
import com.orpatservice.app.utils.Utils
import com.tapadoo.alerter.Alerter

class TaskCompletedFragment  : Fragment() {

    private lateinit var binding: FragmentCompletedRequestBinding
    private var leadDataArrayList: ArrayList<TaskCompletedRequestData> = ArrayList()
    private var tempDataArrayList: ArrayList<TaskCompletedRequestData> = ArrayList()
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var requestLeadsViewModel: RequestsLeadsViewModel
    private var isLoading: Boolean = false
    private var pageNumber = 1
    private var totalPage = 1

    private val onItemClickListener: (Int, View) -> Unit = { position, view ->
        when (view.id) {
            R.id.btn_view_details -> {
                when (view.id) {
                    R.id.btn_view_details -> {
                        val intent = Intent(activity, TaskCompletedDetailsActivity::class.java)

                        intent.putExtra(Constants.TASK_DATA, leadDataArrayList[position])
                        startActivity(intent)
                    }
                }
            }
        }
    }

    private val requestsLeadsAdapter = TaskCompletedAdapter(
        leadDataArrayList,
        itemClickListener = onItemClickListener,
        Constants.LEAD_COMPLETED_REQUEST
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
        binding = FragmentCompletedRequestBinding.inflate(inflater, container, false)

        layoutManager = LinearLayoutManager(activity)
        binding.rvAssignTechnician.layoutManager = layoutManager
        binding.rvAssignTechnician.apply {
            adapter = requestsLeadsAdapter
        }

        requestLeadsViewModel = ViewModelProvider(this)[RequestsLeadsViewModel::class.java]


        setObserver()
        loadUI()
        requestLeadsViewModel.loadTaskCompletedLeads(pageNumber)

        binding.rvAssignTechnician.addOnScrollListener(scrollListener)

        utilUIBind()

        return binding.root
    }

    private fun utilUIBind() {

            binding.edtTaskSearch.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(text: Editable?) {

                }

                override fun beforeTextChanged(char: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(charSeq: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if(!charSeq.isNullOrEmpty()){
                        //filter(charSeq.toString())
                    }else{
                        leadDataArrayList.clear()
                        tempDataArrayList.clear()
                       // filter("")

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
                  //  binding.cpiLoading.visibility = View.VISIBLE
                    requestLeadsViewModel.loadTaskCompletedLeads(pageNumber)
                    isLoading = true
                }
            }
        }
    }

    private fun setObserver() {
        requestLeadsViewModel.taskcompletedLeadsData.observe(viewLifecycleOwner, this::getCompletedLeads)
    }
    fun filter(text: String) {
        //loadSearchLead(text)
    }


    private fun getCompletedLeads(resources: Resource<TakCompletedResponse>) {
        when (resources.status) {
            Status.LOADING -> {
                binding.cpiLoading.visibility = View.VISIBLE
            }
            Status.ERROR -> {
                binding.cpiLoading.visibility = View.GONE
                isLoading = true
                activity?.let {
                    /*Alerter.create(it)
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

    private fun loadUI () {
        binding.tvNoLeads.visibility = View.GONE
        binding.cpiLoading.visibility = View.VISIBLE
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TaskCompletedFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }

    fun loadSearchLead(query: String) {
        leadDataArrayList.clear()
        tempDataArrayList.clear()
        requestLeadsViewModel.searchCompletedLeads(query)
        //requestLeadsViewModel.searchAssignedLeads(query)
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


}