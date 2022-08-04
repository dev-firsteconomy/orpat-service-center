package com.orpatservice.app.ui.leads.new_lead_fragment.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MenuItem
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.orpatservice.app.R
import com.orpatservice.app.base.Callback
import com.orpatservice.app.data.Resource
import com.orpatservice.app.data.Status
import com.orpatservice.app.data.model.TechnicianResponse
import com.orpatservice.app.data.model.requests_leads.LeadData
import com.orpatservice.app.data.model.requests_leads.RequestLeadResponse
import com.orpatservice.app.databinding.ActivityAssignToTechnicianBinding
import com.orpatservice.app.databinding.ActivityChangeTechnicianBinding
import com.orpatservice.app.databinding.ActivityTechnicianTaskUpdateBinding
import com.orpatservice.app.ui.admin.technician.TechniciansViewModel
import com.orpatservice.app.ui.leads.adapter.AllTechnicianAdapter
import com.orpatservice.app.ui.leads.adapter.ComplaintAdapter
import com.orpatservice.app.ui.leads.new_lead_fragment.adapter.ChangeTechnicianAdapter
import com.orpatservice.app.ui.leads.new_lead_fragment.new_lead_request.NewRequestResponse
import com.orpatservice.app.ui.leads.new_lead_fragment.new_lead_request.RequestData
import com.orpatservice.app.ui.leads.service_center.RequestLeadActivity
import com.orpatservice.app.utils.Constants
import com.orpatservice.app.utils.DividerItemDecorator
import com.orpatservice.app.utils.Utils
import com.tapadoo.alerter.Alerter

class ChangeTechnicianActivity  : AppCompatActivity(), Callback {
    private lateinit var binding: ActivityChangeTechnicianBinding
    private var leadId = ""
    private var selectedTechnician = ""
    private var technician_id = ""
    private val techList: ArrayList<RequestData> = ArrayList()
    private lateinit var changetechnicianAdapter: ChangeTechnicianAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager
    lateinit var viewModel: TechniciansViewModel
    private var isLoading: Boolean = false
    private var pageNumber = 1
    private var totalPage = 1
    var clickedPosition: Int? = null
    private var selected_technicianId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangeTechnicianBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this)[TechniciansViewModel::class.java]
        setSupportActionBar(binding.toolbar)

        supportActionBar?.apply {
            title = ""
            // show back button on toolbar
            // on back button press, it will navigate to parent activity
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        leadId = intent.getStringExtra(Constants.LEADS_ID).toString()
        selectedTechnician = intent.getStringExtra(Constants.SELECTED_PARTS).toString()
        technician_id = intent.getStringExtra(Constants.TECHNICIAN_ID).toString()

        changetechnicianAdapter = ChangeTechnicianAdapter(techList,itemClickListener = onItemClickListener,leadId,selectedTechnician,technician_id)

        linearLayoutManager = LinearLayoutManager(this)

        val dividerItemDecoration: RecyclerView.ItemDecoration =
            DividerItemDecorator(ContextCompat.getDrawable(this, R.drawable.rv_divider))

        binding.rvAllTechList.apply {
            adapter = changetechnicianAdapter
           // addItemDecoration(dividerItemDecoration)
            layoutManager = linearLayoutManager

        }
        changetechnicianAdapter.callback = this

        userUtilData()
        setObserver()
        addScrollerListener()

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

    private fun userUtilData() {


        binding.btnChangeTechnician.setOnClickListener {

            if(selected_technicianId != null) {

                println("leadId, techList[clickedPosition!!].id.toString()" + leadId + selected_technicianId.toString())
                viewModel.hitAPIAssignChangeTechnicianLead(leadId, selected_technicianId.toString())
                    .observe(this, loadAssignTechnician())
            }else {

                Utils.instance.popupPinUtil(
                    this@ChangeTechnicianActivity,
                    "Please select technician",
                    "",
                    false
                )
            }
        }
    }

    private val onItemClickListener: (Int, View) -> Unit = { position, view ->
        when (view.id) {
            R.id.radio_technician -> {

                clickedPosition = position
                selected_technicianId = techList[position].id.toString()
            }
            R.id.li_technician_name -> {

                clickedPosition = position
                println("clickedPosition"+clickedPosition)
                selected_technicianId = techList[position].id.toString()
            }
        }
    }

    private fun addScrollerListener() {
        //attaches scrollListener with RecyclerView
        binding.rvAllTechList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!isLoading) {
                    if (linearLayoutManager.findLastCompletelyVisibleItemPosition() == techList.size - 1 && totalPage > pageNumber) {
                        pageNumber++
                        viewModel.loadAssignedTechnicianLeads(pageNumber)
                        isLoading = true
                    }
                }
            }
        })
    }
    private fun setObserver() {
        binding.cpiLoading.visibility = VISIBLE
     /*   viewModel.changeTechnicianLead(pageNumber)
        viewModel.changeTechnicianList.observe(this, this::getAssignedLeads)*/
        viewModel.technicianList.observe(this, this::getAssignedLeads)
        viewModel.loadTechnicianLeads(pageNumber,leadId.toInt())
     }

    override fun onItemClick(view: View, position: Int) {

    }

    private var nextPage: String? = null

    private fun getAssignedLeads(resources: Resource<NewRequestResponse>) {
        when (resources.status) {
            Status.LOADING -> {
                //    showLoadingUI()
                binding.cpiLoading.visibility = VISIBLE
            }
            Status.ERROR -> {
                binding.cpiLoading.visibility = GONE
                //  hideLoadingUI()
               /* Alerter.create(this)
                    .setText(resources.error?.message.toString())
                    .setBackgroundColorRes(R.color.orange)
                    .setDuration(1500)
                    .show()*/

                Utils.instance.popupPinUtil(this@ChangeTechnicianActivity,
                    resources.error?.message.toString(),
                    "",
                    false)
            }
            else -> {
                // hideLoadingUI()
                binding.cpiLoading.visibility = GONE
                val data = resources.data

                data.let {
                    if(it?.success == true){
                       // totalPage = it.data.pagination.last_page
                        techList.addAll(it.data.data)
                     //   nextPage = it.data.pagination.next_page_url

                        //technicianAdapter.notifyDataSetChanged()
                        isLoading = false

                        if (pageNumber == 1)
                            changetechnicianAdapter.notifyDataSetChanged()
                        else
                            changetechnicianAdapter.notifyItemInserted(techList.size - 1)

                    }else{
                        it?.message?.let { msg ->
                            // Utils.instance.popupUtil(this@CustomerDetailsActivity, msg, null, false)
                        }
                        val r = Runnable {
                            // barcodeView?.resume()
                        }
                        Handler().postDelayed(r, 1000)
                    }
                }.run {  }
            }
        }
    }

    private fun loadAssignTechnician(): Observer<Resource<TechnicianResponse>> {
        return Observer { it ->
            when (it?.status) {
                Status.LOADING -> {
                    // binding.cpiLoading.visibility = View.VISIBLE

                }
                Status.ERROR -> {
                    isLoading = false
                    // binding.cpiLoading.visibility = View.GONE

                    /*Alerter.create(this@ChangeTechnicianActivity)
                        .setTitle("")
                        .setText("" + it.error?.message.toString())
                        .setBackgroundColorRes(R.color.orange)
                        .setDuration(1000)
                        .show()*/
                    Utils.instance.popupPinUtil(this@ChangeTechnicianActivity,
                        it.error?.message.toString(),
                        "",
                        false)


                }
                else -> {
                    // binding.cpiLoading.visibility = View.GONE
                    val data = it?.data

                    data?.let {
                        if (it.success) {
                            //confirmationDialog(it.message)
                            /* Alerter.create(this@AllTechnicianActivity)
                                 .setTitle("")
                                 .setText(data.message.toString())
                                 .setBackgroundColorRes(R.color.orange)
                                 .setDuration(1000)
                                 .show()*/

                            it.message.toString()?.let { it1 ->
                                Utils.instance.popupUtil(
                                    this,
                                    it1,
                                    "",
                                    true
                                )
                            }
                            Handler(Looper.getMainLooper()).postDelayed({
                                val intent = Intent(this, RequestLeadActivity::class.java)
                                startActivity(intent)
                                finish()

                            }, 5000)
                        }
                    } ?: run {
                    }
                }
            }
        }
    }
}
