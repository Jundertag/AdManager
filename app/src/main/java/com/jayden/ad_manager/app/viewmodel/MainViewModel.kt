package com.jayden.ad_manager.app.viewmodel

import android.adservices.adid.AdId
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jayden.ad_manager.repo.AdServiceRepo
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onSubscription
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(
    private val repo: AdServiceRepo
) : ViewModel() {
    private val _adId = MutableStateFlow<AdId?>(null)
    val adId = _adId.asStateFlow()

    fun refreshAdId() {
        viewModelScope.launch {
            _adId.value = repo.getAdId()
        }
    }
}