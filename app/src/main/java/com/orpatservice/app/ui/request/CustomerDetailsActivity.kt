package com.orpatservice.app.ui.request

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.orpatservice.app.R
import com.orpatservice.app.databinding.ActivityCustomerDetailsBinding
import com.orpatservice.app.databinding.ActivityTechniciansBinding

class CustomerDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCustomerDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCustomerDetailsBinding.inflate(layoutInflater)

    }
}