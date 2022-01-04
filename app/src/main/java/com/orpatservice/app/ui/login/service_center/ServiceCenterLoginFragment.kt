package com.orpatservice.app.ui.login.service_center

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.orpatservice.app.R
import com.orpatservice.app.databinding.FragmentServiceCenterLoginBinding
import com.orpatservice.app.ui.login.LoginActivity
import com.orpatservice.app.utils.Utils

class ServiceCenterLoginFragment : Fragment(), View.OnClickListener {

    private lateinit var binding: FragmentServiceCenterLoginBinding

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
        binding = FragmentServiceCenterLoginBinding.inflate(inflater, container, false)

        binding.btnContinueLogin.setOnClickListener(this)

        return binding.root
    }

    private fun signUp() {
        if(Utils.instance.validateEmail(binding.edtEmailId) &&
                Utils.instance.validatePassword(binding.edtPassword)) {

            showLoadingUI()
            val email = binding.edtEmailId.text.toString()
            val password = binding.edtPassword.text.toString()
            (activity as LoginActivity).signUpServiceCenter(email, password)
        }
    }

    fun showLoadingUI() {
        binding.btnContinueLogin.visibility = View.INVISIBLE
        binding.cpiLoading.visibility = View.VISIBLE
    }

    fun hideLoadingUI() {
        binding.btnContinueLogin.visibility = View.VISIBLE
        binding.cpiLoading.visibility = View.GONE
    }

    override fun onClick(v: View) {
        when(v.id) {
            R.id.btn_continue_login -> {
                signUp()
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ServiceCenterLoginFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}