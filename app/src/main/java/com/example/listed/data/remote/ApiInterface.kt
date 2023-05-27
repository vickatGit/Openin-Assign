package com.example.listed.data.remote

import com.example.listed.data.model.DashboardResponseModel
import retrofit2.http.GET
import retrofit2.http.Header

interface ApiInterface {

    @GET("dashboardNew")
    suspend fun getDashboardData(@Header("Authorization") authToken: String):DashboardResponseModel
}