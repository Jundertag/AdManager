package com.jayden.ad_manager.app.ui

import android.adservices.appsetid.AppSetId
import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Bundle
import android.text.Html.fromHtml
import android.text.SpannableStringBuilder
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

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.refreshAdId()
                viewModel.adId.collect { adId ->
                    if (adId == null) {
                        //binding.adValue.text = resources.getString(R.string.ad_id_unavailable)
                        binding.adValue.text = HtmlCompat.fromHtml(resources.getString(R.string.ad_id_unavailable), HtmlCompat.FROM_HTML_MODE_LEGACY)
                        binding.adLimitedTrackingValue.text = HtmlCompat.fromHtml(resources.getString(R.string.ad_id_unavailable), HtmlCompat.FROM_HTML_MODE_LEGACY)
                    } else {
                        binding.adValue.text = HtmlCompat.fromHtml("<tt>adId: ${adId.adId}</tt>", HtmlCompat.FROM_HTML_MODE_LEGACY)
                        binding.adLimitedTrackingValue.text = HtmlCompat.fromHtml("<tt>isLimitedAdTrackingEnabled: ${adId.isLimitAdTrackingEnabled}</tt>", HtmlCompat.FROM_HTML_MODE_LEGACY)
                        binding.adDescription.text = if (adId.isLimitAdTrackingEnabled) {
                            HtmlCompat.fromHtml(resources.getString(R.string.ad_id_limited_true), HtmlCompat.FROM_HTML_MODE_LEGACY)
                        } else {
                            HtmlCompat.fromHtml(resources.getString(R.string.ad_id_limited_false), HtmlCompat.FROM_HTML_MODE_LEGACY)
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
                        binding.appSetIdValue.text = HtmlCompat.fromHtml(resources.getString(R.string.app_set_id_unavailable), HtmlCompat.FROM_HTML_MODE_LEGACY)
                        binding.appSetIdScope.text = HtmlCompat.fromHtml(resources.getString(R.string.app_set_id_unavailable), HtmlCompat.FROM_HTML_MODE_LEGACY)
                        binding.appSetIdDescription.text = HtmlCompat.fromHtml(resources.getString(R.string.app_set_id_unusable), HtmlCompat.FROM_HTML_MODE_LEGACY)
                    } else {
                        binding.appSetIdValue.text = HtmlCompat.fromHtml(resources.getString(R.string.app_set_id_value, appSetId.id), HtmlCompat.FROM_HTML_MODE_LEGACY)
                        binding.appSetIdScope.text = if (appSetId.scope == AppSetId.SCOPE_APP) {
                            HtmlCompat.fromHtml(resources.getString(R.string.app_set_id_scope_app), HtmlCompat.FROM_HTML_MODE_LEGACY)
                        } else if (appSetId.scope == AppSetId.SCOPE_DEVELOPER) {
                            HtmlCompat.fromHtml(resources.getString(R.string.app_set_id_scope_dev), HtmlCompat.FROM_HTML_MODE_LEGACY)
                        } else {
                            HtmlCompat.fromHtml(resources.getString(R.string.app_set_id_scope_value, appSetId.scope), HtmlCompat.FROM_HTML_MODE_LEGACY)
                        }
                        binding.appSetIdDescription.text = if (appSetId.scope == AppSetId.SCOPE_APP) {
                            HtmlCompat.fromHtml(
                                resources.getString(R.string.app_set_id_scope_app_desc),
                                HtmlCompat.FROM_HTML_MODE_LEGACY
                            )
                        } else { // assumed SCOPE_DEVELOPER
                            HtmlCompat.fromHtml(
                                resources.getString(R.string.app_set_id_scope_dev_desc),
                                HtmlCompat.FROM_HTML_MODE_LEGACY
                            )
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