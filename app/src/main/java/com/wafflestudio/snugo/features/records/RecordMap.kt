package com.wafflestudio.snugo.features.records

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.LocationTrackingMode
import com.naver.maps.map.compose.MapProperties
import com.naver.maps.map.compose.MapUiSettings
import com.naver.maps.map.compose.NaverMap
import com.naver.maps.map.compose.PathOverlay
import com.naver.maps.map.compose.rememberCameraPositionState
import com.naver.maps.map.compose.rememberFusedLocationSource

@OptIn(ExperimentalNaverMapApi::class)
@Composable
fun RecordMap(
    modifier: Modifier = Modifier,
    path: List<LatLng>,
) {
    Column {
        Box(modifier = Modifier.padding(50.dp).height(400.dp)) {
            val cameraPositionState = rememberCameraPositionState()
            NaverMap(
                modifier = Modifier,
                cameraPositionState = cameraPositionState,
                locationSource = rememberFusedLocationSource(),
                properties = MapProperties(locationTrackingMode = LocationTrackingMode.Follow),
                uiSettings = MapUiSettings(isLocationButtonEnabled = true),
            ) {
                if (path.size >= 2) {
                    PathOverlay(
                        coords = path,
                        width = 5.dp,
                        outlineWidth = 2.dp,
                        color = Color.Red,
                        outlineColor = Color.Green,
                    )
                }
            }
        }
    }
}
