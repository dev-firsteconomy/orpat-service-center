package com.orpatservice.app.ui.login.technician

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.orpatservice.app.R
import com.orpatservice.app.databinding.FragmentTechnicianLoginBinding
import com.orpatservice.app.ui.login.LoginActivity
import com.orpatservice.app.utils.Utils

class TechnicianLoginFragment : Fragment(), View.OnClickListener {

    private lateinit var binding: FragmentTechnicianLoginBinding

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
        binding = FragmentTechnicianLoginBinding.inflate(inflater, container, false)

        binding.btnContinueMobile.setOnClickListener(this)

        return binding.root
    }

    private fun signUp() {
        if(Utils.instance.validatePhoneNumber(binding.edtMobile)) {

            showLoadingUI()
            val mobileNumber = binding.edtMobile.text.toString()
            (activity as LoginActivity).signUpTechnician(mobileNumber)
        }
    }

    fun showLoadingUI() {
        binding.btnContinueMobile.visibility = View.INVISIBLE
        binding.cpiLoading.visibility = View.VISIBLE
    }

    fun hideLoadingUI() {
        binding.btnContinueMobile.visibility = View.VISIBLE
        binding.cpiLoading.visibility = View.GONE
    }

    override fun onClick(v: View) {
        when(v.id) {
            R.id.btn_continue_mobile -> {
                signUp()
            }
        }
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TechnicianLoginFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}