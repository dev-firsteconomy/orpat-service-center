package com.orpatservice.app.ui.leads.technician

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import com.orpatservice.app.R
import com.orpatservice.app.databinding.ActivityTechnicianRequestLeadBinding
import com.orpatservice.app.ui.leads.new_lead_fragment.NewRequestsFragment

class TechnicianRequestLeadActivity : AppCompatActivity() {

    private lateinit var binding : ActivityTechnicianRequestLeadBinding
    private val newRequestFragment = NewRequestsFragment()

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

    private lateinit var searchView : SearchView
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)

        layoutInflater.inflate(R.menu.menu_search, menu)

        val searchItem = menu.findItem(R.id.action_search)
        searchView = searchItem.actionView as SearchView
        searchView.imeOptions = EditorInfo.IME_ACTION_DONE
        searchView.queryHint = "Search leads"

        //This is where you find the edittext and set its background resource
        val searchPlate: View = searchView.findViewById(androidx.appcompat.R.id.search_src_text)
        //searchPlate.setBackgroundResource(R.drawable.rounded_search)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                requestLeadsViewModel.searchPendingLeads(query)
                leadDataArrayList.clear()
                requestsLeadsAdapter.notifyDataSetChanged()
                isLoading = true
                loadUI()
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
    }

    override fun onBackPressed() {
        if(newRequestFragment.closeSearchView()) {
            newRequestFragment.initialAPICall()
            return
        }
        super.onBackPressed()
    }
}