package com.orpatservice.app.ui.leads.new_requests.new_request_fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.orpatservice.app.R
import com.orpatservice.app.databinding.FragmentAssignToTechnicianBinding
import com.orpatservice.app.ui.data.Resource
import com.orpatservice.app.ui.data.Status
import com.orpatservice.app.ui.data.model.requests_leads.LeadData
import com.orpatservice.app.ui.data.model.requests_leads.RequestLeadResponse
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
 * Use the [AssignToTechnicianFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AssignToTechnicianFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding: FragmentAssignToTechnicianBinding
    private var leadDataArrayList: ArrayList<LeadData> = ArrayList()
    private lateinit var requestLeadsViewModel: RequestsLeadsViewModel

    private val onItemClickListener: (Int, Int) -> Unit = { position, id ->
        when (id) {
            R.id.btn_view_details -> {
//                val intent = Intent(activity, CustomerDetailsActivity::class.java)
//
//                intent.putExtra(Constants.LEAD_DATA, leadDataArrayList[position])
//                startActivity(intent)
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
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentAssignToTechnicianBinding.inflate(inflater, container, false)

        binding.rvAssignTechnician.apply {
            adapter = requestsLeadsAdapter
        }
        requestLeadsViewModel = ViewModelProvider(this)[RequestsLeadsViewModel::class.java]

        setObserver()
        binding.cpiLoading.visibility = View.VISIBLE
        requestLeadsViewModel.loadAssignedLeads()

        return binding.root
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

                        leadDataArrayList.addAll(response.data.leadData)
                        requestsLeadsAdapter.notifyDataSetChanged()
                    }
                }
            }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AssignToTechnicianFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AssignToTechnicianFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}