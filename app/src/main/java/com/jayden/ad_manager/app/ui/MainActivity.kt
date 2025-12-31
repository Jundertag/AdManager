package com.jayden.ad_manager.app.ui

import android.adservices.appsetid.AppSetId
import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Bundle
import android.text.Html.fromHtml
import android.text.SpannableStringBuilder
import android.text.style.TypefaceSpan
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.core.text.toSpannable
import androidx.core.text.toSpanned
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.jayden.ad_manager.R
import com.jayden.ad_manager.app.MainApplication
import com.jayden.ad_manager.app.viewmodel.MainViewModel
import com.jayden.ad_manager.databinding.ActivityMainBinding
import io.noties.markwon.Markwon
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
        //val markwon = Markwon.create(applicationContext)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        TypefaceSpan("monospace")
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.refreshAdId()
                viewModel.adId.collect { adId ->
                    if (adId == null) {
                        //binding.adValue.text = resources.getText(R.string.ad_id_unavailable)
                        binding.adValue.text = resources.getText(R.string.ad_id_unavailable)
                        binding.adLimitedTrackingValue.text = resources.getText(R.string.ad_id_unavailable)
                    } else {
                        binding.adValue.text = SpannableStringBuilder().append(resources.getText(R.string.ad_id_value)).append(adId.adId)
                        binding.adLimitedTrackingValue.text = SpannableStringBuilder().append(resources.getText(R.string.ad_id_tracking_limited_value)).append(adId.isLimitAdTrackingEnabled.toString())
                        binding.adDescription.text = if (adId.isLimitAdTrackingEnabled) {
                            resources.getText(R.string.ad_id_limited_true)
                        } else {
                            resources.getText(R.string.ad_id_limited_false)
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
                        binding.appSetIdValue.text = resources.getText(R.string.app_set_id_unavailable)
                        binding.appSetIdScope.text = resources.getText(R.string.app_set_id_unavailable)
                        binding.appSetIdDescription.text = resources.getText(R.string.app_set_id_unusable)
                    } else {
                        binding.appSetIdValue.text = resources.getText(R.string.app_set_id_value, appSetId.id)
                        binding.appSetIdScope.text = if (appSetId.scope == AppSetId.SCOPE_APP) {
                            resources.getText(R.string.app_set_id_scope_app)
                        } else { // assumed SCOPE_DEVELOPER
                            resources.getText(R.string.app_set_id_scope_dev)
                        }
                        binding.appSetIdDescription.text = if (appSetId.scope == AppSetId.SCOPE_APP) {
                            resources.getText(R.string.app_set_id_scope_app_desc)
                        } else { // assumed SCOPE_DEVELOPER
                            resources.getText(R.string.app_set_id_scope_dev_desc)
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
                        true -> "measurementApiStatus: MEASUREMENT_API_STATUS_ENABLED = 1"
                        false -> "measurementApiStatus: MEASUREMENT_API_STATUS_DISABLED = 0"
                        null -> "<unavailable>"
                    }
                }
            }
        }

        binding.adTitle.post {
            val cutoutTopInset =
                windowManager.currentWindowMetrics.windowInsets.displayCutout?.safeInsetTop ?: 0
            Log.v(TAG, "inset top is $cutoutTopInset")
            (binding.adTitle.layoutParams as ViewGroup.MarginLayoutParams).setMargins(
                0,
                cutoutTopInset,
                0,
                0
            )
            Log.d(TAG, "set margins to { left = 0, top = $cutoutTopInset, right = 0, bottom = 0 }")
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