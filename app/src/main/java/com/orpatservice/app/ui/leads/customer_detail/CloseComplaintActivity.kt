package com.orpatservice.app.ui.leads.customer_detail

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.orpatservice.app.R
import com.orpatservice.app.data.Resource
import com.orpatservice.app.data.Status
import com.orpatservice.app.data.model.RepairPartResponse
import com.orpatservice.app.data.model.RepairParts
import com.orpatservice.app.databinding.ActivityCloseComplaintBinding
import com.orpatservice.app.ui.admin.technician.TechniciansViewModel
import com.orpatservice.app.ui.leads.customer_detail.adapter.RepairPartAdapter
import com.orpatservice.app.ui.technician.HappyCodeActivity
import com.orpatservice.app.utils.Constants
import com.tapadoo.alerter.Alerter


class CloseComplaintActivity : AppCompatActivity(), AdapterView.OnItemClickListener {
    private lateinit var binding: ActivityCloseComplaintBinding
    private lateinit var viewModel: TechniciansViewModel

    private lateinit var partsArrayAdapter: ArrayAdapter<RepairParts>

    private lateinit var repairPartAdapter: RepairPartAdapter

    private val repairPartsList: ArrayList<RepairParts> = ArrayList()
    private val suggestPartsList: ArrayList<RepairParts> = ArrayList()

    private val onItemClickListener: (Int, View) -> Unit = { position, view ->
        when (view.id) {
            R.id.iv_cancel -> {
                repairPartsList.also {
                    it.removeAt(position)
                }
                repairPartAdapter.notifyItemRemoved(position)
            }
            R.id.btn_submit -> {
                Intent(this, HappyCodeActivity::class.java).apply {
                    putExtra(Constants.LEADS_ID,intent.getStringExtra(Constants.LEADS_ID))
                    putExtra(Constants.SELECTED_PARTS,"1")
                    startActivity(this)
                }
            }
        }


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCloseComplaintBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[TechniciansViewModel::class.java]

        binding.includedContent.btnSubmit.setOnClickListener {
            onItemClickListener(
                0,
                binding.includedContent.btnSubmit
            )
        }

        supportActionBar?.apply {
            title = ""
            // show back button on toolbar
            // on back button press, it will navigate to parent activity
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        repairPartAdapter = RepairPartAdapter(repairPartsList, onItemClickListener)

        binding.includedContent.rvRepairParts.apply {
            adapter = repairPartAdapter
        }

        binding.includedContent.mtvParts.threshold = 2
        binding.includedContent.mtvParts.onItemClickListener = this

        binding.includedContent.mtvParts.addTextChangedListener(textWatcher)


    }

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun afterTextChanged(text: Editable?) {
            if (binding.includedContent.mtvParts.isPerformingCompletion){

            }else{
                if (text.toString().isNotEmpty() && text.toString().trim().length > 2){
                    hitAPIPartsData(text.toString().trim())
                }

            }

        }

    }

    private fun hitAPIPartsData(partsText : String){
        viewModel.hitAPIParts(partsText).observe(this,loadPartsData())
    }
    private fun loadPartsData(): Observer<Resource<RepairPartResponse>> {
        return Observer { it ->
            when (it?.status) {
                Status.LOADING -> {
                    binding.cpiLoading.visibility = View.VISIBLE

                }
                Status.ERROR -> {
                    binding.cpiLoading.visibility = View.GONE

                    Alerter.create(this@CloseComplaintActivity)
                        .setTitle("")
                        .setText("" + it.error?.message.toString())
                        .setBackgroundColorRes(R.color.orange)
                        .setDuration(1000)
                        .show()

                }
                else -> {
                    binding.cpiLoading.visibility = View.GONE
                    val data = it?.data

                    data?.let {
                        if (it.success) {
                            suggestPartsList.clear()
                            suggestPartsList.addAll(it.data)

                            this.runOnUiThread{
                              val  partsArrayAdapters =
                                    ArrayAdapter(this, android.R.layout.simple_list_item_1, suggestPartsList)
                                binding.includedContent.mtvParts.setAdapter(partsArrayAdapters)
                                binding.includedContent.mtvParts.showDropDown()

                               // partsArrayAdapters.notifyDataSetChanged()
                            }

                        }
                    } ?: run {


                        Alerter.create(this@CloseComplaintActivity)
                            .setTitle("")
                            .setText("it.data?.message.toString()")
                            .setBackgroundColorRes(R.color.orange)
                            .setDuration(1000)
                            .show()
                    }
                }
            }
        }
    }

    override fun onItemClick(p0: AdapterView<*>?, view: View?, position: Int, id: Long) {
        repairPartsList.add(suggestPartsList[position])
        repairPartAdapter.notifyItemInserted(position)

        binding.includedContent.mtvParts.setText("")

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