package com.datdang.snakeblock.billing

import android.app.Activity
import android.content.Context
import com.android.billingclient.api.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.collections.any
import kotlin.collections.firstOrNull

@Singleton
class BillingManager @Inject constructor(
    private val context: Context
) {
    private var billingClient: BillingClient? = null

    private val _isPremium = MutableStateFlow(false)
    val isPremium: StateFlow<Boolean> = _isPremium.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    companion object {
        const val REMOVE_ADS_SKU = "remove_ads"
    }

    init {
        setupBillingClient()
    }

    private fun setupBillingClient() {
        billingClient = BillingClient.newBuilder(context)
            .setListener { billingResult, purchases ->
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
                    for (purchase in purchases) {
                        handlePurchase(purchase)
                    }
                }
            }
            .enablePendingPurchases(
                PendingPurchasesParams.newBuilder()
                    .enableOneTimeProducts()
                    .build()
            )
            .build()

        billingClient?.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    checkPurchaseStatus()
                }
            }

            override fun onBillingServiceDisconnected() {
                // Retry connection
                setupBillingClient()
            }
        })
    }

    fun purchaseRemoveAds(activity: Activity, onSuccess: () -> Unit, onError: (String) -> Unit) {
        _isLoading.value = true

        val productList = listOf(
            QueryProductDetailsParams.Product.newBuilder()
                .setProductId(REMOVE_ADS_SKU)
                .setProductType(BillingClient.ProductType.INAPP)
                .build()
        )

        val params = QueryProductDetailsParams.newBuilder()
            .setProductList(productList)
            .build()

        billingClient?.queryProductDetailsAsync(params) { billingResult, productDetailsList ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                val productDetails = productDetailsList.productDetailsList.firstOrNull()
                if (productDetails != null) {
                    launchBillingFlow(activity, productDetails, onSuccess, onError)
                } else {
                    _isLoading.value = false
                    onError("Product not found")
                }
            } else {
                _isLoading.value = false
                onError("Failed to query product details: ${billingResult.debugMessage}")
            }
        }
    }

    private fun launchBillingFlow(
        activity: Activity,
        productDetails: ProductDetails,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val productDetailsParamsList = listOf(
            BillingFlowParams.ProductDetailsParams.newBuilder()
                .setProductDetails(productDetails)
                .build()
        )

        val billingFlowParams = BillingFlowParams.newBuilder()
            .setProductDetailsParamsList(productDetailsParamsList)
            .build()

        val billingResult = billingClient?.launchBillingFlow(activity, billingFlowParams)

        if (billingResult?.responseCode == BillingClient.BillingResponseCode.OK) {
            // Purchase flow started successfully
        } else {
            _isLoading.value = false
            onError("Failed to launch billing flow: ${billingResult?.debugMessage}")
        }
    }

    private fun handlePurchase(purchase: Purchase) {
        if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
            if (!purchase.isAcknowledged) {
                val acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder()
                    .setPurchaseToken(purchase.purchaseToken)
                    .build()

                billingClient?.acknowledgePurchase(acknowledgePurchaseParams) { billingResult ->
                    if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                        _isPremium.value = true
                        _isLoading.value = false
                    }
                }
            } else {
                _isPremium.value = true
                _isLoading.value = false
            }
        }
    }

    private fun checkPurchaseStatus() {
        val params = QueryPurchasesParams.newBuilder()
            .setProductType(BillingClient.ProductType.INAPP)
            .build()

        billingClient?.queryPurchasesAsync(params) { billingResult, purchases ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                val hasActivePurchase = purchases.any { purchase ->
                    purchase.purchaseState == Purchase.PurchaseState.PURCHASED &&
                    purchase.products.contains(REMOVE_ADS_SKU)
                }
                _isPremium.value = hasActivePurchase
            }
        }
    }

    fun disconnect() {
        billingClient?.endConnection()
    }
}