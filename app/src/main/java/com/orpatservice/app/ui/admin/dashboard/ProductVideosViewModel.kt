package com.orpatservice.app.ui.admin.dashboard

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.orpatservice.app.data.Resource
import com.orpatservice.app.data.model.BaseCategoryVideoResponse
import com.orpatservice.app.data.model.BaseProductsVideoResponse
import com.orpatservice.app.data.model.BaseSubCategoryVideoResponse
import com.orpatservice.app.data.model.ProductVideoLinkDataResponse
import com.orpatservice.app.data.model.VideoProductCategoriesData
import com.orpatservice.app.data.remote.ErrorUtils
import com.orpatservice.app.data.repository.DataRepository
import com.orpatservice.app.ui.leads.technician.response.TechnicianRequestLeadResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ProductVideosViewModel :ViewModel(){

    val categoriesData = MutableLiveData<BaseCategoryVideoResponse>()
    val subCategoriesData = MutableLiveData<BaseSubCategoryVideoResponse>()
    val videoProductData = MutableLiveData<BaseProductsVideoResponse>()

    fun loadProductVideoCategories() {
        DataRepository.instance.hitGetInstallationVideosCategories().enqueue(callbackTechnicianPendingLeads)

    }


    fun loadProductVideoSubCategories(id: Int) {
        DataRepository.instance.hitGetInstallationVideosSubCategories(id).enqueue(callBackVideoSubCategories)

    }

    fun loadProductVideos(catId: Int,id: Int,searchQuery :String) {
        DataRepository.instance.hitGetInstallationVideosProducts(catId,id, searchQuery).enqueue(callbackVideoProducts)
    }

    private val callbackTechnicianPendingLeads: Callback<BaseCategoryVideoResponse> =
            object : Callback<BaseCategoryVideoResponse> {
                override fun onResponse(
                    call: Call<BaseCategoryVideoResponse>,
                    response: Response<BaseCategoryVideoResponse>
                ) {
                    if (response.isSuccessful) {
                        categoriesData.postValue(response.body()) // = response.body().let { Resource.success(it) }
                    } else {
//                    categoriesData.postValue(
//                        Resource.error(
//                        ErrorUtils.getError(
//                            response.errorBody(),
//                            response.code()
//                        )
//                    ))
                    }
                }

                override fun onFailure(call: Call<BaseCategoryVideoResponse>, t: Throwable) {
                    // categoriesData.postValue(Resource.error(ErrorUtils.getError(t)))
                }
            }

    private val callBackVideoSubCategories: Callback<BaseSubCategoryVideoResponse> =
        object : Callback<BaseSubCategoryVideoResponse> {
            override fun onResponse(
                call: Call<BaseSubCategoryVideoResponse>,
                response: Response<BaseSubCategoryVideoResponse>
            ) {
                if (response.isSuccessful) {
                    subCategoriesData.postValue(response.body()) // = response.body().let { Resource.success(it) }
                } else {
//                    categoriesData.postValue(
//                        Resource.error(
//                        ErrorUtils.getError(
//                            response.errorBody(),
//                            response.code()
//                        )
//                    ))
                }
            }

            override fun onFailure(call: Call<BaseSubCategoryVideoResponse>, t: Throwable) {
                // categoriesData.postValue(Resource.error(ErrorUtils.getError(t)))
            }
        }

    private val callbackVideoProducts: Callback<BaseProductsVideoResponse> =
        object : Callback<BaseProductsVideoResponse> {
            override fun onResponse(
                call: Call<BaseProductsVideoResponse>,
                response: Response<BaseProductsVideoResponse>
            ) {
                if (response.isSuccessful) {
                    videoProductData.postValue(response.body()) // = response.body().let { Resource.success(it) }
                } else {
//                    categoriesData.postValue(
//                        Resource.error(
//                        ErrorUtils.getError(
//                            response.errorBody(),
//                            response.code()
//                        )
//                    ))
                }
            }

            override fun onFailure(call: Call<BaseProductsVideoResponse>, t: Throwable) {
                // videoProductData.postValue(Resource.error(ErrorUtils.getError(t)))
            }
        }


}