package com.orpatservice.app.ui.admin.dashboard

import android.os.Bundle
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.transition.Visibility
import com.orpatservice.app.databinding.ActivityCategoriesBinding
import com.orpatservice.app.databinding.ActivityTechniciansBinding


class CategoriesActivity : AppCompatActivity(){

    private lateinit var binding: ActivityCategoriesBinding




    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityCategoriesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        binding.tvTitle.text = "Product Categories"
        binding.ivBack.visibility = VISIBLE
        binding.ivBack.setOnClickListener {

            this.onBackPressedDispatcher.onBackPressed()
        }

        super.onCreate(savedInstanceState)
    }

}