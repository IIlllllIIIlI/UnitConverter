package com.convertly.app.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.convertly.app.ads.AdManager

@Composable
fun AdBanner(
    modifier: Modifier = Modifier
) {
    AndroidView(
        modifier = modifier.fillMaxWidth(),
        factory = { context ->
            val adView = AdManager.createBannerAdView(context)
            AdManager.loadBannerAd(adView)
            adView
        }
    )
}
