package com.orpatservice.app.ui.dashboard

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.AppBarConfiguration
import com.orpatservice.app.R
import com.orpatservice.app.databinding.ActivityDashboardBinding
import com.orpatservice.app.ui.addtechnician.AddTechniciansActivity




class DashboardActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //setContentView(R.layout.activity_dashboard)

        setSupportActionBar(binding.toolbar)

        /*val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main) as NavHostFragment
        val navController = navHostFragment.navController
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)*/

        binding.includedContent.mcvRequest.setOnClickListener(this)
        binding.includedContent.mcvAddTechnician.setOnClickListener(this)
        binding.includedContent.mcvHistory.setOnClickListener(this)
        binding.includedContent.mcvProfile.setOnClickListener(this)
        binding.includedContent.mcvPayment.setOnClickListener(this)
        binding.includedContent.mcvMore.setOnClickListener(this)

    }

    override fun onClick(view: View?) {
        //val navController = findNavController()
        when (view?.id) {
            R.id.mcv_request -> {
                startActivity(Intent(this,AddTechniciansActivity::class.java))

            }
            R.id.mcv_add_technician -> {

            }
            R.id.mcv_history -> {

            }
            R.id.mcv_profile -> {

            }
            R.id.mcv_payment -> {

            }
            R.id.mcv_more -> {

            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        val item: MenuItem = menu.findItem(R.id.action_settings)
        item.isVisible = false
        super.onPrepareOptionsMenu(menu)
        return true
    }

    /*override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }*/
}