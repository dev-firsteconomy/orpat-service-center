package com.orpatservice.app.ui.leads.new_lead_fragment.adapter

import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

private const val NUM_TABS = 3
class NewRequestViewPagerAdapter
constructor(val fragmentList: ArrayList<Fragment>,totalCountLead:TextView, fragmentManager: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return  NUM_TABS
    }

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }

}