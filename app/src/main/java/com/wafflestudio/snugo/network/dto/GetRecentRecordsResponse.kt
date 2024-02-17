package com.wafflestudio.snugo.network.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.wafflestudio.snugo.network.dto.core.RecordDto

@JsonClass(generateAdapter = true)
data class GetRecentRecordsResponse(
    val result: List<RecordDto>,
    @Json(name = "total_count") val totalCount: Int,
    val hasNext: Boolean,
)
