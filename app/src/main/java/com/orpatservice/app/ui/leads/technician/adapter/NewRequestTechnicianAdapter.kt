package com.orpatservice.app.ui.leads.technician.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

private const val NUM_TABS = 1
class NewRequestTechnicianAdapter
    constructor(val fragmentList: ArrayList<Fragment>, fragmentManager: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fragmentManager, lifecycle) {

        override fun getItemCount(): Int {
            return  NUM_TABS
        }

        override fun createFragment(position: Int): Fragment {
            return fragmentList[position]
        }

    }