package com.jayden.ad_manager.app.viewmodel

import android.adservices.adid.AdId
import android.adservices.appsetid.AppSetId
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

    fun refreshAdId() {
        viewModelScope.launch {
            _adId.value = repo.getAdId()
        }
    }

    fun refreshAppId() {
        viewModelScope.launch {
            try {
                _appSetId.value = repo.getAppSetId()
            } catch (_: RuntimeException) {
                _appSetId.value = null
            }
        }
    }
}