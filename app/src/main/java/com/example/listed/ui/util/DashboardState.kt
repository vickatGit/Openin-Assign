package com.example.listed.ui.util

import com.example.listed.data.model.DashboardResponseModel

sealed class DashboardState{
    object Loading: DashboardState()
    data class Success(var data:DashboardResponseModel): DashboardState()
    data class Error(var e:String): DashboardState()
}
