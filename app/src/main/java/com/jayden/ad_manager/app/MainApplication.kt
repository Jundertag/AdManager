package com.jayden.ad_manager.app

import android.app.Application
import com.jayden.ad_manager.AppGraph

class MainApplication : Application() {
    lateinit var appGraph: AppGraph
    override fun onCreate() {
        appGraph = AppGraph(applicationContext)
    }
}