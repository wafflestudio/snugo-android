package com.wafflestudio.snugo.features.home

import com.naver.maps.geometry.LatLng
import kotlinx.coroutines.flow.Flow

interface LocationRepository {
    val recordingPath: Flow<RouteRecord?>

    suspend fun addLocationToPath(
        time: Long,
        location: LatLng,
    )

    suspend fun clearPath()
}
