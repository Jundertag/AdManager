package com.jayden.ad_manager.data

import android.adservices.AdServicesState
import android.adservices.adid.AdId
import android.adservices.adid.AdIdManager
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

    suspend fun fetchAdId(): AdId = suspendCancellableCoroutine { cout ->
        val adIdReceiver = object : OutcomeReceiver<AdId, Exception> {
            override fun onResult(result: AdId) {
                if (cout.isActive) cout.resume(result)
            }

            override fun onError(error: Exception) {
                if (cout.isActive) cout.resumeWithException(error)
            }
        }
        adIdManager.getAdId(ctx.mainExecutor, adIdReceiver)
    }


}