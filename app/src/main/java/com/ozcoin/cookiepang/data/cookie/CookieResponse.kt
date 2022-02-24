package com.ozcoin.cookiepang.data.cookie

import kotlinx.serialization.Serializable

@Serializable
data class CookieResponse(
    val authorUserId: Int,
    val categoryId: Int,
    val createdAt: String,
    val fromBlockAddress: Int,
    val id: Int,
    val imageUrl: String?,
    val nftTokenId: Int,
    val ownedUserId: Int,
    val price: Int,
    val status: String,
    val title: String
)