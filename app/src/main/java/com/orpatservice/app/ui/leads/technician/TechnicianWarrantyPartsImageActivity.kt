package com.orpatservice.app.ui.leads.technician

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.orpatservice.app.data.model.requests_leads.Enquiry
import com.orpatservice.app.databinding.ActivitySliderImageBinding
import com.orpatservice.app.ui.leads.technician.adapter.EnquiryImageSliderAdapter
import com.orpatservice.app.ui.leads.technician.adapter.TechnicianEnquiryImageSliderAdapter
import com.orpatservice.app.ui.leads.technician.response.TechnicianEnquiry
import com.orpatservice.app.utils.Constants

class TechnicianWarrantyPartsImageActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySliderImageBinding
    private lateinit var imageData: Enquiry

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySliderImageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        imageData =  intent?.getParcelableExtra<Enquiry>(Constants.IMAGE_DATA) as Enquiry


        displayList()

        binding.imgCancel.setOnClickListener {
            onBackPressed()
            finish()
        }
    }

    private fun displayList() {
        val imgList = imageData.lead_enquiry_images
        binding.recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        binding.recyclerView.adapter = TechnicianEnquiryImageSliderAdapter(imgList)
    }

}