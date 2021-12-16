package com.orpatservice.app.ui.data.remote

import com.orpatservice.app.ui.data.model.TechnicianResponse
import retrofit2.Call
import retrofit2.http.GET

interface ApiEndPoint {

    @GET("technicians")
    fun getTechnicianAPI(): Call<TechnicianResponse>

    @GET("/technicians/create")
    fun hitAPIAddTechnician(): Call<TechnicianResponse>
}