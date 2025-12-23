package com.jayden.ad_manager.app.ui

import android.adservices.AdServicesState
import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.jayden.ad_manager.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {
    private var _binding: ActivitySplashBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i(TAG, "onCreate()")
        super.onCreate(savedInstanceState)
        _binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val mainActivityIntent = Intent().apply {
            `package` = "com.jayden.ad_manager"
            component = ComponentName(applicationContext, MainActivity::class.java)
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }

        if (AdServicesState.isAdServicesStateEnabled()) {
            startActivity(mainActivityIntent)
        } else {
            // show ad services are disabled
            binding.adServicesStateText.visibility = View.VISIBLE
        }
    }

    override fun onDestroy() {
        Log.i(TAG, "onDestroy()")
        _binding = null
        super.onDestroy()
    }

    companion object {
        private const val TAG = "SplashActivity"
    }
}