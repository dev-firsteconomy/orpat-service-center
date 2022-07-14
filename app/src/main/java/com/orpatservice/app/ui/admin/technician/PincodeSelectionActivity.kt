package com.orpatservice.app.ui.admin.technician

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.orpatservice.app.R
import com.orpatservice.app.data.Resource
import com.orpatservice.app.data.Status
import com.orpatservice.app.databinding.ActivityPincodeSelectionBinding
import com.orpatservice.app.databinding.AdapterRequestPincodeBinding
import com.orpatservice.app.utils.CommonUtils
import com.orpatservice.app.utils.Utils

class PincodeSelectionActivity: AppCompatActivity() {
    private lateinit var binding: ActivityPincodeSelectionBinding
    private lateinit var techniciansViewModel: TechniciansViewModel
    private lateinit var layoutManager: LinearLayoutManager
    private var pincodeDataArrayList: ArrayList<PincodeData> = ArrayList()


    private val onItemClickListener: (Int, View, AdapterRequestPincodeBinding) -> Unit = { position, view,bindingAdapter ->
        when (view.id) {
            R.id.check_id -> {

                if(bindingAdapter.checkId.isChecked) {

                    val pincodeData = PinData(
                        pincodeDataArrayList[position].id.toString(),
                        pincodeDataArrayList[position].pincode.toString(),
                    )
                    CommonUtils.pincodeData.add(pincodeData)
                }else{

                }
            }
        }
    }

    private val requestsPincodeAdapter = RequestsPincodeAdapter(
        pincodeDataArrayList, itemClickListener = onItemClickListener
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPincodeSelectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        techniciansViewModel = ViewModelProvider(this)[TechniciansViewModel::class.java]


        binding.ivCancel.setOnClickListener { finish() }

        setObserver()
        utilBindData()
    }

    private fun setObserver() {
        techniciansViewModel.loadPincode()
        techniciansViewModel.pincodeData.observe(this, this::getPincode)

    }

    private fun utilBindData() {

        layoutManager = LinearLayoutManager(this)
        binding.rvPincodeList.layoutManager = layoutManager
        binding.rvPincodeList.apply {
            adapter = requestsPincodeAdapter
        }

        binding.btnOk.setOnClickListener {
            onBackPressed()
            finish()
        }
    }

    private fun getPincode(resources: Resource<RequestPincodeResponse>) {
        when (resources.status) {
            Status.LOADING -> {

            }
            Status.ERROR -> {

                    Utils.instance.popupPinUtil(this,
                        resources.error?.message.toString(),
                        "",
                        false)
            }
            else -> {
                val response = resources.data

                response?.let {
                    if (it.success) {
                        pincodeDataArrayList.clear()

                        pincodeDataArrayList.addAll(response.data.pincodes)
                        requestsPincodeAdapter.notifyDataSetChanged()
                    }
                }
            }
        }
    }
}