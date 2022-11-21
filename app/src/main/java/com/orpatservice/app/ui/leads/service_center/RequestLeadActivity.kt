package com.orpatservice.app.ui.leads.service_center

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.orpatservice.app.databinding.ActivityRequestsLeadsBinding
import com.orpatservice.app.ui.admin.dashboard.DashboardActivity
import com.orpatservice.app.ui.leads.new_lead_fragment.*
import com.orpatservice.app.ui.leads.new_lead_fragment.adapter.NewRequestViewPagerAdapter
import com.orpatservice.app.utils.Constants

class RequestLeadActivity : AppCompatActivity(), TabLayout.OnTabSelectedListener {

    private lateinit var binding: ActivityRequestsLeadsBinding
    private lateinit var viewPager: ViewPager2
    private val newRequestFragment = NewRequestsFragment()
    private  val assignedLeadFragment = AssignedLeadFragment()
    private val assignToTechnicianFragment = AssignToTechnicianFragment()
   // private val taskCompletedFragment = TaskCompletedFragment()
    //private val otpVerification = OTPVerificationFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRequestsLeadsBinding.inflate(layoutInflater)
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

        permissionCheck()


        //View pager with tab layout
        viewPager = binding.vpRequests
        val tabLayout = binding.tabLayout
        tabLayout.addOnTabSelectedListener(this)

        val fragmentArrayList: ArrayList<Fragment> = ArrayList()
        fragmentArrayList.add(newRequestFragment)
        fragmentArrayList.add(assignedLeadFragment)
        fragmentArrayList.add(assignToTechnicianFragment)
        //fragmentArrayList.add(taskCompletedFragment)
      //  fragmentArrayList.add(otpVerification)

       val adapter = NewRequestViewPagerAdapter(fragmentArrayList,binding.toolbarTotalLead, supportFragmentManager, lifecycle)
        viewPager.adapter = adapter

        //Tab name
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = Constants.requestsTabNameArray[position]
        }.attach()
        ////////////////////////////////////

        setObserver()
    }

    private fun permissionCheck() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED || checkSelfPermission(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_DENIED) {
                val permission = arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                requestPermissions(permission, 112)
            }
        }
    }

    private fun setObserver() {

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
               // onBackPressed()
                val intent = Intent(this, DashboardActivity::class.java)
                startActivity(intent)
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private lateinit var searchView : SearchView
    override fun onCreateOptionsMenu(menu: Menu): Boolean {

        if (supportFragmentManager.fragments.get(viewPager.currentItem) is NewRequestsFragment) {
            binding.toolbarTotalLead.text = ""
            newRequestFragment.loadTotalLead(binding.toolbarTotalLead)
        }else if (supportFragmentManager.fragments.get(viewPager.currentItem) is AssignedLeadFragment) {
            binding.toolbarTotalLead.text = ""
            assignedLeadFragment.loadTotalLead(binding.toolbarTotalLead)
        } else if (supportFragmentManager.fragments.get(viewPager.currentItem) is AssignToTechnicianFragment) {
            //assignToTechnicianFragment.loadSearchLead(query)
        }

      /*  menuInflater.inflate(R.menu.menu_search, menu)

        // Associate searchable configuration with the SearchView
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView = menu.findItem(R.id.action_search).actionView as SearchView
        searchView.imeOptions = EditorInfo.IME_ACTION_DONE
        (searchView).apply {
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
        }
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                if(!query.isNullOrEmpty()) {
                    if (supportFragmentManager.fragments.get(viewPager.currentItem) is NewRequestsFragment) {
                        newRequestFragment.loadSearchLead(query)
                    } else if (supportFragmentManager.fragments.get(viewPager.currentItem) is AssignToTechnicianFragment) {
                        assignToTechnicianFragment.loadSearchLead(query)
                    } else if (supportFragmentManager.fragments.get(viewPager.currentItem) is TaskCompletedFragment) {
                        //taskCompletedFragment.loadSearchLead(query)
                    }
                }else{
                    if (supportFragmentManager.fragments.get(viewPager.currentItem) is NewRequestsFragment) {
                        newRequestFragment.loadSearchLead("")
                    } else if (supportFragmentManager.fragments.get(viewPager.currentItem) is AssignToTechnicianFragment) {
                        assignToTechnicianFragment.loadSearchLead("")
                    } else if (supportFragmentManager.fragments.get(viewPager.currentItem) is TaskCompletedFragment) {
                        //taskCompletedFragment.loadSearchLead(query)
                    }
                }
                searchView.clearFocus()
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })*/

        return true
    }

    override fun onResume() {
        super.onResume()
    }
  /*  override fun onBackPressed() {
        *//*if (!searchView.isIconified) {
            searchView.onActionViewCollapsed()
            searchView.isIconified = true
            if(supportFragmentManager.fragments.get(viewPager.currentItem) is NewRequestsFragment) {
                newRequestFragment.loadOldLeadData()
            } else if(supportFragmentManager.fragments.get(viewPager.currentItem) is AssignedLeadFragment){
                assignToTechnicianFragment.loadOldLeadData()
            }else if(supportFragmentManager.fragments.get(viewPager.currentItem) is AssignToTechnicianFragment){
                assignToTechnicianFragment.loadOldLeadData()
            }*//**//*else if(supportFragmentManager.fragments.get(viewPager.currentItem) is TaskCompletedFragment){
                taskCompletedFragment.loadOldLeadData()
            }*//**//**//**//*else if(supportFragmentManager.fragments.get(viewPager.currentItem) is OTPVerificationFragment){
                otpVerification.loadOldLeadData()
            }*//**//*
            return
        }*//*
        super.onBackPressed()
    }*/

    /**
     * Tab selection logic
     */
    override fun onTabSelected(tab: TabLayout.Tab) {

        when (tab.position) {

            0 -> {
                tab.select()
            }
            1 -> {
                assignedLeadFragment.loadTotalLead(binding.toolbarTotalLead)
                tab.select()
            }
            2 -> {
                assignToTechnicianFragment.loadTotalLead(binding.toolbarTotalLead)
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