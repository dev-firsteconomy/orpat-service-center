package com.orpatservice.app.ui.leads.history.history_request_fragment

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
import com.orpatservice.app.R
import com.orpatservice.app.databinding.FragmentCompletedRequestBinding
import com.orpatservice.app.ui.data.model.requests_leads.LeadData
import com.orpatservice.app.ui.leads.customer_detail.CustomerDetailsActivity
import com.orpatservice.app.ui.leads.new_requests.RequestsLeadsAdapter
import com.orpatservice.app.ui.leads.new_requests.RequestsLeadsViewModel
import com.orpatservice.app.utils.Constants

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CompletedRequestFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CompletedRequestFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding: FragmentCompletedRequestBinding
    private var leadDataArrayList: ArrayList<LeadData> = ArrayList()
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
                        val intent = Intent(activity, CustomerDetailsActivity::class.java)

                        intent.putExtra(Constants.LEAD_DATA, leadDataArrayList[position])
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
        Constants.LEAD_COMPLETED_REQUEST
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
        binding = FragmentCompletedRequestBinding.inflate(inflater, container, false)

        layoutManager = LinearLayoutManager(activity)
        binding.rvCompletedRequest.layoutManager = layoutManager
        binding.rvCompletedRequest.apply {
            adapter = requestsLeadsAdapter
        }

        requestLeadsViewModel = ViewModelProvider(this)[RequestsLeadsViewModel::class.java]

        setObserver()
        loadUI()
        tempData()

        binding.rvCompletedRequest.addOnScrollListener(scrollListener)

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

    }

    fun tempData() {
        for (index in 1..10) {
            val leadData = LeadData()
            leadData.address = "Prayagraj"
            leadData.name = "Ajay Yadav"
            leadData.pincode = "211001"
            leadData.status = "In-Progress"
            leadData.id = index + 99990
            leadData.created_at = "2021-12-14 14:32:16"
            leadDataArrayList.add(leadData)
        }
        requestsLeadsAdapter.notifyDataSetChanged()
    }

    private fun loadUI () {
        binding.tvNoLeads.visibility = View.GONE
        binding.cpiLoading.visibility = View.VISIBLE
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CompletedRequestFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CompletedRequestFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}