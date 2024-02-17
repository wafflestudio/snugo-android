package com.wafflestudio.snugo.network.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PostSignUpResponse(
    val token: String
)