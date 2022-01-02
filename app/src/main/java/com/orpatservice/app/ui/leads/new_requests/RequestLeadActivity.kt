package com.orpatservice.app.ui.leads.new_requests

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.orpatservice.app.databinding.ActivityRequestsLeadsBinding
import com.orpatservice.app.ui.leads.new_requests.new_request_fragment.AssignToTechnicianFragment
import com.orpatservice.app.ui.leads.new_requests.new_request_fragment.NewRequestsFragment
import com.orpatservice.app.ui.leads.pager.ViewPagerAdapter
import com.orpatservice.app.utils.Constants

import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.appcompat.widget.SearchView
import com.orpatservice.app.R


class RequestLeadActivity : AppCompatActivity(), TabLayout.OnTabSelectedListener {

    lateinit var binding: ActivityRequestsLeadsBinding
    lateinit var viewModel: RequestsLeadsViewModel

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

        viewModel = ViewModelProvider(this)[RequestsLeadsViewModel::class.java]

        //View pager with tab layout
        val viewPager = binding.vpRequests
        val tabLayout = binding.tabLayout
        tabLayout.addOnTabSelectedListener(this)

        val fragmentArrayList: ArrayList<Fragment> = ArrayList()
        fragmentArrayList.add(NewRequestsFragment())
        fragmentArrayList.add(AssignToTechnicianFragment())

        val adapter = ViewPagerAdapter(fragmentArrayList, supportFragmentManager, lifecycle)
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_search, menu)

        val searchItem = menu.findItem(R.id.action_search)
        val searchView: SearchView = searchItem.actionView as SearchView
        searchView.imeOptions = EditorInfo.IME_ACTION_DONE
        searchView.queryHint = "Search leads"

        //This is where you find the edittext and set its background resource
        val searchPlate: View = searchView.findViewById(androidx.appcompat.R.id.search_src_text)
        //searchPlate.setBackgroundResource(R.drawable.rounded_search)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
        return true
    }
}