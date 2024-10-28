package com.orpatservice.app.data.model

import com.orpatservice.app.data.model.login.LoginBaseData
import com.orpatservice.app.ui.leads.new_lead_fragment.new_lead_request.TaskCompletedRequestData

data class VideoProductCategoriesData (
    val id : Int,
    val name : String
)

data class ProductVideoLinkDataResponse (
    val products: ProductVideoLinkInnerDataResponse,
    val pagination : PaginationData
)

data class ProductVideoLinkInnerDataResponse (
    val data: ArrayList<ProductVideoLinkData> = arrayListOf(),
    val pagination : PaginationData
)

data class ProductVideoLinkData (
    val id : String,
    val products_details_installation_link : String,
    val products_details_service_link : String,
    val name : String
)

data class SubCategoryVideoLinkData (
    val sub_categories: ArrayList<VideoProductCategoriesData> = arrayListOf(),

    )

data class CategoryVideoLinkData (
    val categories: ArrayList<VideoProductCategoriesData> = arrayListOf(),

    )

data class BaseProductsVideoResponse(val success: Boolean,
                         val data: ProductVideoLinkDataResponse,
                         val message: String)


data class BaseCategoryVideoResponse (val success: Boolean,
                                      val data: CategoryVideoLinkData,
                                      val message: String)

data class BaseSubCategoryVideoResponse (val success: Boolean,
                                      val data: SubCategoryVideoLinkData,
                                      val message: String)