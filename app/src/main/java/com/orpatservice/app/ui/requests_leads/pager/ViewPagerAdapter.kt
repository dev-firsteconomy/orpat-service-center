package com.orpatservice.app.ui.requests_leads.pager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.orpatservice.app.ui.requests_leads.pager.requests_fragment.AssignToTechnicianFragment
import com.orpatservice.app.ui.requests_leads.pager.requests_fragment.NewRequestsFragment

/**
 * Created by Ajay Yadav on 22/12/21.
 */

private const val NUM_TABS = 2

class ViewPagerAdapter constructor(val fragmentList: ArrayList<Fragment>, fragmentManager: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return  NUM_TABS
    }

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }

}