package com.jayden.ad_manager.data

import android.adservices.AdServicesState
import android.adservices.adid.AdId
import android.adservices.adid.AdIdManager
import android.adservices.appsetid.AppSetId
import android.adservices.appsetid.AppSetIdManager
import android.content.Context
import android.os.OutcomeReceiver
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class AdServiceDataSource(
    private val ctx: Context
) {
    private val adIdManager = AdIdManager.get(ctx)
    private val appSetIdManager = AppSetIdManager.get(ctx)

    suspend fun fetchAdId(): AdId = suspendCancellableCoroutine { continuation ->
        val adIdReceiver = object : OutcomeReceiver<AdId, Exception> {
            override fun onResult(result: AdId) {
                if (continuation.isActive) continuation.resume(result)
            }

            override fun onError(error: Exception) {
                if (continuation.isActive) continuation.resumeWithException(error)
            }
        }
        adIdManager.getAdId(ctx.mainExecutor, adIdReceiver)
    }

    suspend fun fetchAppSetId(): AppSetId = suspendCancellableCoroutine { continuation ->
        val appSetIdReceiver = object : OutcomeReceiver<AppSetId, Exception> {
            override fun onResult(result: AppSetId) {
                continuation.resume(result)
            }

            override fun onError(error: Exception) {
                continuation.resumeWithException(error)
            }
        }
        appSetIdManager.getAppSetId(ctx.mainExecutor, appSetIdReceiver)
    }
}