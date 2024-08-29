package com.orpatservice.app.ui.leads.technician

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.view.KeyEvent
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.*
import com.google.gson.JsonObject
import com.google.zxing.BarcodeFormat
import com.google.zxing.ResultPoint
import com.google.zxing.client.android.BeepManager
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import com.journeyapps.barcodescanner.DecoratedBarcodeView
import com.journeyapps.barcodescanner.DefaultDecoderFactory
import com.orpatservice.app.data.Resource
import com.orpatservice.app.data.Status
import com.orpatservice.app.databinding.ActivityBarscannerBinding
import com.orpatservice.app.ui.leads.customer_detail.CustomerDetailsModel
import com.orpatservice.app.ui.leads.customer_detail.UpdateRequestResponse
import com.orpatservice.app.utils.Utils
import org.json.JSONException
import java.util.*


class BarScannerActivity : AppCompatActivity() {

    companion object {
        private const val REQUEST_CAMERA_PERMISSION = 10
    }
    private lateinit var binding: ActivityBarscannerBinding
    private var barcodeView: DecoratedBarcodeView? = null
    private var beepManager: BeepManager? = null

    private var currentScannedProduct: String? = null
    private val formats: Collection<BarcodeFormat> = Arrays.asList(BarcodeFormat.QR_CODE, BarcodeFormat.CODE_39)
    private var leadId: String = ""
    lateinit var customerDetailsViewModel: CustomerDetailsModel

    private var latitude : String = ""
    private var longitude : String = ""
    private  final val permissionCode  = 42
    lateinit var mFusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBarscannerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        customerDetailsViewModel = ViewModelProvider(this)[CustomerDetailsModel::class.java]

        leadId = intent.getStringExtra("lead_id")!!
        barcodeView = binding.barcodeScanner



        binding.ivCancel.setOnClickListener {

            val data = Intent()
            data.putExtra("close", "close");

            setResult(2, data);
            finish()
        }

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        getLastLocation()

        setObserver()
        initScanner()

    }

    private fun setObserver() {

       // customerDetailsViewModel.qrCodeData.observe(this, this::onValidateQRCode)


    }

    @SuppressLint("MissingPermission")
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
                onBackPressed()
            }
        } else {
            requestPermissions()
        }
    }

    /*override fun onResume() {
        super.onResume()
        if (!checkPermissions()) {
            requestPermissions()
        } else {
            getLastLocation()
        }
    }
*/

    private fun checkPermissions(): Boolean {
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
            permissionCode
        )
    }


    @SuppressLint("MissingPermission")
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
    }

    private fun initScanner() {
        // Request camera permissions
        if (isCameraPermissionGranted()) {
            val formats: Collection<BarcodeFormat> = Arrays.asList(BarcodeFormat.QR_CODE, BarcodeFormat.CODE_39)

            barcodeView?.barcodeView?.decoderFactory = DefaultDecoderFactory(formats)
            barcodeView?.initializeFromIntent(intent)
            barcodeView?.decodeContinuous(callback)
            barcodeView?.barcodeView?.cameraSettings?.isAutoFocusEnabled = true
            beepManager = BeepManager(this)
            barcodeView?.getStatusView()?.setVisibility(View.GONE);

        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA),
                BarScannerActivity.REQUEST_CAMERA_PERMISSION
            )
        }
    }

    private fun isCameraPermissionGranted(): Boolean {
        val selfPermission = ContextCompat.checkSelfPermission(baseContext, Manifest.permission.CAMERA)
        return selfPermission == PackageManager.PERMISSION_GRANTED
    }

    private val callback: BarcodeCallback = object : BarcodeCallback {
        override fun barcodeResult(result: BarcodeResult) {
            if (result.text == null) { // Prevent duplicate scans
                return
            }

            var resultBarCode = result.text
            var currentScanner =
                resultBarCode.replace("http://", "").replace("http:// www.", "").replace("www.", "")

            barcodeView!!.setStatusText(currentScanner)
            //beepManager!!.playBeepSoundAndVibrate()

            println("Scanned Code: " + result.text)
            // writeScancodeEvent(result.text)

            currentScannedProduct = currentScanner

         //   bindingAdapter.ivQrCodeImage.visibility = View.VISIBLE
          //  bindingAdapter.barcodeScanner.visibility = View.INVISIBLE

            val r = Runnable {
                barcodeView?.resume()
            }
            Handler().postDelayed(r, 1000)


            barcodeView?.pause()

            Handler().postDelayed(r, 2000)

            println("currentScannercurrentScanner"+currentScanner)
            hitValidQRCode(currentScanner)
            //passProductAsResult(currentScanner)


        }
        override fun possibleResultPoints(resultPoints: List<ResultPoint>) {}
    }

    private fun hitValidQRCode(currentScanner: String) {

        val jsonObject = JsonObject()

        try {
            //val jsArray  = JsonArray()
            jsonObject.addProperty("scanned_barcode", currentScanner)
            jsonObject.addProperty("latitude", latitude)
            jsonObject.addProperty("longitude", longitude)

        } catch (e: JSONException) {
            e.printStackTrace()
        }

        println("jsonObject"+jsonObject)
        println("leadId"+leadId)
        customerDetailsViewModel.hitValidateQRApi(
            jsonObject,
            leadId
        ).observe(this, this::validateQRCode)
    }

    private fun validateQRCode(resources: Resource<UpdateRequestResponse>) {
        when (resources.status) {
            Status.LOADING -> {
                binding.cpiLoading.visibility = View.VISIBLE
            }
            Status.ERROR -> {
                binding.cpiLoading.visibility = View.GONE
            }
            else -> {
                binding.cpiLoading.visibility = View.GONE
                val response = resources.data

                response?.let {
                    if (it.success) {

                        Utils.instance.popupPinsUtil(this,
                            it.message,
                            "",
                            true)

                        currentScannedProduct?.let { it1 -> passProductAsResult(it1) }
                      //  bindingAdapter.tvQrcodeSuccessVerify.visibility = VISIBLE
                       // bindingAdapter.tvQrcodeSuccessVerify.text = response.message.toString()

                        /* bindingAdapter.ivQrCodeImage.visibility = VISIBLE
                          bindingAdapter.scannerView.visibility = GONE*/

                    } else {
                        // clearScannerAddMoreProducts()
                        it.message?.let { msg ->
                            Utils.instance.popupPinUtil(this,
                                msg,
                                null,
                                false)
                        }
                        val r = Runnable {
                            barcodeView?.resume()
                        }
                        Handler().postDelayed(r, 5000)

                    }
                }
            }
        }
    }

    private fun passProductAsResult(currentScanner: String) {
        val data = Intent()
        data.putExtra("currentScanner", currentScanner);

        setResult(1, data);
        finish()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == BarScannerActivity.REQUEST_CAMERA_PERMISSION) {
            if (isCameraPermissionGranted()) {
                barcodeView?.barcodeView?.decoderFactory = DefaultDecoderFactory(formats)
                barcodeView?.initializeFromIntent(intent)
                barcodeView?.decodeContinuous(callback)
                beepManager = BeepManager(this)
                barcodeView?.pause()

                val r = Runnable {
                    barcodeView?.resume()

                }
                Handler().postDelayed(r, 2000)
                //loadBottomSheetDialog()

            } else {
                Toast.makeText(this, "Camera permission is required.", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        barcodeView!!.resume()

    }

    override fun onPause() {
        super.onPause()
        barcodeView!!.pause()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return barcodeView!!.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event)
    }

}