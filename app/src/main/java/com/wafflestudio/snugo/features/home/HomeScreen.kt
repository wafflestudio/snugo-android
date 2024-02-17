package com.wafflestudio.snugo.features.home

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.LocationTrackingMode
import com.naver.maps.map.compose.MapProperties
import com.naver.maps.map.compose.MapUiSettings
import com.naver.maps.map.compose.NaverMap
import com.naver.maps.map.compose.PathOverlay
import com.naver.maps.map.compose.rememberCameraPositionState
import com.naver.maps.map.compose.rememberFusedLocationSource

enum class HomePageMode {
    NORMAL,
    MOVING,
}

@OptIn(ExperimentalNaverMapApi::class)
@Composable
fun HomeScreen(
    modifier: Modifier,
    pageMode: HomePageMode,
    path: List<LatLng>,
    startMoving: () -> Unit,
) {
    val cameraPositionState = rememberCameraPositionState()
    var list by remember {
        mutableStateOf(listOf<LatLng>())
    }

    Column(
        modifier =
            modifier
                .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        NaverMap(
            modifier = Modifier.weight(0.5f),
            cameraPositionState = cameraPositionState,
            locationSource = rememberFusedLocationSource(),
            onMapClick = { _, latLng ->
                 list = (list + latLng).toMutableList()
            },
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
            if (list.size >=2 ) {
                PathOverlay(
                    coords = list,
                    width = 3.dp,
                    color = Color.Red,
                )
            }
        }
        Text(
            text = "기록 시작",
            modifier =
            Modifier
                .padding(15.dp)
                .clickable {
                    if (pageMode == HomePageMode.NORMAL) {
                        startMoving()
                    }
                },
            style =
                TextStyle(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                ),
        )
        Text(
            text = "로그",
            modifier =
            Modifier
                .padding(15.dp)
                .clickable {
                    Log.d("aaaa", list.fold("") {acc, latLng ->
                        acc + ", LatLng(${latLng.latitude}, ${latLng.longitude})"
                    })
                },
            style =
            TextStyle(
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
            ),
        )
        Text(
            text = "지우기",
            modifier =
            Modifier
                .padding(15.dp)
                .clickable {
                    list = emptyList()
                },
            style =
            TextStyle(
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
            ),
        )
    }
}
