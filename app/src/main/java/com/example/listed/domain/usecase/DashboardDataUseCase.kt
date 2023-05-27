package com.example.listed.domain.usecase

import com.example.listed.data.model.DashboardResponseModel
import com.example.listed.domain.repository.ListedRepo
import javax.inject.Inject

class DashboardDataUseCase @Inject constructor(private val repo:ListedRepo) {

    suspend operator fun invoke(authToken:String): DashboardResponseModel {
        return repo.getDashBoardData(authToken)
    }
}