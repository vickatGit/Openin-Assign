package com.example.listed.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.listed.data.local.AuthManager
import com.example.listed.domain.usecase.DashboardDataUseCase
import com.example.listed.ui.util.DashboardState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class HomePageViewModel @Inject constructor(
    private val getDashboardDataUseCase: DashboardDataUseCase,
    private val authManager: AuthManager
) : ViewModel() {

    private val _loadState= MutableStateFlow<DashboardState>(DashboardState.Loading)

    val loadState:StateFlow<DashboardState> =_loadState

    fun getHomePageData() {
        viewModelScope.launch {
            _loadState.emit(DashboardState.Loading)
            try {
                val data =
                    getDashboardDataUseCase("Bearer ${authManager.getToken()}")
                _loadState.emit(DashboardState.Success(data))
            } catch (e: Exception) {
                Log.e("TAG", "getHomePageData: Exception ${e.message}")
                _loadState.emit(DashboardState.Error(e.message!!))
            }
        }

    }

    fun getGreetings(): String {
        val calendar = Calendar.getInstance()
        val hourOfDay = calendar.get(Calendar.HOUR_OF_DAY)

        return when {
            hourOfDay < 12 -> "Good morning"
            hourOfDay < 18 -> "Good afternoon"
            else -> "Good evening"
        }
    }

}