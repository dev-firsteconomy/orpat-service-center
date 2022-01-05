package com.orpatservice.app.ui.leads.technician

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.orpatservice.app.R
import com.orpatservice.app.databinding.ActivityTechnicianRequestLeadBinding
import com.orpatservice.app.ui.leads.new_lead_fragment.NewRequestsFragment

class TechnicianRequestLeadActivity : AppCompatActivity() {

    private lateinit var binding : ActivityTechnicianRequestLeadBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTechnicianRequestLeadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // set toolbar as support action bar
        setSupportActionBar(binding.toolbar)

        supportActionBar?.apply {
            title = ""
            // show back button on toolbar
            // on back button press, it will navigate to parent activity
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        val newRequestFragment = NewRequestsFragment()
        loadFragment(newRequestFragment)
    }

    private fun loadFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fl_container, fragment)
        transaction.disallowAddToBackStack()
        transaction.commit()
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