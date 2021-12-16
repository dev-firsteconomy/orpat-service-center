package com.orpatservice.app.ui.addtechnician

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.orpatservice.app.databinding.ActivityTechniciansBinding
import com.orpatservice.app.ui.data.Status
import com.orpatservice.app.ui.data.model.TechnicianData

class TechniciansActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTechniciansBinding
    private lateinit var viewModel : TechniciansViewModel

    private val techList: ArrayList<TechnicianData> = ArrayList()
    private var technicianAdapter = TechnicianAdapter(techList)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTechniciansBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        supportActionBar?.apply {
            title = "Technician"
            // show back button on toolbar
            // on back button press, it will navigate to parent activity
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        viewModel = ViewModelProvider(this)[TechniciansViewModel::class.java]

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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

}