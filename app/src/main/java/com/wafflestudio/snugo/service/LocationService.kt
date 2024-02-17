package com.wafflestudio.snugo.service

import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.os.Looper
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Granularity
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.naver.maps.geometry.LatLng
import com.wafflestudio.snugo.location.LocalStorageLocalStorageLocationRecordRecordSaver
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LocationService : LifecycleService() {
    @Inject
    lateinit var localStorageLocationRecordSaver: LocalStorageLocalStorageLocationRecordRecordSaver

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var locationListener: LocationListener? = null
    private val binder = LocalBinder()

    override fun onCreate() {
        super.onCreate()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(applicationContext)
    }

    override fun onStartCommand(
        intent: Intent?,
        flags: Int,
        startId: Int,
    ): Int {
        super.onStartCommand(intent, flags, startId)
        return if (intent == null) {
            START_NOT_STICKY
        } else {
            handleCommand(intent.action)
        }
    }

    override fun onBind(intent: Intent): IBinder? {
        super.onBind(intent)
        return binder
    }

    fun startRecordingPath() {
        try {
            locationListener =
                LocationListener {
                    lifecycleScope.launch {
                        localStorageLocationRecordSaver.addLocationToRecordingPath(
                            System.currentTimeMillis(),
                            LatLng(it.latitude, it.longitude),
                        )
                    }
                }

            fusedLocationClient.requestLocationUpdates(
                LocationRequest
                    .Builder(Priority.PRIORITY_HIGH_ACCURACY, 1000L)
                    .setMinUpdateDistanceMeters(1f)
                    .setGranularity(Granularity.GRANULARITY_PERMISSION_LEVEL)
                    .setWaitForAccurateLocation(true)
                    .build(),
                locationListener!!,
                Looper.getMainLooper(),
            )
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }

    fun stopRecordingPath() {
        locationListener = null
        lifecycleScope.launch {
            localStorageLocationRecordSaver.clearRecordingPath()
        }
    }

    private fun startForegroundService() {
        val notification = createNotification()
        startForeground(FOREGROUND_SERVICE_NOTIFICATION_ID, notification)
    }

    private fun handleCommand(action: String?): Int {
        return when (action) {
            "종료" -> {
                locationListener?.let {
                    fusedLocationClient.removeLocationUpdates(it)
                }
                stopForeground(STOP_FOREGROUND_REMOVE)
                stopSelf()
                START_NOT_STICKY
            }

            else -> {
                startForegroundService()
                START_STICKY
            }
        }
    }

    inner class LocalBinder : Binder() {
        fun getService(): LocationService {
            return this@LocationService
        }
    }

    companion object {
        private const val FOREGROUND_SERVICE_NOTIFICATION_ID = 1001
    }
}
