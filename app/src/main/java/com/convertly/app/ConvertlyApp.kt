package com.convertly.app

import android.app.Application
import com.convertly.app.ads.AdManager

class ConvertlyApp : Application() {

    override fun onCreate() {
        super.onCreate()
        AdManager.initialize(this)
    }
}
