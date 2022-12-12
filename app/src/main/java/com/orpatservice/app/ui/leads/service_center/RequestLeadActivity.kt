package com.orpatservice.app.ui.leads.service_center

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.orpatservice.app.R
import com.orpatservice.app.data.Resource
import com.orpatservice.app.data.Status
import com.orpatservice.app.databinding.ActivityRequestsLeadsBinding
import com.orpatservice.app.ui.admin.dashboard.DashboardActivity
import com.orpatservice.app.ui.leads.customer_detail.CustomerDetailsModel
import com.orpatservice.app.ui.leads.new_lead_fragment.*
import com.orpatservice.app.ui.leads.new_lead_fragment.adapter.NewRequestViewPagerAdapter
import com.orpatservice.app.ui.leads.service_center.response.OrderCountResponse
import com.orpatservice.app.ui.leads.technician.TechnicianUpdateRequestResponse
import com.orpatservice.app.ui.leads.viewmodel.RequestsLeadsViewModel
import com.orpatservice.app.utils.CommonUtils
import com.orpatservice.app.utils.Constants
import com.orpatservice.app.utils.Utils


class RequestLeadActivity : AppCompatActivity(), TabLayout.OnTabSelectedListener {

    private lateinit var binding: ActivityRequestsLeadsBinding
    private lateinit var viewPager: ViewPager2
    lateinit var requestsLeadsViewModel: RequestsLeadsViewModel
    private val newRequestFragment = NewRequestsFragment()
    private  val assignedLeadFragment = AssignedLeadFragment()
    private val assignToTechnicianFragment = AssignToTechnicianFragment()
    private var leadBadge : TextView? = null
    private var leadBadge2 : TextView? = null
    private var leadBadge3 : TextView? = null
    private var tabPos : Int? = null
    private var tabPos2 : Int? = null
    private var tabPos3 : Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRequestsLeadsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // set toolbar as support action bar
        setSupportActionBar(binding.toolbar)
        requestsLeadsViewModel = ViewModelProvider(this)[RequestsLeadsViewModel::class.java]


        supportActionBar?.apply {
            title = ""
            // show back button on toolbar
            // on back button press, it will navigate to parent activity
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        permissionCheck()

        setObserver()
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

            if(position == 0){
                tabPos = position
                tab.setCustomView(R.layout.custom_tab_badge)
                leadBadge = tab.customView!!.findViewById(R.id.tv_count) as TextView
                val tab_name = tab.customView!!.findViewById(R.id.tv_title) as TextView
                tab_name.text = "New"
               // leadBadge?.text =


                //tab.text = "New"
            }else if(position == 1){
               // tab.text = "Assign"
                tabPos2 = position
                tab.setCustomView(R.layout.custom_tab_badge)
                leadBadge2 = tab.customView!!.findViewById(R.id.tv_count) as TextView
                val tab_name = tab.customView!!.findViewById(R.id.tv_title) as TextView
                tab_name.text = "Assign"
                //badge.text = "5"


            }else if(position == 2){
              //  tab.text = "Verify"
                tabPos3 = position
                tab.setCustomView(R.layout.custom_tab_badge)
                leadBadge3 = tab.customView!!.findViewById(R.id.tv_count) as TextView
                val tab_name = tab.customView!!.findViewById(R.id.tv_title) as TextView
                tab_name.text = "Verify"
                //badge.text = "4"

            }
           // tab.text = Constants.requestsTabNameArray[position]
        }.attach()
        ////////////////////////////////////
      //  tabLayout.getTabAt(0)?.getOrCreateBadge()?.setNumber(10);


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

        val jsonObject = JsonObject()

        try {
            val arraylist = java.util.ArrayList<String>()
            for (i in Constants.orderCountArray) {
                    arraylist.add(i)
            }
            val jsArray = JsonArray()
            for (i in arraylist) {
                jsArray.add(i)
            }
            jsonObject.add("app_order_count", jsArray)
            requestsLeadsViewModel.hitOrderCountRequest(
                jsonObject,
            ).observe(this@RequestLeadActivity, this::onOrderCountResponse)

        }catch (ex : Exception){
            ex.toString()
        }
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
            newRequestFragment.loadTotalLead(leadBadge!!)
        }else if (supportFragmentManager.fragments.get(viewPager.currentItem) is AssignedLeadFragment) {
            binding.toolbarTotalLead.text = ""
            assignedLeadFragment.loadTotalLead(leadBadge2!!)
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


    private fun onOrderCountResponse(resources: Resource<OrderCountResponse>) {
        when (resources.status) {
            Status.LOADING -> {
                //binding.cpiLoading.visibility = View.VISIBLE
            }
            Status.ERROR -> {
                //binding.cpiLoading.visibility = View.GONE
            }
            else -> {
               // binding.cpiLoading.visibility = View.GONE
                val response = resources.data

                response?.let {
                    if (it.success) {

                        if(tabPos == 0){
                            leadBadge?.text = it.data.app_order_count?.new_request_count.toString()
                        }
                        if(tabPos2 == 1){
                            leadBadge2?.text = it.data.app_order_count?.assigned_request_count.toString()
                        }
                        if(tabPos3 == 2){
                            leadBadge3?.text = it.data.app_order_count?.verify_request_count.toString()
                        }

                    } else {

                    }
                }
            }
        }
    }


    override fun onResume() {
        super.onResume()
       // setObserver()
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
                assignedLeadFragment.loadTotalLead(leadBadge2!!)
                tab.select()
            }
            2 -> {
                assignToTechnicianFragment.loadTotalLead(leadBadge3!!)
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