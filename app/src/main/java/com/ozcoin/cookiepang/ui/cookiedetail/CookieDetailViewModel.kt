package com.ozcoin.cookiepang.ui.cookiedetail

import androidx.lifecycle.viewModelScope
import com.ozcoin.cookiepang.base.BaseViewModel
import com.ozcoin.cookiepang.domain.cookiedetail.CookieDetail
import com.ozcoin.cookiepang.domain.cookiedetail.CookieDetailRepository
import com.ozcoin.cookiepang.utils.DataResult
import com.ozcoin.cookiepang.utils.DialogUtil
import com.ozcoin.cookiepang.utils.Event
import com.ozcoin.cookiepang.utils.UiState
import com.ozcoin.cookiepang.utils.observer.ActivityEventObserver
import com.ozcoin.cookiepang.utils.observer.UiStateObserver
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class CookieDetailViewModel @Inject constructor(
    private val cookieDetailRepository: CookieDetailRepository
) : BaseViewModel() {

    private val _cookieDetail = MutableStateFlow<CookieDetail?>(null)
    val cookieDetail: StateFlow<CookieDetail?>
        get() = _cookieDetail

    lateinit var activityEventObserver: ActivityEventObserver
    lateinit var uiStateObserver: UiStateObserver

    private lateinit var cookieId: String

    fun getCookieDetail(cookieId: String) {
        if (cookieId.isNotEmpty()) {
            this.cookieId = cookieId
            uiStateObserver.update(UiState.OnLoading)

            viewModelScope.launch {
                val result = cookieDetailRepository.getCookieDetail(cookieId)
                if (result is DataResult.OnSuccess) {
                    Timber.d("getCookieDetail($cookieId) is success")
                    uiStateObserver.update(UiState.OnSuccess)
                    _cookieDetail.emit(result.response)
                } else {
                    Timber.d("getCookieDetail($cookieId) is fail")
                    uiStateObserver.update(UiState.OnFail)
                }
            }
        } else {
            navigateUp()
        }
    }

    private fun purchaseCookie() {
        Timber.d("purchaseCookie() called")

        uiStateObserver.update(UiState.OnLoading)

        viewModelScope.launch {
            delay(1000L)

            uiStateObserver.update(UiState.OnSuccess)
            showPurchaseCookieSuccessDialog()
        }
    }

    private fun showPurchaseCookieSuccessDialog() {
        activityEventObserver.update(
            Event.ShowDialog(
                DialogUtil.getPurchaseCookieSuccessContents(),
                callback = {
                    if (it) getCookieDetail(cookieId) else Timber.d("PurchaseCookieSuccessDialog cancelled")
                }
            )
        )
    }

    private fun showConfirmPurchaseCookieDialog() {
        activityEventObserver.update(
            Event.ShowDialog(
                DialogUtil.getConfirmPurchaseCookieContents(cookieDetail.value),
                callback = {
                    if (it) purchaseCookie() else Timber.d("ConfirmPurchaseCookieDialog cancelled")
                }
            )
        )
    }

    private fun editPricingInfo() {
    }

    fun clickCookieContentsBtn(isMine: Boolean) {
        if (isMine) {
            editPricingInfo()
        } else {
            showConfirmPurchaseCookieDialog()
        }
    }
}