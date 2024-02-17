package com.wafflestudio.snugo.location

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import com.wafflestudio.snugo.features.home.RouteRecord
import com.wafflestudio.snugo.service.LocationService
import dagger.hilt.android.qualifiers.ActivityContext
import kotlinx.coroutines.flow.Flow
import java.lang.ref.WeakReference
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

interface ILocationProvider {
    val currentRecordingPath: Flow<RouteRecord?>

    suspend fun startRecordingPath()
}

class LocationProvider(
    @ActivityContext context: Context,
    localStorageLocationRecordSaver: LocalStorageLocalStorageLocationRecordRecordSaver,
) : ILocationProvider {
    private val weakActivity = WeakReference(context as Activity)

    override val currentRecordingPath: Flow<RouteRecord?> =
        localStorageLocationRecordSaver.recordingPath

    override suspend fun startRecordingPath() {
        val service = startLocationService()
        service.startRecordingPath()
    }

    private suspend fun startLocationService(): LocationService =
        suspendCoroutine { continuation ->
            weakActivity.get()?.let {
                it.startService(Intent(it, LocationService::class.java))

                val connection =
                    object : ServiceConnection {
                        override fun onServiceConnected(
                            name: ComponentName?,
                            service: IBinder?,
                        ) {
                            val binder = service as LocationService.LocalBinder

                            continuation.resume(binder.getService())
                        }

                        override fun onServiceDisconnected(name: ComponentName?) {
                        }
                    }
                it.bindService(
                    Intent(it, LocationService::class.java),
                    connection,
                    Context.BIND_AUTO_CREATE,
                )
            }
        }
}
