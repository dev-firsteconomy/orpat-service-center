package com.orpatservice.app.ui.leads.service_center

import android.app.SearchManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.EditorInfo
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.orpatservice.app.R
import com.orpatservice.app.databinding.ActivityRequestsLeadsBinding
import com.orpatservice.app.ui.leads.new_lead_fragment.AssignToTechnicianFragment
import com.orpatservice.app.ui.leads.new_lead_fragment.NewRequestsFragment
import com.orpatservice.app.ui.leads.new_lead_fragment.TaskCompletedFragment
import com.orpatservice.app.ui.leads.new_lead_fragment.adapter.NewRequestViewPagerAdapter
import com.orpatservice.app.utils.Constants

class RequestLeadActivity : AppCompatActivity(), TabLayout.OnTabSelectedListener {

    private lateinit var binding: ActivityRequestsLeadsBinding
    private lateinit var viewPager: ViewPager2
    private val newRequestFragment = NewRequestsFragment()
    private val assignToTechnicianFragment = AssignToTechnicianFragment()
    private val taskCompletedFragment = TaskCompletedFragment()

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

        //View pager with tab layout
        viewPager = binding.vpRequests
        val tabLayout = binding.tabLayout
        tabLayout.addOnTabSelectedListener(this)

        val fragmentArrayList: ArrayList<Fragment> = ArrayList()
        fragmentArrayList.add(newRequestFragment)
        fragmentArrayList.add(assignToTechnicianFragment)
        fragmentArrayList.add(taskCompletedFragment)

        val adapter = NewRequestViewPagerAdapter(fragmentArrayList, supportFragmentManager, lifecycle)
        viewPager.adapter = adapter

        //Tab name
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = Constants.requestsTabNameArray[position]
        }.attach()
        ////////////////////////////////////

        setObserver()
    }

    private fun setObserver() {


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

    private lateinit var searchView : SearchView
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)

        // Associate searchable configuration with the SearchView
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView = menu.findItem(R.id.action_search).actionView as SearchView
        searchView.imeOptions = EditorInfo.IME_ACTION_DONE
        (searchView).apply {
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
        }
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                if(supportFragmentManager.fragments.get(viewPager.currentItem) is NewRequestsFragment) {
                    newRequestFragment.loadSearchLead(query)
                } else if(supportFragmentManager.fragments.get(viewPager.currentItem) is AssignToTechnicianFragment){
                    assignToTechnicianFragment.loadSearchLead(query)
                }else if(supportFragmentManager.fragments.get(viewPager.currentItem) is TaskCompletedFragment){
                   // assignToTechnicianFragment.loadSearchLead(query)
                }
                searchView.clearFocus()
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })

        return true
    }

    override fun onBackPressed() {
        if (!searchView.isIconified) {
            searchView.onActionViewCollapsed()
            searchView.isIconified = true
            if(supportFragmentManager.fragments.get(viewPager.currentItem) is NewRequestsFragment) {
                newRequestFragment.loadOldLeadData()
            } else if(supportFragmentManager.fragments.get(viewPager.currentItem) is AssignToTechnicianFragment){
                assignToTechnicianFragment.loadOldLeadData()
            }else if(supportFragmentManager.fragments.get(viewPager.currentItem) is TaskCompletedFragment){
               // taskCompletedFragment.loadOldLeadData()
            }
            return
        }
        super.onBackPressed()
    }

    /**
     * Tab selection logic
     */
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