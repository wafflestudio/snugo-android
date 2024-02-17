package com.wafflestudio.snugo.network.dto.core

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.wafflestudio.snugo.models.Record
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

@JsonClass(generateAdapter = true)
data class RecordDto(
    val id: String,
    val userId: String,
    val nickname: String,
    @Json(name = "routeType") val route: RouteDto,
    val path: Map<String, LocationDto>,
    val startTime: Long,
    val duration: Long,
    @Json(name = "highscoreyn") val isHigh: Boolean,
) {
    fun toRecord(): Record =
        Record(
            id = id,
            userId = userId,
            nickname = nickname,
            route = route.toRoute(),
            path =
                path.entries.associate {
                    LocalDateTime.ofInstant(
                        Instant.ofEpochMilli(it.key.toLong()),
                        ZoneId.systemDefault(),
                    ) to it.value.toLatLng()
                },
            startTime = startTime,
            duration = duration,
            isHigh = isHigh,
        )
}
