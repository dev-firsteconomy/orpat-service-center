package com.orpatservice.app.ui.addtechnician

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.orpatservice.app.databinding.ActivityAddTechniciansBinding
import com.orpatservice.app.ui.data.Status
import com.orpatservice.app.ui.data.model.TechnicianData

class AddTechniciansActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddTechniciansBinding
    private lateinit var viewModel : AddTechniciansViewModel

    private val techList: ArrayList<TechnicianData> = ArrayList()
    private var technicianAdapter = TechnicianAdapter(techList)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTechniciansBinding.inflate(layoutInflater)

        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[AddTechniciansViewModel::class.java]

        binding.rvTechList.apply {
            adapter = technicianAdapter
        }

        setObserver()

    }

    private fun setObserver(){
        viewModel.loadTechnician().observe(this, Observer { resource ->
            when (resource?.status) {
                Status.LOADING -> {
                   binding.cpiLoading.visibility = View.VISIBLE

                }
                Status.ERROR -> {
                    binding.cpiLoading.visibility = View.GONE
                }
                else -> {
                    binding.cpiLoading.visibility = View.GONE
                    val data = resource?.data

                    data.let {
                        if (it?.success == true){
                            techList.addAll(it.data.data)
                            technicianAdapter.notifyDataSetChanged()
                        }
                    }.run {

                    }
                }
            }
        })
    }

}