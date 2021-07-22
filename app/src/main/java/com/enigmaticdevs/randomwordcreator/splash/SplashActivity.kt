package com.enigmaticdevs.randomwordcreator.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.enigmaticdevs.randomwordcreator.MainActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Handler(Looper.getMainLooper()).postDelayed({ // This method will be executed once the timer is over
            val i = Intent(this, MainActivity::class.java)
            startActivity(i)
            finish()
        }, 700)
    }
}