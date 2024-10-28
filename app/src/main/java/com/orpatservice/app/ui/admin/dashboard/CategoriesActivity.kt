package com.orpatservice.app.ui.admin.dashboard

import android.content.Intent
import android.os.Bundle
import android.view.View.VISIBLE
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.Visibility
import com.orpatservice.app.data.model.VideoProductCategoriesData
import com.orpatservice.app.databinding.ActivityCategoriesBinding
import com.orpatservice.app.databinding.ActivityTechniciansBinding
import com.orpatservice.app.utils.Constants
import com.orpatservice.app.utils.Constants.KEY_CATEGORIES_LEVEL_1
import com.orpatservice.app.utils.Constants.KEY_CATEGORY_ID
import com.orpatservice.app.utils.Constants.KEY_SERVICE_TYPE_IDENTIFIER


class CategoriesActivity : AppCompatActivity(), CategoriesRVadapter.OnItemClickListener {

    private lateinit var binding: ActivityCategoriesBinding
    val productVideosViewModel :ProductVideosViewModel by viewModels()
    var categoriesList =  arrayListOf<VideoProductCategoriesData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityCategoriesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        binding.ivBack.visibility = VISIBLE
        binding.ivBack.setOnClickListener {

            this.onBackPressedDispatcher.onBackPressed()
        }
        val categoriesRVadapter = CategoriesRVadapter(categoriesList,this)
        binding.rvDataList.apply {
            adapter = categoriesRVadapter
            layoutManager = GridLayoutManager(this@CategoriesActivity,2)
            categoriesList
        }

        if(this.intent.getBooleanExtra(KEY_CATEGORIES_LEVEL_1,false)){
            productVideosViewModel.loadProductVideoCategories()
            binding.tvTitle.text = "Product Categories"
            productVideosViewModel.categoriesData.observe(this@CategoriesActivity){
                categoriesList.addAll(it.data.categories)
                categoriesRVadapter.notifyDataSetChanged()

            }

        }else{
            productVideosViewModel.loadProductVideoSubCategories(this.intent.getIntExtra(KEY_CATEGORY_ID,0))
            binding.tvTitle.text = "Product  Sub Categories"
            productVideosViewModel.subCategoriesData.observe(this@CategoriesActivity){
                categoriesList.addAll(it.data.sub_categories)
                categoriesRVadapter.notifyDataSetChanged()

            }

        }




        super.onCreate(savedInstanceState)
    }

    override fun onItemClick(item: VideoProductCategoriesData) {

        if(intent.getBooleanExtra(KEY_CATEGORIES_LEVEL_1,false)){
            val intent = Intent(this, CategoriesActivity::class.java)
            intent.putExtra(Constants.KEY_SERVICE_TYPE_IDENTIFIER, this.intent.getBooleanExtra(KEY_SERVICE_TYPE_IDENTIFIER,false))
            intent.putExtra(Constants.KEY_CATEGORIES_LEVEL_1, false)
            intent.putExtra(Constants.KEY_CATEGORY_ID,item.id)
            startActivity(intent)
        }else{
            val intent = Intent(this, ProductsVideoActivity::class.java)
            intent.putExtra(Constants.KEY_SERVICE_TYPE_IDENTIFIER, this.intent.getBooleanExtra(KEY_SERVICE_TYPE_IDENTIFIER,false))
            intent.putExtra(Constants.KEY_CATEGORY_ID, this.intent.getIntExtra(KEY_CATEGORY_ID,0))
            intent.putExtra(Constants.KEY_SUB_CAT_ID,item.id)
            startActivity(intent)
        }

    }

}