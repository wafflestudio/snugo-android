package com.wafflestudio.snugo.models

import com.naver.maps.geometry.LatLng
import java.time.LocalDateTime

data class Record(
    val id: String,
    val userId: String,
    val nickname: String,
    val route: Route,
    val path: Map<LocalDateTime, LatLng>,
    val startTime: Long,
    val duration: Long,
    val isHigh: Boolean,
)