package com.orpatservice.app.ui.leads.service_center

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.orpatservice.app.R
import com.orpatservice.app.data.Resource
import com.orpatservice.app.data.Status
import com.orpatservice.app.databinding.ActivityLeadHistoryBinding
import com.orpatservice.app.ui.leads.history_lead_fragment.CancelledRequestFragment
import com.orpatservice.app.ui.leads.history_lead_fragment.CompletedRequestFragment
import com.orpatservice.app.ui.leads.new_lead_fragment.AssignedLeadFragment
import com.orpatservice.app.ui.leads.new_lead_fragment.NewRequestsFragment
import com.orpatservice.app.ui.leads.viewmodel.RequestsLeadsViewModel
import com.orpatservice.app.ui.leads.pager.ViewPagerAdapter
import com.orpatservice.app.ui.leads.service_center.response.OrderCountResponse
import com.orpatservice.app.utils.Constants

class LeadHistoryActivity : AppCompatActivity(), TabLayout.OnTabSelectedListener {

    lateinit var binding: ActivityLeadHistoryBinding
    lateinit var viewModel: RequestsLeadsViewModel
    private val completeRequestFragment = CompletedRequestFragment()
    private  val cancelledRequestFragment = CancelledRequestFragment()
    lateinit var requestsLeadsViewModel: RequestsLeadsViewModel
    private var leadBadge : TextView? = null
    private var leadBadge2 : TextView? = null
    private var leadBadge3 : TextView? = null
    private var tabPos : Int? = null
    private var tabPos2 : Int? = null
    private var tabPos3 : Int? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLeadHistoryBinding.inflate(layoutInflater)
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

        viewModel = ViewModelProvider(this)[RequestsLeadsViewModel::class.java]

        //View pager with tab layout
        val viewPager = binding.vpRequests
        val tabLayout = binding.tabLayout
        tabLayout.addOnTabSelectedListener(this)

        val fragmentArrayList: ArrayList<Fragment> = ArrayList()
        fragmentArrayList.add(completeRequestFragment)
        fragmentArrayList.add(cancelledRequestFragment)

        val adapter = ViewPagerAdapter(fragmentArrayList, supportFragmentManager, lifecycle)
        viewPager.adapter = adapter

        //Tab name
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            if(position == 0){
                tabPos = position
                tab.setCustomView(R.layout.custom_tab_badge)
                leadBadge = tab.customView!!.findViewById(R.id.tv_count) as TextView
                val tab_name = tab.customView!!.findViewById(R.id.tv_title) as TextView
                tab_name.text = "Completed Request"



                //tab.text = "New"
            }else if(position == 1){
                // tab.text = "Assign"
                tabPos2 = position
                tab.setCustomView(R.layout.custom_tab_badge)
                leadBadge2 = tab.customView!!.findViewById(R.id.tv_count) as TextView
                val tab_name = tab.customView!!.findViewById(R.id.tv_title) as TextView
                tab_name.text = "Cancelled Request"



            }

           // tab.text = Constants.requestsHistoryTabNameArray[position]
        }.attach()
        ////////////////////////////////////

        setObserver()
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
            ).observe(this@LeadHistoryActivity, this::onOrderCountResponse)

        }catch (ex : Exception){
            ex.toString()
        }

    }
    private fun onOrderCountResponse(resources: Resource<OrderCountResponse>) {
        when (resources.status) {
            Status.LOADING -> {
              //  binding.cpiLoading.visibility = View.VISIBLE
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
                            leadBadge?.text = it.data.app_order_count?.history_completed_request_count.toString()
                        }
                        if(tabPos2 == 1){
                            leadBadge2?.text = it.data.app_order_count?.history_cancelled_request_count.toString()
                        }

                    } else {

                    }
                }
            }
        }
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

    /**
     * Tab selection logic
     */
    override fun onTabSelected(tab: TabLayout.Tab) {
        when (tab.position) {
            0 -> {
                completeRequestFragment.loadTotalLead(leadBadge!!)
                tab.select()
            }
            1 -> {
                cancelledRequestFragment.loadTotalLead(leadBadge2!!)
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