package com.orpatservice.app.ui.admin.technician

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.orpatservice.app.R
import com.orpatservice.app.base.Callback
import com.orpatservice.app.databinding.ActivityTechniciansBinding
import com.orpatservice.app.ui.data.Resource
import com.orpatservice.app.ui.data.Status
import com.orpatservice.app.ui.data.model.TechnicianData
import com.orpatservice.app.ui.data.model.TechnicianResponse
import com.tapadoo.alerter.Alerter

class TechniciansActivity : AppCompatActivity(), View.OnClickListener, Callback {
    private lateinit var binding: ActivityTechniciansBinding
    private lateinit var viewModel: TechniciansViewModel

    private val techList: ArrayList<TechnicianData> = ArrayList()
    private var technicianAdapter = TechnicianAdapter(techList)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTechniciansBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.fabAddTechnician.setOnClickListener(this)

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
        technicianAdapter.callback = this

        setObserver()

    }

    private fun setObserver() {
        viewModel.loadTechnician().observe(this, loadTechnician())
    }

    private fun loadTechnician(): Observer<Resource<TechnicianResponse>> {
        return Observer { it ->
            when (it?.status) {
                Status.LOADING -> {
                    binding.cpiLoading.visibility = View.VISIBLE

                }
                Status.ERROR -> {
                    binding.cpiLoading.visibility = View.GONE
                    Alerter.create(this@TechniciansActivity)
                        .setTitle("")
                        .setText(""+it.error?.message.toString())
                        .setBackgroundColorRes(R.color.orange)
                        .setDuration(1000)
                        .show()
                }
                else -> {
                    binding.cpiLoading.visibility = View.GONE
                    val data = it?.data

                    data?.let {
                        if (it.success) {
                            techList.addAll(it.data.data)
                            technicianAdapter.notifyDataSetChanged()
                        }
                    }?: run {
                        Alerter.create(this@TechniciansActivity)
                            .setTitle("")
                            .setText(it.data?.message.toString())
                            .setBackgroundColorRes(R.color.orange)
                            .setDuration(1000)
                            .show()
                    }
                }
            }
        }
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

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.fab_add_technician -> {
                val intent = Intent(this, AddTechnicianActivity::class.java)
                intent.putExtra(ADD, ADD)

                startActivity(intent)

            }
        }
    }

    override fun onItemClick(view: View, position: Int) {
        val intent = Intent(this, AddTechnicianActivity::class.java)
        intent.putExtra(UPDATE, UPDATE)
        intent.putExtra(PARCELABLE_TECHNICIAN,techList[position])

        startActivity(intent)
    }

}