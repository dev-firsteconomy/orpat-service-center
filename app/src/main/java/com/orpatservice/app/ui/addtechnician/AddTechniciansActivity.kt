package com.orpatservice.app.ui.addtechnician

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.orpatservice.app.databinding.ActivityAddTechniciansBinding
import com.orpatservice.app.ui.data.model.TechnicianData

class AddTechniciansActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddTechniciansBinding
    private val techList: ArrayList<TechnicianData> = ArrayList()
    private var technicianAdapter = TechnicianAdapter(techList)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTechniciansBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.rvTechList.apply {
            adapter = technicianAdapter
        }

    }

}