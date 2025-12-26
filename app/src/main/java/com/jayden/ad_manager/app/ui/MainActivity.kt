package com.jayden.ad_manager.app.ui

import android.adservices.appsetid.AppSetId
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.jayden.ad_manager.R
import com.jayden.ad_manager.app.MainApplication
import com.jayden.ad_manager.app.viewmodel.MainViewModel
import com.jayden.ad_manager.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainViewModel by viewModels(
        factoryProducer = { (application as MainApplication).appGraph.viewModelProvider }
    )

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i(TAG, "onCreate()")
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.adTitle.apply {
            post {
                val cutoutTopInset = windowManager.currentWindowMetrics.windowInsets.displayCutout?.safeInsetTop ?: 0
                Log.v(TAG, "inset top is $cutoutTopInset")
                (layoutParams as ViewGroup.MarginLayoutParams).setMargins(0, cutoutTopInset, 0, 0)
                Log.d(TAG, "set margins to { left = 0, top = $cutoutTopInset, right = 0, bottom = 0 }")
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.refreshAdId()
                viewModel.adId.collect { adId ->
                    if (adId == null) {
                        binding.adValue.text = resources.getString(R.string.ad_id_unavailable)
                        binding.adLimitedTrackingValue.text = resources.getString(R.string.ad_id_unavailable)
                    } else {
                        binding.adValue.text = "adId: ${adId.adId}"
                        binding.adLimitedTrackingValue.text = "isLimitedAdTrackingEnabled: ${adId.isLimitAdTrackingEnabled}"
                        binding.adDescription.text = if (adId.isLimitAdTrackingEnabled) {
                            resources.getString(R.string.ad_id_limited_true)
                        } else {
                            resources.getString(R.string.ad_id_limited_false)
                        }
                    }
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.refreshAppId()
                viewModel.appSetId.collect { appSetId ->
                    if (appSetId == null) {
                        binding.appSetIdValue.text = resources.getString(R.string.app_set_id_unavailable)
                        binding.appSetIdScope.text = resources.getString(R.string.app_set_id_unavailable)
                        binding.appSetIdDescription.text = resources.getString(R.string.app_set_id_unusable)
                    } else {
                        binding.appSetIdValue.text = "id: ${appSetId.id}"
                        binding.appSetIdScope.text = if (appSetId.scope == AppSetId.SCOPE_APP) {
                            "scope: SCOPE_APP = ${appSetId.scope}"
                        } else if (appSetId.scope == AppSetId.SCOPE_DEVELOPER) {
                            "scope: SCOPE_DEVELOPER = ${appSetId.scope}"
                        } else {
                            "<number-range-exceeded> scope = ${appSetId.scope}"
                        }
                        binding.appSetIdDescription.text = if (appSetId.scope == AppSetId.SCOPE_APP) {
                            resources.getString(R.string.app_set_id_scope_app)
                        } else if (appSetId.scope == AppSetId.SCOPE_DEVELOPER) {
                            resources.getString(R.string.app_set_id_scope_dev)
                        } else {
                            "<number-range-exceeded>"
                        }
                    }
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.refreshMeasurementApiStatus()
                viewModel.measurementApiStatus.collect { status ->
                    binding.measurementApiStatus.text = when (status) {
                        true -> "measurementApiStatus: MEASUREMENT_API_STATUS_ENABLED"
                        false -> "measurementApiStatus: MEASUREMENT_API_STATUS_DISABLED"
                        null -> "<unavailable>"
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        Log.i(TAG, "onDestroy()")
        _binding = null
        super.onDestroy()
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}