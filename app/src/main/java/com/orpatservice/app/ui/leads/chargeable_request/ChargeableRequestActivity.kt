package com.orpatservice.app.ui.leads.chargeable_request

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
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
import com.orpatservice.app.databinding.ActivityChargeableRequestLeadBinding
import com.orpatservice.app.ui.admin.dashboard.DashboardActivity
import com.orpatservice.app.ui.leads.service_center.response.OrderCountResponse
import com.orpatservice.app.ui.leads.viewmodel.RequestsLeadsViewModel
import com.orpatservice.app.utils.Constants

class ChargeableRequestActivity : AppCompatActivity() , TabLayout.OnTabSelectedListener {
    private lateinit var binding : ActivityChargeableRequestLeadBinding

    private val newRequestFragment = ChargeableNewRequest()
    private  val chargeableFinishedFragment = ChargeableFinishedFragment()
    private val chargeableCancelledFragment = ChargeableCancelledFragment()
    private lateinit var viewPager: ViewPager2
    lateinit var requestsLeadsViewModel: RequestsLeadsViewModel

    private var leadBadge : TextView? = null
    private var leadBadge2 : TextView? = null
    private var leadBadge3 : TextView? = null
    private var tabPos : Int? = null
    private var tabPos2 : Int? = null
    private var tabPos3 : Int? = null
    private var tab_model : String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChargeableRequestLeadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // set toolbar as support action bar
        setSupportActionBar(binding.toolbar)
        requestsLeadsViewModel = ViewModelProvider(this)[RequestsLeadsViewModel::class.java]

        tab_model = intent.getStringExtra(Constants.NEW_CHARGEABLE).toString()
        println("Constants.NEW)"+intent.getStringExtra(Constants.NEW_CHARGEABLE).toString())



        supportActionBar?.apply {
            title = ""
            // show back button on toolbar
            // on back button press, it will navigate to parent activity
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        setObserver()

        viewPager = binding.vpChargeableRequests
        val tabLayout = binding.tabLayout
        tabLayout.addOnTabSelectedListener(this)



        val fragmentArrayList: ArrayList<Fragment> = ArrayList()
        fragmentArrayList.add(newRequestFragment)
        fragmentArrayList.add(chargeableFinishedFragment)
        fragmentArrayList.add(chargeableCancelledFragment)



        val adapter = ChageableRequestTechnicianAdapter(fragmentArrayList, supportFragmentManager, lifecycle)
        viewPager.adapter = adapter

        //Tab name
          TabLayoutMediator(tabLayout, viewPager) { tab, position ->
              if(position == 0){
                  tabPos = position
                  tab.setCustomView(R.layout.custom_tab_badge)
                  leadBadge = tab.customView!!.findViewById(R.id.tv_count) as TextView
                  val tab_name = tab.customView!!.findViewById(R.id.tv_title) as TextView
                  tab_name.text = "New"


              }else if(position == 1){
                  tabPos2 = position
                  tab.setCustomView(R.layout.custom_tab_badge)
                  leadBadge2 = tab.customView!!.findViewById(R.id.tv_count) as TextView
                  val tab_name = tab.customView!!.findViewById(R.id.tv_title) as TextView
                  tab_name.text = "Completed Request"
                  //badge.text = "5"

              }else if(position == 2){

                  tabPos3 = position
                  tab.setCustomView(R.layout.custom_tab_badge)
                  leadBadge3 = tab.customView!!.findViewById(R.id.tv_count) as TextView
                  val tab_name = tab.customView!!.findViewById(R.id.tv_title) as TextView
                  tab_name.text = "Cancelled"

              }
             // tab.text = Constants.requestsTechnicianTabNameArray[position]
          }.attach()


        openTab(tab_model,tabLayout)
        //loadFragment(newRequestFragment)
    }

    private fun openTab(tabModel: String, tabLayout: TabLayout) {

        println("tabModel"+tabModel)
        if(tabModel.equals(Constants.CHARGEABLE_NEW)) {
            tabLayout.selectTab(tabLayout.getTabAt(0));
        }else if(tabModel.equals(Constants.CHARGEABLE_COMPLETED)) {
            tabLayout.selectTab(tabLayout.getTabAt(1));
        }else if(tabModel.equals(Constants.CHARGEABLE_CANCELLED)) {
            tabLayout.selectTab(tabLayout.getTabAt(2));
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
            ).observe(this@ChargeableRequestActivity, this::onOrderCountResponse)

        }catch (ex : Exception){
            ex.toString()
        }
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
              // super.onBackPressed()
                val intent = Intent(this, DashboardActivity::class.java)
                startActivity(intent)
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
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
                            leadBadge?.text = it.data.app_order_count?.chargeable_new_request_count.toString()
                        }
                        if(tabPos2 == 1){
                            leadBadge2?.text = it.data.app_order_count?.chargeable_completed_request_count.toString()
                        }
                        if(tabPos3 == 2){
                            leadBadge3?.text = it.data.app_order_count?.chargeable_cancelled_request_count.toString()
                        }

                    } else {

                    }
                }
            }
        }
    }



    private lateinit var searchView : SearchView
    override fun onCreateOptionsMenu(menu: Menu): Boolean {

        /*if (supportFragmentManager.fragments.get(viewPager.currentItem) is NewRequestsFragment) {
            binding.toolbarTotalLead.text = ""
            newRequestFragment.loadTotalLead(binding.toolbarTotalLead)
        }*//*else if (supportFragmentManager.fragments.get(viewPager.currentItem) is AssignedLeadFragment) {
            binding.toolbarTotalLead.text = ""
            chargeableFinishedFragment.loadTotalLead(binding.toolbarTotalLead)
        } else if (supportFragmentManager.fragments.get(viewPager.currentItem) is AssignToTechnicianFragment) {
            chargeableCancelledFragment.loadSearchLead(query)
        }*/
        return true
    }
        /*override fun onBackPressed() {
        *//*if (!searchView.isIconified) {
            searchView.onActionViewCollapsed()
            searchView.isIconified = true
            if(supportFragmentManager.fragments.get(viewPager.currentItem) is ChargeableNewRequest) {
                newRequestFragment.loadOldLeadData()
            } else if(supportFragmentManager.fragments.get(viewPager.currentItem) is AssignedLeadFragment){
                chargeableFinishedFragment.loadOldLeadData()
            }else if(supportFragmentManager.fragments.get(viewPager.currentItem) is AssignToTechnicianFragment){
                chargeableCancelledFragment.loadOldLeadData()
            }
            return
        }*//*
        super.onBackPressed()
    }
*/
    override fun onTabSelected(tab: TabLayout.Tab) {
        when (tab.position) {
            0 -> {
                newRequestFragment.loadTotalLead(leadBadge!!)
                tab.select()
            }
            1 -> {
                chargeableFinishedFragment.loadTotalLead(leadBadge2!!)
                tab.select()
            }
            2 -> {
                chargeableCancelledFragment.loadTotalLead(leadBadge3!!)
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