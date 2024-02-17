package com.wafflestudio.snugo.network.dto.core

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GetRecordDto(
    @Json(name = "result") val result: List<RecordDto>,
    @Json(name = "total_count") val total_count: Int,
    @Json(name = "hasNext") val hasNext: Boolean,
)
