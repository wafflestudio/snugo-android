package com.wafflestudio.snugo.features.home

import com.naver.maps.geometry.LatLng

data class TimeStamp(
    val time: Long,
    val location: LatLng,
)
