package com.jayden.ad_manager.app.ui

import android.adservices.adid.AdId
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.marginTop
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

    private val adIdOnClickListener = View.OnClickListener {

    }

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
                        binding.adValue.text = "collecting..."
                        binding.adTrackingValue.text = "collecting..."
                    } else {
                        binding.adValue.text = adId.adId
                        binding.adTrackingValue.text = if (adId.isLimitAdTrackingEnabled) {
                            resources.getString(R.string.ad_id_limited_true)
                        } else {
                            resources.getString(R.string.ad_id_limited_false)
                        }
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