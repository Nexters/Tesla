package com.ozcoin.cookiepang.data.response

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class AddressResponse(
    val address: String
)
