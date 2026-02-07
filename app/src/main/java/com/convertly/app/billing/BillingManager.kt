package com.convertly.app.billing

import android.app.Activity
import android.content.Context
import com.android.billingclient.api.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class BillingManager(private val context: Context) : PurchasesUpdatedListener {

    companion object {
        const val PRODUCT_REMOVE_ADS = "remove_ads_lifetime"
    }

    private var billingClient: BillingClient? = null
    private var productDetails: ProductDetails? = null

    private val _purchaseState = MutableStateFlow<PurchaseState>(PurchaseState.Idle)
    val purchaseState: StateFlow<PurchaseState> = _purchaseState.asStateFlow()

    private val _isAdFree = MutableStateFlow(false)
    val isAdFree: StateFlow<Boolean> = _isAdFree.asStateFlow()

    private var onPurchaseComplete: ((Boolean) -> Unit)? = null

    sealed class PurchaseState {
        data object Idle : PurchaseState()
        data object Loading : PurchaseState()
        data class Available(val price: String) : PurchaseState()
        data object Purchased : PurchaseState()
        data class Error(val message: String) : PurchaseState()
    }

    fun initialize() {
        billingClient = BillingClient.newBuilder(context)
            .setListener(this)
            .enablePendingPurchases()
            .build()

        billingClient?.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    queryProducts()
                    queryExistingPurchases()
                }
            }

            override fun onBillingServiceDisconnected() {
                // Retry connection
            }
        })
    }

    private fun queryProducts() {
        val productList = listOf(
            QueryProductDetailsParams.Product.newBuilder()
                .setProductId(PRODUCT_REMOVE_ADS)
                .setProductType(BillingClient.ProductType.INAPP)
                .build()
        )

        val params = QueryProductDetailsParams.newBuilder()
            .setProductList(productList)
            .build()

        _purchaseState.value = PurchaseState.Loading

        billingClient?.queryProductDetailsAsync(params) { billingResult, productDetailsList ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK &&
                productDetailsList.isNotEmpty()
            ) {
                productDetails = productDetailsList[0]
                val price = productDetails?.oneTimePurchaseOfferDetails?.formattedPrice ?: "$0.99"
                _purchaseState.value = PurchaseState.Available(price)
            } else {
                // Product not yet configured in Play Console - show default price
                _purchaseState.value = PurchaseState.Available("$0.99")
            }
        }
    }

    private fun queryExistingPurchases() {
        billingClient?.queryPurchasesAsync(
            QueryPurchasesParams.newBuilder()
                .setProductType(BillingClient.ProductType.INAPP)
                .build()
        ) { billingResult, purchasesList ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                for (purchase in purchasesList) {
                    if (purchase.products.contains(PRODUCT_REMOVE_ADS) &&
                        purchase.purchaseState == Purchase.PurchaseState.PURCHASED
                    ) {
                        _isAdFree.value = true
                        _purchaseState.value = PurchaseState.Purchased
                        if (!purchase.isAcknowledged) {
                            acknowledgePurchase(purchase)
                        }
                    }
                }
            }
        }
    }

    fun launchPurchaseFlow(activity: Activity, onComplete: (Boolean) -> Unit) {
        onPurchaseComplete = onComplete

        val details = productDetails
        if (details != null) {
            val productDetailsParams = BillingFlowParams.ProductDetailsParams.newBuilder()
                .setProductDetails(details)
                .build()

            val billingFlowParams = BillingFlowParams.newBuilder()
                .setProductDetailsParamsList(listOf(productDetailsParams))
                .build()

            billingClient?.launchBillingFlow(activity, billingFlowParams)
        } else {
            _purchaseState.value = PurchaseState.Error("Product not available. Please try again later.")
            onComplete(false)
        }
    }

    fun restorePurchases(onComplete: (Boolean) -> Unit) {
        billingClient?.queryPurchasesAsync(
            QueryPurchasesParams.newBuilder()
                .setProductType(BillingClient.ProductType.INAPP)
                .build()
        ) { billingResult, purchasesList ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                var found = false
                for (purchase in purchasesList) {
                    if (purchase.products.contains(PRODUCT_REMOVE_ADS) &&
                        purchase.purchaseState == Purchase.PurchaseState.PURCHASED
                    ) {
                        found = true
                        _isAdFree.value = true
                        _purchaseState.value = PurchaseState.Purchased
                    }
                }
                onComplete(found)
            } else {
                onComplete(false)
            }
        }
    }

    override fun onPurchasesUpdated(billingResult: BillingResult, purchases: List<Purchase>?) {
        when (billingResult.responseCode) {
            BillingClient.BillingResponseCode.OK -> {
                purchases?.forEach { purchase ->
                    if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
                        acknowledgePurchase(purchase)
                        _isAdFree.value = true
                        _purchaseState.value = PurchaseState.Purchased
                        onPurchaseComplete?.invoke(true)
                    }
                }
            }
            BillingClient.BillingResponseCode.USER_CANCELED -> {
                _purchaseState.value = PurchaseState.Available(
                    productDetails?.oneTimePurchaseOfferDetails?.formattedPrice ?: "$0.99"
                )
                onPurchaseComplete?.invoke(false)
            }
            else -> {
                _purchaseState.value = PurchaseState.Error("Purchase failed. Please try again.")
                onPurchaseComplete?.invoke(false)
            }
        }
    }

    private fun acknowledgePurchase(purchase: Purchase) {
        val params = AcknowledgePurchaseParams.newBuilder()
            .setPurchaseToken(purchase.purchaseToken)
            .build()

        billingClient?.acknowledgePurchase(params) { /* handled */ }
    }

    fun destroy() {
        billingClient?.endConnection()
    }
}
