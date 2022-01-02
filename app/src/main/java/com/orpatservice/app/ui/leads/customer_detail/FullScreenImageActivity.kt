package com.orpatservice.app.ui.leads.customer_detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.orpatservice.app.R
import com.orpatservice.app.databinding.ActivityFullScreenImageBinding
import com.orpatservice.app.utils.Constants

class FullScreenImageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFullScreenImageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFullScreenImageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ivCancel.setOnClickListener { finish() }

        Glide.with(this)
            .load(intent.getStringExtra(Constants.IMAGE_URL))
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            //.circleCrop() // .error(R.drawable.active_dot)
            .placeholder(R.color.gray)
            .into(binding.ivFullScreenImage)
    }
}