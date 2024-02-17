package com.wafflestudio.snugo.models

import com.naver.maps.geometry.LatLng

data class Building(
    val id: String,
    val name: String,
    val location: LatLng,
    val section: Section,
)
