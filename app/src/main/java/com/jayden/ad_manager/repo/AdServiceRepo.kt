package com.jayden.ad_manager.repo

import android.adservices.measurement.MeasurementManager
import com.jayden.ad_manager.data.AdServiceDataSource

class AdServiceRepo(
    private val dataSource: AdServiceDataSource
) {
    suspend fun getAdId() = dataSource.fetchAdId()
    suspend fun getAppSetId() = dataSource.fetchAppSetId()
    suspend fun getMeasurementApiStatus() = when (dataSource.fetchMeasurementManagerApiStatus()) {
        MeasurementManager.MEASUREMENT_API_STATE_DISABLED -> false
        MeasurementManager.MEASUREMENT_API_STATE_ENABLED -> true
        else -> null
    }
}