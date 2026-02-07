package com.convertly.app.ads

import android.content.Context
import android.util.Log
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds

object AdManager {

    // Test ad unit IDs - REPLACE WITH YOUR REAL AD UNIT IDS BEFORE PUBLISHING
    // Banner: ca-app-pub-3940256099942544/6300978111 (test)
    const val BANNER_AD_UNIT_ID = "ca-app-pub-3940256099942544/6300978111"

    private var isInitialized = false

    fun initialize(context: Context) {
        if (!isInitialized) {
            MobileAds.initialize(context) {
                isInitialized = true
                Log.d("AdManager", "AdMob initialized successfully")
            }
        }
    }

    fun createBannerAdView(context: Context): AdView {
        return AdView(context).apply {
            setAdSize(AdSize.BANNER)
            adUnitId = BANNER_AD_UNIT_ID
        }
    }

    fun loadBannerAd(adView: AdView) {
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)
    }
}
