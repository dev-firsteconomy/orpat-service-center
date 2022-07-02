package com.orpatservice.app.ui.leads.chargeable_request

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.orpatservice.app.R
import com.orpatservice.app.databinding.ActivityChargeableRequestLeadBinding
import com.orpatservice.app.databinding.ActivityTechnicianRequestLeadBinding
import com.orpatservice.app.ui.admin.dashboard.DashboardActivity
import com.orpatservice.app.ui.leads.technician.TechnicianNewRequest
import com.orpatservice.app.ui.leads.technician.adapter.NewRequestTechnicianAdapter

class ChargeableRequestActivity : AppCompatActivity() , TabLayout.OnTabSelectedListener {
    private lateinit var binding : ActivityChargeableRequestLeadBinding

    private val newRequestFragment = ChargeableNewRequest()
    private lateinit var viewPager: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChargeableRequestLeadBinding.inflate(layoutInflater)
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

        viewPager = binding.vpChargeableRequests
        val tabLayout = binding.tabLayout
        tabLayout.addOnTabSelectedListener(this)

        val fragmentArrayList: ArrayList<Fragment> = ArrayList()
        fragmentArrayList.add(newRequestFragment)
        val adapter = ChageableRequestTechnicianAdapter(fragmentArrayList, supportFragmentManager, lifecycle)
        viewPager.adapter = adapter

        //Tab name
        /*  TabLayoutMediator(tabLayout, viewPager) { tab, position ->
              tab.text = Constants.requestsTechnicianTabNameArray[position]
          }.attach()
  */
        //loadFragment(newRequestFragment)
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
                //onBackPressed()
                val intent = Intent(this, DashboardActivity::class.java)
                startActivity(intent)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private lateinit var searchView : SearchView
    override fun onCreateOptionsMenu(menu: Menu): Boolean {


        return true
    }


    override fun onTabSelected(tab: TabLayout.Tab) {
        when (tab.position) {
            0 -> {
                tab.select()
            }
            else -> {
                tab.select()
            }
        }
    }

    override fun onTabUnselected(tab: TabLayout.Tab) {
    }

    override fun onTabReselected(tab: TabLayout.Tab) {
    }
}