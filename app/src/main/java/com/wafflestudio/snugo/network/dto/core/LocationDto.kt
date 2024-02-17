package com.wafflestudio.snugo.network.dto.core

import com.naver.maps.geometry.LatLng
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LocationDto(
    val lat: Double,
    val lng: Double,
) {
    fun toLatLng(): LatLng = LatLng(lat, lng)
}