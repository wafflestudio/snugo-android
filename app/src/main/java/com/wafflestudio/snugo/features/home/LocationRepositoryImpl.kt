package com.wafflestudio.snugo.features.home

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.naver.maps.geometry.LatLng
import com.wafflestudio.snugo.data.MoshiSerializer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocationRepositoryImpl
    @Inject
    constructor(
        private val dataStore: DataStore<Preferences>,
        private val serializer: MoshiSerializer,
    ) : LocationRepository {
        override val recordingPath: Flow<RouteRecord?>
            get() =
                dataStore.data.map {
                    val rawData = it[PATH_KEY] ?: return@map null
                    serializer.deserialize<RouteRecord>(rawData, RouteRecord::class.java)
                }

        override suspend fun addLocationToPath(
            time: Long,
            location: LatLng,
        ) {
            dataStore.edit {
                val rawData = it[PATH_KEY]

                // 첫 기록일 경우
                if (rawData == null) {
                    it[PATH_KEY] =
                        serializer.serialize(
                            RouteRecord(listOf(TimeStamp(time, location))), RouteRecord::class.java,
                        )
                    return@edit
                }
                val before = serializer.deserialize<RouteRecord>(rawData, RouteRecord::class.java)
                val after =
                    before.copy(
                        path =
                            before.path.toMutableList().apply {
                                add(TimeStamp(time, location))
                            },
                    )
                it[PATH_KEY] = serializer.serialize(after, RouteRecord::class.java)
            }
        }

        override suspend fun clearPath() {
            dataStore.edit {
                it.remove(PATH_KEY)
            }
        }

        companion object {
            val PATH_KEY = stringPreferencesKey("path_key")
        }
    }
