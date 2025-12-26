package com.jayden.ad_manager

import android.content.Context
import com.jayden.ad_manager.app.viewmodel.MainViewModelProvider
import com.jayden.ad_manager.data.AdServiceDataSource
import com.jayden.ad_manager.repo.AdServiceRepo

class AppGraph(
    appContext: Context
) {
    val adServiceDataSource = AdServiceDataSource(appContext)
    val adServiceRepo = AdServiceRepo(adServiceDataSource)
    val viewModelProvider = MainViewModelProvider(adServiceRepo)
}