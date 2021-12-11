package com.orpatservice.app.ui.addtechnician

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.orpatservice.app.R

class AddTechniciansFragment : Fragment() {

    companion object {
        fun newInstance() = AddTechniciansFragment()
    }

    private lateinit var viewModel: AddTechniciansViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.add_technicians_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AddTechniciansViewModel::class.java)
        // TODO: Use the ViewModel
    }

}