package com.ozcoin.cookiepang.data.user

import com.ozcoin.cookiepang.domain.user.User
import kotlinx.serialization.Serializable

@Serializable
data class UserEntity(
    val id: Int,
    val walletAddress: String,
    val nickname: String,
    val introduction: String,
    val profileUrl: String? = null,
    val backgroundUrl: String? = null,
    val status: String? = null
)

fun UserEntity.toDomain(): User = User().apply {
    userId = id.toString()
    walletAddress = this.walletAddress
    profileID = nickname
}

fun User.toData(): UserEntity = UserEntity(
    id = kotlin.runCatching { userId.toInt() }.getOrDefault(0),
    walletAddress = this.walletAddress,
    nickname = profileID,
    introduction = "",
    profileUrl = null,
    backgroundUrl = null,
    status = null
)
