package com.orpatservice.app.ui.leads.chargeable_request

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

private const val NUM_TABS = 3
class ChageableRequestTechnicianAdapter
    constructor(val fragmentList: ArrayList<Fragment>, fragmentManager: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fragmentManager, lifecycle) {

        override fun getItemCount(): Int {
            return  NUM_TABS
        }

        override fun createFragment(position: Int): Fragment {
            return fragmentList[position]
        }

    }