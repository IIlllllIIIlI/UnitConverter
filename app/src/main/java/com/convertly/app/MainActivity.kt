package com.convertly.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.convertly.app.billing.BillingManager
import com.convertly.app.ui.navigation.ConvertlyNavGraph
import com.convertly.app.ui.screens.MainViewModel
import com.convertly.app.ui.theme.ConvertlyTheme

class MainActivity : ComponentActivity() {

    private lateinit var billingManager: BillingManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        billingManager = BillingManager(this)
        billingManager.initialize()

        setContent {
            val mainViewModel: MainViewModel = viewModel()
            val navController = rememberNavController()

            val themeMode by mainViewModel.themeMode.collectAsState()
            val accentColor by mainViewModel.accentColor.collectAsState()
            val adsRemoved by mainViewModel.adsRemoved.collectAsState()
            val isAdFree by billingManager.isAdFree.collectAsState()

            // Sync billing state with local prefs
            LaunchedEffect(isAdFree) {
                if (isAdFree) {
                    mainViewModel.setAdsRemoved(true)
                }
            }

            val showAds = !adsRemoved && !isAdFree

            ConvertlyTheme(
                themeMode = themeMode,
                accentColorName = accentColor
            ) {
                Surface(modifier = Modifier.fillMaxSize()) {
                    ConvertlyNavGraph(
                        navController = navController,
                        viewModel = mainViewModel,
                        billingManager = billingManager,
                        showAds = showAds
                    )
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::billingManager.isInitialized) {
            billingManager.destroy()
        }
    }
}
