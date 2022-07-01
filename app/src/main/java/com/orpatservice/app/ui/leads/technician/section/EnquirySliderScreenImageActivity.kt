package com.orpatservice.app.ui.leads.technician.section

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.orpatservice.app.databinding.ActivitySliderImageBinding
import com.orpatservice.app.ui.leads.technician.adapter.EnquiryImageSliderAdapter
import com.orpatservice.app.ui.leads.technician.response.TechnicianEnquiry
import com.orpatservice.app.utils.Constants

class EnquirySliderScreenImageActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySliderImageBinding
    private lateinit var imageData: TechnicianEnquiry

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySliderImageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        imageData =  intent?.getParcelableExtra<TechnicianEnquiry>(Constants.IMAGE_DATA) as TechnicianEnquiry


        displayList()

        binding.imgCancel.setOnClickListener {
            onBackPressed()
            finish()
        }
    }

    private fun displayList() {
        val imgList = imageData.lead_enquiry_images
        binding.recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        binding.recyclerView.adapter = EnquiryImageSliderAdapter(imgList)
    }

}