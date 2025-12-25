package com.jayden.ad_manager.app.viewmodel

import android.adservices.adid.AdId
import android.adservices.appsetid.AppSetId
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jayden.ad_manager.repo.AdServiceRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(
    private val repo: AdServiceRepo
) : ViewModel() {
    private val _adId = MutableStateFlow<AdId?>(null)
    val adId = _adId.asStateFlow()

    private val _appSetId = MutableStateFlow<AppSetId?>(null)
    val appSetId = _appSetId.asStateFlow()

    private val _measurementApiStatus = MutableStateFlow<Boolean?>(null)
    val measurementApiStatus = _measurementApiStatus.asStateFlow()

    fun refreshAdId() {
        viewModelScope.launch {
            _adId.value = repo.getAdId()
        }
    }

    fun refreshAppId() {
        viewModelScope.launch {
            try {
                _appSetId.value = repo.getAppSetId()
            } catch (e: RuntimeException) {
                Log.w(TAG, "Received RuntimeException Error", e)
                _appSetId.value = null
            }
        }
    }

    fun refreshMeasurementApiStatus() {
        viewModelScope.launch {
            _measurementApiStatus.value = repo.getMeasurementApiStatus()
        }
    }

    companion object {
        private const val TAG = "MainViewModel"
    }
}