package com.example.sampleapp.app

import com.example.sampleapp.utils.SessionManager
import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import timber.log.Timber

class Base : Application() {

    override fun onCreate() {
        super.onCreate()
        SessionManager.initializeval(applicationContext)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        Timber.plant(Timber.DebugTree())
    }
}