package com.orpatservice.app.ui.admin.technician

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.orpatservice.app.R
import com.orpatservice.app.base.Callback
import com.orpatservice.app.databinding.ActivityTechniciansBinding
import com.orpatservice.app.ui.data.Resource
import com.orpatservice.app.ui.data.Status
import com.orpatservice.app.ui.data.model.TechnicianData
import com.orpatservice.app.ui.data.model.TechnicianResponse
import com.orpatservice.app.utils.Constants
import com.tapadoo.alerter.Alerter
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView.ItemDecoration

import com.orpatservice.app.utils.DividerItemDecorator




class TechniciansActivity : AppCompatActivity(), View.OnClickListener, Callback {
    private lateinit var binding: ActivityTechniciansBinding
    private lateinit var viewModel: TechniciansViewModel

    private val techList: ArrayList<TechnicianData> = ArrayList()
    private lateinit var technicianAdapter : TechnicianAdapter


    private var isLoading: Boolean = false
    private lateinit var linearLayoutManager: LinearLayoutManager
    private var pageNumber: Int = 1
    private var totalPage = 1
    private var isNave = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTechniciansBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.fabAddTechnician.setOnClickListener(this)
        binding.btnSubmitTechnician.setOnClickListener(this)

        setSupportActionBar(binding.toolbar)

        supportActionBar?.apply {
            title = ""
            // show back button on toolbar
            // on back button press, it will navigate to parent activity
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        viewModel = ViewModelProvider(this)[TechniciansViewModel::class.java]

        isNave = intent.getStringExtra(Constants.IS_NAV).toString()

        if (Constants.ComingFrom.CUSTOMER_DETAILS.equals(isNave,ignoreCase = true)){
            binding.fabAddTechnician.visibility = View.GONE
            binding.btnSubmitTechnician.visibility = View.VISIBLE

        }else{
            binding.btnSubmitTechnician.visibility = View.GONE
            binding.fabAddTechnician.visibility = View.VISIBLE

        }

        technicianAdapter = TechnicianAdapter(techList,isNave)


        linearLayoutManager = LinearLayoutManager(this)

        val dividerItemDecoration: ItemDecoration =
            DividerItemDecorator(ContextCompat.getDrawable(this, R.drawable.rv_divider))

        binding.rvTechList.apply {
            adapter = technicianAdapter
            addItemDecoration(dividerItemDecoration)
            layoutManager = linearLayoutManager

        }
        technicianAdapter.callback = this

        setObserver()

        addScrollerListener()

    }

    private fun addScrollerListener() {
        //attaches scrollListener with RecyclerView
        binding.rvTechList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!isLoading) {
                    if (linearLayoutManager.findLastCompletelyVisibleItemPosition() == techList.size - 1 && totalPage > pageNumber) {
                        pageNumber++
                        setObserver()
                        isLoading = true
                    }
                }
            }
        })
    }

    private fun setObserver() {
        viewModel.loadTechnician(pageNumber).observe(this, loadTechnician())
    }

    var nextPage: String? = null
    private fun loadTechnician(): Observer<Resource<TechnicianResponse>> {
        return Observer { it ->
            when (it?.status) {
                Status.LOADING -> {
                    binding.cpiLoading.visibility = View.VISIBLE

                }
                Status.ERROR -> {
                    isLoading = false
                    binding.cpiLoading.visibility = View.GONE
                    Alerter.create(this@TechniciansActivity)
                        .setTitle("")
                        .setText("" + it.error?.message.toString())
                        .setBackgroundColorRes(R.color.orange)
                        .setDuration(1000)
                        .show()
                }
                else -> {
                    binding.cpiLoading.visibility = View.GONE
                    val data = it?.data

                    data?.let {
                        if (it.success) {
                            totalPage = it.data.pagination.last_page
                            techList.addAll(it.data.data)
                            nextPage = it.data.pagination.next_page_url

                            //technicianAdapter.notifyDataSetChanged()
                            isLoading = false

                             if (pageNumber==1)
                                 technicianAdapter.notifyDataSetChanged()
                             else
                                 technicianAdapter.notifyItemInserted(techList.size-1)

                        }
                    } ?: run {
                        Alerter.create(this@TechniciansActivity)
                            .setTitle("")
                            .setText(it.data?.message.toString())
                            .setBackgroundColorRes(R.color.orange)
                            .setDuration(1000)
                            .show()
                    }
                }
            }
        }
    }

    private fun openCallDialPad(contactNumber: String) {
        val i = Intent(Intent.ACTION_DIAL)
        val p = "tel:$contactNumber"
        i.data = Uri.parse(p)
        startActivity(i)
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

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.fab_add_technician -> {
                val intent = Intent(this, AddTechnicianActivity::class.java)
                intent.putExtra(ADD, ADD)
                addTechnicianLauncher.launch(intent)

            }
        }
    }

    var clickedPosition: Int? = null
    override fun onItemClick(view: View, position: Int) {
        when (view.id) {
            R.id.tv_edit -> {
                clickedPosition = position

                val intent = Intent(this, AddTechnicianActivity::class.java)
                intent.putExtra(UPDATE, UPDATE)
                intent.putExtra(PARCELABLE_TECHNICIAN, techList[position])

                addTechnicianLauncher.launch(intent)
            }
            R.id.btn_submit_technician ->{

            }
            R.id.iv_call -> {
                openCallDialPad(techList[position].mobile)

            }
        }

    }

    private var addTechnicianLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                //setObserver()
                val technicianData =
                    result.data?.getParcelableExtra<TechnicianData>(PARCELABLE_TECHNICIAN)
                if (clickedPosition != null) {
                    techList[clickedPosition!!] = technicianData!!
                    technicianAdapter.notifyItemChanged(clickedPosition!!)

                } else {

                    binding.rvTechList.post(Runnable {
                        techList.add(0, technicianData!!)
                        technicianAdapter.notifyItemInserted(0)
                        binding.rvTechList.scrollToPosition(0)

                    })


                }
            }
        }
} 
