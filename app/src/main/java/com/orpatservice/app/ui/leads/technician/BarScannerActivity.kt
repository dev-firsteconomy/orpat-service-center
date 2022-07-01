package com.orpatservice.app.ui.leads.technician

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.KeyEvent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBarscannerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        customerDetailsViewModel = ViewModelProvider(this)[CustomerDetailsModel::class.java]

        leadId = intent.getStringExtra("lead_id")!!
        barcodeView = binding.barcodeScanner



        binding.ivCancel.setOnClickListener { finish() }

        setObserver()
        initScanner()

    }

    private fun setObserver() {

       // customerDetailsViewModel.qrCodeData.observe(this, this::onValidateQRCode)


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
            //  val jsArray  = JsonArray()
            jsonObject.addProperty("scanned_barcode", currentScanner)

        } catch (e: JSONException) {
            e.printStackTrace()
        }

        customerDetailsViewModel.hitValidateQRApi(
            jsonObject,
            leadId
        ).observe(this, this::validateQRCode)
    }

    private fun validateQRCode(resources: Resource<UpdateRequestResponse>) {
        when (resources.status) {
            Status.LOADING -> {
               // binding.cpiLoading.visibility = View.VISIBLE
            }
            Status.ERROR -> {
               // binding.cpiLoading.visibility = View.GONE
            }
            else -> {
                //binding.cpiLoading.visibility = View.GONE
                val response = resources.data

                response?.let {
                    if (it.success) {
/*
                        Alerter.create(this@TechnicianCustomerDetailsActivity)
                            .setTitle("")
                            .setText("" + it.message?.toString())
                            .setBackgroundColorRes(R.color.orange)
                            .setDuration(1000)
                            .show()*/

                        Utils.instance.popupPinUtil(this,
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
                        Handler().postDelayed(r, 1000)

                    }
                }
            }
        }
    }
    private fun passProductAsResult(currentScanner: String) {
        val data = Intent()
        data.putExtra("currentScanner", currentScanner);

        setResult(Activity.RESULT_OK, data);
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