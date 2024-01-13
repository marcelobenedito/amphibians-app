package com.github.marcelobenedito.amphibians

import android.app.Application
import com.github.marcelobenedito.amphibians.data.AppContainer
import com.github.marcelobenedito.amphibians.data.DefaultAppContainer

class AmphibiansApplication : Application() {
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer()
    }
}