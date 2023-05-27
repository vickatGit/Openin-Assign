package com.example.listed.domain.repository

import com.example.listed.data.model.DashboardResponseModel

interface ListedRepo {

    suspend fun getDashBoardData(authToken:String):DashboardResponseModel
}