package com.orpatservice.app.ui.dashboard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.orpatservice.app.R
import com.orpatservice.app.databinding.FragmentDashboardBinding

class DashboardFragment : Fragment(), View.OnClickListener {
    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)

        binding.mcvRequest.setOnClickListener(this)
        binding.mcvAddTechnician.setOnClickListener(this)
        binding.mcvHistory.setOnClickListener(this)
        binding.mcvProfile.setOnClickListener(this)
        binding.mcvPayment.setOnClickListener(this)
        binding.mcvMore.setOnClickListener(this)

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DashboardFragment().apply {
                arguments = Bundle().apply {
                    //putString(ARG_PARAM1, param1)
                }
            }
    }

    override fun onClick(view: View?) {
        val navController = findNavController()
        when (view?.id) {
            R.id.mcv_request -> {
                val action =
                    DashboardFragmentDirections.actionDashboardFragmentToAddTechniciansFragment()
                navController.navigate(action)
            }
            R.id.mcv_add_technician -> {
                val action =
                    DashboardFragmentDirections.actionDashboardFragmentToAddTechniciansFragment()
                navController.navigate(action)
            }
            R.id.mcv_history -> {
                val action =
                    DashboardFragmentDirections.actionDashboardFragmentToAddTechniciansFragment()
                navController.navigate(action)
            }
            R.id.mcv_profile -> {
                val action =
                    DashboardFragmentDirections.actionDashboardFragmentToAddTechniciansFragment()
                navController.navigate(action)
            }
            R.id.mcv_payment -> {
                val action =
                    DashboardFragmentDirections.actionDashboardFragmentToAddTechniciansFragment()
                navController.navigate(action)
            }
            R.id.mcv_more -> {
                val action =
                    DashboardFragmentDirections.actionDashboardFragmentToAddTechniciansFragment()
                navController.navigate(action)
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}