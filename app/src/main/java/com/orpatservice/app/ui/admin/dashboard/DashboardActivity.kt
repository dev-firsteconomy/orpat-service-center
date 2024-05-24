package com.orpatservice.app.ui.admin.dashboard

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.*
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.orpatservice.app.R
import com.orpatservice.app.data.Resource
import com.orpatservice.app.data.Status
import com.orpatservice.app.databinding.ActivityDashboardBinding
import com.orpatservice.app.ui.ProfileActivity
import com.orpatservice.app.ui.admin.technician.TechniciansActivity
import com.orpatservice.app.ui.leads.service_center.LeadHistoryActivity
import com.orpatservice.app.ui.leads.service_center.RequestLeadActivity
import com.orpatservice.app.utils.Constants
import com.orpatservice.app.data.sharedprefs.SharedPrefs
import com.orpatservice.app.ui.leads.chargeable_request.ChargeableRequestActivity
import com.orpatservice.app.ui.leads.technician.TechnicianHistoryLeadActivity
import com.orpatservice.app.ui.login.SelectUserActivity
import com.orpatservice.app.ui.leads.technician.TechnicianRequestLeadActivity
import com.orpatservice.app.ui.leads.viewmodel.RequestsLeadsViewModel
import com.orpatservice.app.ui.login.LoginActivity
import com.orpatservice.app.utils.Utils
import com.pushwoosh.Pushwoosh
import org.json.JSONException
import org.json.JSONObject


class DashboardActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityDashboardBinding
    private lateinit var requestLeadsViewModel: RequestsLeadsViewModel
    private lateinit var notificationCount: String

    //val PERMISSION_ID = 42
    //lateinit var mFusedLocationClient: FusedLocationProviderClient
    private var latitude : String = ""
    private var longitude : String = ""
    private var page_model : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        requestLeadsViewModel = ViewModelProvider(this)[RequestsLeadsViewModel::class.java]

        page_model = intent.getStringExtra(Constants.RECEIVE_DATA).toString()




       // mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

       // getLastLocation()

        binding.includedContent.mcvRequest.setOnClickListener(this)
        binding.includedContent.mcvAddTechnician.setOnClickListener(this)
        binding.includedContent.mcvHistory.setOnClickListener(this)
        binding.includedContent.mcvProfile.setOnClickListener(this)
        binding.includedContent.mcvPayment.setOnClickListener(this)
        binding.includedContent.mcvMore.setOnClickListener(this)
        binding.includedContent.mcvChargeableRequest.setOnClickListener(this)
        binding.ivLogout.setOnClickListener(this)

       // println("Pushwoosh.PUSH_RECEIVE_EVENT"+intent.getStringExtra(Pushwoosh.PUSH_RECEIVE_EVENT))



        updateUI()
        setObserver()


    }

    private fun openPage(pageModel: String) {
        println("pageModelpageModel"+pageModel)

        if(pageModel.equals(Constants.REQUEST_NEW)){
            val intent = Intent(this, RequestLeadActivity::class.java)
            intent.putExtra(Constants.NEW, pageModel)
            startActivity(intent)
        }else if(pageModel.equals(Constants.REQUEST_ASSIGN)){

            val intent = Intent(this, RequestLeadActivity::class.java)
            intent.putExtra(Constants.NEW, pageModel)
            startActivity(intent)

        }else if(pageModel.equals(Constants.REQUEST_VERIFY)){

            val intent = Intent(this, RequestLeadActivity::class.java)
            intent.putExtra(Constants.NEW, pageModel)
            startActivity(intent)

        }else if(pageModel.equals(Constants.CHARGEABLE_NEW)){

            val intent = Intent(this, ChargeableRequestActivity::class.java)
            intent.putExtra(Constants.NEW_CHARGEABLE, pageModel)
            startActivity(intent)

        }else if(pageModel.equals(Constants.CHARGEABLE_COMPLETED)){

            val intent = Intent(this, ChargeableRequestActivity::class.java)
            intent.putExtra(Constants.NEW_CHARGEABLE, pageModel)
            startActivity(intent)

        }else if(pageModel.equals(Constants.CHARGEABLE_CANCELLED)){

            val intent = Intent(this, ChargeableRequestActivity::class.java)
            intent.putExtra(Constants.NEW_CHARGEABLE, pageModel)
            startActivity(intent)

        }
        else if(pageModel.equals(Constants.TECHNICIAN_REQUEST_NEW)){

            val intent = Intent(this, TechnicianRequestLeadActivity::class.java)
            intent.putExtra(Constants.NEW_CHARGEABLE, pageModel)
            startActivity(intent)

        }
    }

    /*   @SuppressLint("MissingPermission")
       private fun getLastLocation() {
           if (checkPermissions()) {
               if (isLocationEnabled()) {

                   mFusedLocationClient.lastLocation.addOnCompleteListener(this) { task ->
                       var location: Location? = task.result
                       if (location == null) {
                           requestNewLocationData()
                       } else {
                           println("latitude"+location.latitude.toString())
                           println("longitude"+location.longitude.toString())
                           latitude = location.latitude.toString()
                           longitude = location.longitude.toString()
                       }
                   }
               } else {
                   //Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show()
                   val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                   startActivity(intent)
                   //  requireActivity().onBackPressed()
               }
           } else {
               requestPermissions()
           }
       }

   */
   /* private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            PERMISSION_ID
        )
    }*/


   /* override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            PERMISSION_ID -> if (grantResults.isNotEmpty() && grantResults[0] ==
                PackageManager.PERMISSION_GRANTED
            ) {
                getLastLocation()
            }
        }
    }*/

   /* @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        var mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 0
        mLocationRequest.fastestInterval = 0
        mLocationRequest.numUpdates = 1

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mFusedLocationClient!!.requestLocationUpdates(
            mLocationRequest, mLocationCallback,
            Looper.myLooper()
        )
    }
    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val mLastLocation: Location = locationResult.lastLocation
            latitude = mLastLocation.latitude.toString()
            longitude = mLastLocation.longitude.toString()
        }
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }*/


    private fun setObserver() {

        /*requestLeadsViewModel.synAppConfig.observe(this, this::getSynAppConfig)
        requestLeadsViewModel.loadSynAppConfig()
        requestLeadsViewModel.technicianSynAppConfig.observe(this, this::getTechnicianSynAppConfig)
        requestLeadsViewModel.loadTechnicianSynAppConfig()*/


    }

    private fun getSynAppConfig(resources: Resource<RequestSynAppResponse>) {
        when (resources.status) {
            Status.LOADING -> {

            }
            Status.ERROR -> {

                    Utils.instance.popupPinUtil(this,
                        resources.error?.message.toString(),
                        "",
                        false)
            }
            else -> {

                val response = resources.data

                response?.let {
                    if (it.success) {
                        notificationCount =
                            response.data.notification_badge_count.barcode_request_tab


                        openPage(page_model)

                        if (SharedPrefs.getInstance().getString(Constants.USER_TYPE, "")
                                .equals(Constants.SERVICE_CENTER)
                        ) {
                            binding.includedContent.tvCount.visibility = VISIBLE
                            binding.includedContent.tvCount.text =
                                response.data.notification_badge_count.barcode_request_tab
                            binding.includedContent.tvChargeableCount.text =
                                response.data.notification_badge_count.no_barcode_request_tab

                        } else if (SharedPrefs.getInstance().getString(Constants.USER_TYPE, "")
                                .equals(Constants.TECHNICIAN)) {
                            binding.includedContent.tvCount.visibility = VISIBLE
                            binding.includedContent.tvCount.text =
                                response.data.notification_badge_count.barcode_request_tab

                        }
                    }else{
                        if(it.code == 401){
                            val intent = Intent(this, SelectUserActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    }

                    /*}else{
                        Handler(Looper.getMainLooper()).postDelayed({
                            val intent = Intent(this, LoginActivity::class.java)
                            startActivity(intent)
                            finish()
                        }, 5000)
                    }*/
                }
            }
        }
    }

    private fun getTechnicianSynAppConfig(resources: Resource<RequestTechnicianSynAppResponse>) {
        when (resources.status) {
            Status.LOADING -> {

            }
            Status.ERROR -> {

                Utils.instance.popupPinUtil(this,
                    resources.error?.message.toString(),
                    "",
                    false)
            }
            else -> {

                val response = resources.data

                response?.let {
                    if (it.success) {
                        notificationCount = response.data.notification_badge_count.barcode_request_tab

                        openPage(page_model)


                        if (SharedPrefs.getInstance().getString(Constants.USER_TYPE, "").equals(Constants.SERVICE_CENTER)) {
                            binding.includedContent.tvCount.visibility = VISIBLE
                            binding.includedContent.tvCount.text = response.data.notification_badge_count.barcode_request_tab
                         //   binding.includedContent.tvChargeableCount.text = response.data.notification_badge_count.no_barcode_request_tab

                        } else if (SharedPrefs.getInstance().getString(Constants.USER_TYPE, "").equals(Constants.TECHNICIAN)) {
                            binding.includedContent.tvCount.visibility = VISIBLE
                            binding.includedContent.tvCount.text = response.data.notification_badge_count.barcode_request_tab

                        }
                    }else{
                        if(it.code == 401){
                            val intent = Intent(this, SelectUserActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()

       // getLastLocation()
        if (SharedPrefs.getInstance().getString(Constants.USER_TYPE, "").equals(Constants.SERVICE_CENTER)) {
            binding.tvTitle.setText("Admin")

            requestLeadsViewModel.synAppConfig.observe(this, this::getSynAppConfig)
            requestLeadsViewModel.loadSynAppConfig()

        } else if (SharedPrefs.getInstance().getString(Constants.USER_TYPE, "").equals(Constants.TECHNICIAN)) {
            binding.tvTitle.setText("Technician")

            requestLeadsViewModel.technicianSynAppConfig.observe(this, this::getTechnicianSynAppConfig)
            requestLeadsViewModel.loadTechnicianSynAppConfig()
        }
        if (SharedPrefs.getInstance().getString(Constants.USER_TYPE, "").equals(Constants.TECHNICIAN, ignoreCase = true)) {
            binding.includedContent.mcvAddTechnician.visibility = View.GONE
            binding.includedContent.mcvPayment.visibility = View.GONE
        }
    }

    private fun updateUI() {






        if (SharedPrefs.getInstance().getString(Constants.USER_TYPE, "").equals(Constants.SERVICE_CENTER)) {
            binding.tvTitle.setText("Admin")

            requestLeadsViewModel.synAppConfig.observe(this, this::getSynAppConfig)
            requestLeadsViewModel.loadSynAppConfig()


        } else if (SharedPrefs.getInstance().getString(Constants.USER_TYPE, "").equals(Constants.TECHNICIAN)) {
            binding.tvTitle.setText("Technician")

            requestLeadsViewModel.technicianSynAppConfig.observe(this, this::getTechnicianSynAppConfig)
            requestLeadsViewModel.loadTechnicianSynAppConfig()
        }
        if (SharedPrefs.getInstance().getString(Constants.USER_TYPE, "").equals(Constants.TECHNICIAN, ignoreCase = true)) {
            binding.includedContent.mcvAddTechnician.visibility = View.GONE
            binding.includedContent.mcvPayment.visibility = View.GONE
        }
    }



    private fun confirmationDialog() {

        MaterialAlertDialogBuilder(
            this,
            R.style.MyThemeOverlay_MaterialComponents_MaterialAlertDialog
        )
            .setTitle("Logout")
            .setMessage("Are you sure, you want to logout?")
            .setPositiveButton(
                "Logout"
            ) { _, i ->
                SharedPrefs.getInstance().removeAll()
                startActivity(Intent(this,SelectUserActivity::class.java))
                finish()
            }
            .setNegativeButton(
                "CANCEL"
            ) { _, i -> }
            .show()

    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.mcv_request -> {
                if (SharedPrefs.getInstance().getString(Constants.USER_TYPE, "").equals(Constants.SERVICE_CENTER)) {
                    val intent = Intent(this, RequestLeadActivity::class.java)
                    intent.putExtra(Constants.MODULE_TYPE, Constants.REQUEST)
                    startActivity(intent)
                } else if (SharedPrefs.getInstance().getString(Constants.USER_TYPE, "").equals(Constants.TECHNICIAN)) {
                    val intent = Intent(this, TechnicianRequestLeadActivity::class.java)
                    intent.putExtra(Constants.MODULE_TYPE, Constants.REQUEST)
                    startActivity(intent)
                }
            }
            R.id.mcv_add_technician -> {

                val intent = Intent(this, TechniciansActivity::class.java)
                intent.putExtra(Constants.IS_NAV, Constants.ComingFrom.DASHBOARD)
                startActivity(intent)

            }
            R.id.mcv_history -> {

                if (SharedPrefs.getInstance().getString(Constants.USER_TYPE, "").equals(Constants.SERVICE_CENTER)) {
                    val intent = Intent(this, LeadHistoryActivity::class.java)
                    intent.putExtra(Constants.MODULE_TYPE, Constants.HISTORY)
                    startActivity(intent)
                } else if (SharedPrefs.getInstance().getString(Constants.USER_TYPE, "").equals(Constants.TECHNICIAN)) {
                    val intent = Intent(this, TechnicianHistoryLeadActivity::class.java)
                    intent.putExtra(Constants.MODULE_TYPE, Constants.HISTORY)
                    startActivity(intent)
                }
            }

            R.id.mcv_profile -> {
                startActivity(Intent(this, ProfileActivity::class.java))
            }
            R.id.mcv_payment -> {
                val intent = Intent(this, ChargeableRequestActivity::class.java)
                intent.putExtra(Constants.MODULE_TYPE, Constants.REQUEST)
                startActivity(intent)

            }
            R.id.mcv_chargeable_request -> {

            }
            R.id.mcv_more -> {

            }
            R.id.iv_logout -> {
                confirmationDialog()
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    /*override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //do your stuff
            finish()

        }
        return super.onKeyDown(keyCode, event)
    }*/

}