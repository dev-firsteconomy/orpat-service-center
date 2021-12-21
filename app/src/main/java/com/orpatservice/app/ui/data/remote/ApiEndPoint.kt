package com.orpatservice.app.ui.data.remote

import com.orpatservice.app.ui.data.model.TechnicianResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface ApiEndPoint {

    @GET("technicians")
    fun getTechnicianAPI(): Call<TechnicianResponse>

    // @FormUrlEncoded
    @POST("technicians")
    fun hitAPIAddTechnician(
        @Body requestBody: MultipartBody
    ): Call<TechnicianResponse>

    @POST("technicians/update_technician/{technician_id}")
    fun hitAPIUpdateTechnician(
        @Body requestBody: MultipartBody,
        @Path("technician_id") technician_id : Int?
    ): Call<TechnicianResponse>
}