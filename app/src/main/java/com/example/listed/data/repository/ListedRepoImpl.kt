package com.example.listed.data.repository

import com.example.listed.R
import com.example.listed.data.model.DashboardResponseModel
import com.example.listed.data.remote.ApiInterface
import com.example.listed.domain.repository.ListedRepo
import javax.inject.Inject

class ListedRepoImpl @Inject constructor(private val api: ApiInterface) : ListedRepo {
    override suspend fun getDashBoardData(authToken:String): DashboardResponseModel {
        return api.getDashboardData(authToken)
    }
}