package com.orpatservice.app.ui.leads.technician

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.orpatservice.app.databinding.ActivitySliderImageBinding
import com.orpatservice.app.ui.leads.technician.adapter.ImageSliderAdapter
import com.orpatservice.app.utils.CommonUtils
import com.orpatservice.app.utils.Constants


class SliderScreenImageActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySliderImageBinding
    //private lateinit var imageData: TechnicianImageData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySliderImageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val imageList = intent.getSerializableExtra(Constants.IMAGE_DATA) as ArrayList<*>?
        println("myListmyList"+imageList)
       // imageData = intent?.getParcelableExtra<TechnicianImageData>(Constants.IMAGE_DATA) as TechnicianImageData


        displayList(imageList)

        binding.imgCancel.setOnClickListener {
            onBackPressed()
            finish()
        }
    }

    private fun displayList(imageList: ArrayList<*>?) {
        val imgList = CommonUtils.imageList
        println("CommonUtils.imageList"+CommonUtils.imageList)
        binding.recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        binding.recyclerView.adapter = ImageSliderAdapter(imageList as ArrayList<String>)
    }
}