package com.wafflestudio.snugo.network.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PostSignUpRequestBody(
    val nickname: String,
    val department: String,
)
