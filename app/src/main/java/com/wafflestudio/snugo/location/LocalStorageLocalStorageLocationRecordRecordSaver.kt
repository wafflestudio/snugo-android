package com.wafflestudio.snugo.location

import com.naver.maps.geometry.LatLng
import com.wafflestudio.snugo.features.home.LocationRepository
import com.wafflestudio.snugo.features.home.RouteRecord
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface ILocalStorageLocationRecordSaver {
    val recordingPath: Flow<RouteRecord?>

    suspend fun addLocationToRecordingPath(
        time: Long,
        location: LatLng,
    )

    suspend fun clearRecordingPath()
}

class LocalStorageLocalStorageLocationRecordRecordSaver
    @Inject
    constructor(
        private val locationRepository: LocationRepository,
    ) : ILocalStorageLocationRecordSaver {
        override val recordingPath: Flow<RouteRecord?> = locationRepository.recordingPath

        override suspend fun addLocationToRecordingPath(
            time: Long,
            location: LatLng,
        ) {
            locationRepository.addLocationToPath(time, location)
        }

        override suspend fun clearRecordingPath() {
            locationRepository.clearPath()
        }
    }
