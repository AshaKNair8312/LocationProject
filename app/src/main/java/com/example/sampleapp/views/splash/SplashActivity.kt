package com.example.sampleapp.views.splash

import com.example.sampleapp.R
import com.example.sampleapp.utils.Constants
import com.example.sampleapp.utils.SessionManager
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_splash)

        setupDelay()
    }
    private fun setupDelay() {
        if(SessionManager.getRadio()=="")
            SessionManager.setRadio("standard")
        Handler(Looper.getMainLooper()).postDelayed({
                startActivity(Intent(this, TempActivity::class.java))

            finish()
        }, Constants.SPLASH_TIMEOUT)
    }
}