package com.orpatservice.app.ui.history.request_fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.orpatservice.app.R
import com.orpatservice.app.databinding.FragmentCompletedRequestBinding
import com.orpatservice.app.ui.data.model.requests_leads.LeadData
import com.orpatservice.app.ui.requests_leads.RequestsLeadsAdapter
import com.orpatservice.app.ui.requests_leads.customer.CustomerDetailsActivity
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

    private val onItemClickListener: (Int, Int) -> Unit = { position, id ->
        when (id) {
            R.id.btn_view_details -> {
                val intent = Intent(activity, CustomerDetailsActivity::class.java)

                intent.putExtra(Constants.LEAD_DATA, leadDataArrayList[position])
                startActivity(intent)
            }
        }
    }
    private val requestsLeadsAdapter = RequestsLeadsAdapter(
        leadDataArrayList,
        itemClickListener = onItemClickListener,
        Constants.COMPLETED_REQUEST
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
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCompletedRequestBinding.inflate(inflater, container, false)

        binding.rvCompletedRequest.apply {
            adapter = requestsLeadsAdapter
        }
        tempData()

        return binding.root
    }

    fun tempData() {
        for (index in 1..10) {
            val leadData = LeadData()
            leadData.address = "Prayagraj"
            leadData.name = "Ajay Yadav"
            leadData.pincode = "211001"
            leadData.status = "In-Progress"
            leadData.id = index + 99990
            leadData.createdAt = "02 Dec, 10:00 Hr"
            leadDataArrayList.add(leadData)
        }
        requestsLeadsAdapter.notifyDataSetChanged()
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