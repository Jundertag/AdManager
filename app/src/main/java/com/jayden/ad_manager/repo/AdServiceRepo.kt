package com.jayden.ad_manager.repo

import android.adservices.adid.AdId
import com.jayden.ad_manager.data.AdServiceDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart

class AdServiceRepo(
    private val dataSource: AdServiceDataSource
) {
    suspend fun getAdId() = dataSource.fetchAdId()
    suspend fun getAppSetId() = dataSource.fetchAppSetId()
}