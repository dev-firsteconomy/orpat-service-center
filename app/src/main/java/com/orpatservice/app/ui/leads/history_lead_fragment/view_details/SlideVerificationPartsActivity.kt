package com.orpatservice.app.ui.leads.history_lead_fragment.view_details

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.orpatservice.app.data.model.requests_leads.LeadEnquiryImage
import com.orpatservice.app.databinding.ActivitySliderImageBinding
import com.orpatservice.app.ui.leads.customer_detail.adapter.SlideVerificationPartsAdater
import com.orpatservice.app.ui.leads.technician.adapter.ImageSliderAdapter
import com.orpatservice.app.utils.CommonUtils
import com.orpatservice.app.utils.Constants

class SlideVerificationPartsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySliderImageBinding
    //private lateinit var imageData: TechnicianImageData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySliderImageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val imageList = intent.getSerializableExtra(Constants.IMAGE_URL) as ArrayList<LeadEnquiryImage>
        // imageData = intent?.getParcelableExtra<TechnicianImageData>(Constants.IMAGE_DATA) as TechnicianImageData


        displayList(imageList)

        binding.imgCancel.setOnClickListener {
            onBackPressed()
            finish()
        }
    }

    private fun displayList(imageList: ArrayList<LeadEnquiryImage>) {
        val imgList = CommonUtils.imageList
        println("CommonUtils.imageList"+ imageList)
        binding.recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        binding.recyclerView.adapter = imageList.let { SlideVerificationPartsAdater(this, it) }
    }
}