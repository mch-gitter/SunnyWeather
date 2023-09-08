package com.example.sunnyweather

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.media.session.MediaSession.Token

class SunnyWeatherApplication: Application() {
    companion object{
        @SuppressLint("staticFieldLeak")
        lateinit var context: Context
        const val TOKEN = "pLYi1mHzxanBALrj"
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}