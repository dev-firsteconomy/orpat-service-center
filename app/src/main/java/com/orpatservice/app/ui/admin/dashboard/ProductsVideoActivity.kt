package com.orpatservice.app.ui.admin.dashboard

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.Visibility
import com.orpatservice.app.data.model.ProductVideoLinkData
import com.orpatservice.app.data.model.VideoProductCategoriesData
import com.orpatservice.app.databinding.ActivityCategoriesBinding
import com.orpatservice.app.databinding.ActivityTechniciansBinding
import com.orpatservice.app.utils.Constants
import com.orpatservice.app.utils.Constants.KEY_CATEGORIES_LEVEL_1
import com.orpatservice.app.utils.Constants.KEY_CATEGORY_ID
import com.orpatservice.app.utils.Constants.KEY_SERVICE_TYPE_IDENTIFIER
import com.orpatservice.app.utils.Constants.KEY_SUB_CAT_ID


class ProductsVideoActivity : AppCompatActivity(),
    ProductsListRVadapter.OnItemClickListener {

    private lateinit var binding: ActivityCategoriesBinding
    val productVideosViewModel :ProductVideosViewModel by viewModels()
    var categoriesList =  arrayListOf<ProductVideoLinkData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityCategoriesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        binding.etSearch.visibility = VISIBLE
        binding.ivBack.visibility = VISIBLE
        binding.ivBack.setOnClickListener {
            this.onBackPressedDispatcher.onBackPressed()
        }
        val categoriesRVadapter = ProductsListRVadapter(categoriesList,this)
        binding.rvDataList.apply {
            adapter = categoriesRVadapter
            layoutManager = GridLayoutManager(this@ProductsVideoActivity,2)
            categoriesList
        }

        binding.tvTitle.text = "Products"
        productVideosViewModel.loadProductVideos(this.intent.getIntExtra(KEY_CATEGORY_ID,0),this.intent.getIntExtra(
            KEY_SUB_CAT_ID,0),"")


        binding.etSearch.addTextChangedListener {
            productVideosViewModel.loadProductVideos(this.intent.getIntExtra(KEY_CATEGORY_ID,0),this.intent.getIntExtra(
                KEY_SUB_CAT_ID,0),it.toString())

        }

        productVideosViewModel.videoProductData.observe(this@ProductsVideoActivity){
            categoriesList.clear()
            categoriesList.addAll(it.data.products.data)
            categoriesRVadapter.notifyDataSetChanged()
            if(categoriesList.size==0){
                binding.tvNoDataFound.visibility = VISIBLE
            }else{
                binding.tvNoDataFound.visibility = GONE

            }
        }


        super.onCreate(savedInstanceState)
    }



    override fun onItemClick(item: ProductVideoLinkData) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setPackage("com.google.android.youtube")
        if (this.intent.getBooleanExtra(KEY_SERVICE_TYPE_IDENTIFIER,false)){
            intent.data = Uri.parse(item.products_details_service_link)
        }else{
            intent.data = Uri.parse(item.products_details_installation_link)
        }
        try {
            this@ProductsVideoActivity.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            // YouTube app is not installed, open in a web browser instead
            intent.setPackage(null)
            this@ProductsVideoActivity.startActivity(intent)
        }
    }

    }



