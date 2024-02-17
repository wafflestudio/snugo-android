package com.wafflestudio.snugo.features.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.LocationTrackingMode
import com.naver.maps.map.compose.MapProperties
import com.naver.maps.map.compose.MapUiSettings
import com.naver.maps.map.compose.Marker
import com.naver.maps.map.compose.MarkerState
import com.naver.maps.map.compose.NaverMap
import com.naver.maps.map.compose.PathOverlay
import com.naver.maps.map.compose.PolygonOverlay
import com.naver.maps.map.compose.rememberCameraPositionState
import com.naver.maps.map.compose.rememberFusedLocationSource
import com.wafflestudio.snugo.location.MapConstants
import com.wafflestudio.snugo.models.Section
import kotlinx.coroutines.launch

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
    homeViewModel: HomeViewModel = hiltViewModel(),
) {
    val cameraPositionState = rememberCameraPositionState()
    val scope = rememberCoroutineScope()

    val buildingsBySection by homeViewModel.buildingsBySection.collectAsState()
    var selectedSection by remember { mutableStateOf(Section.A) }
    var selectedBuilding by remember { mutableStateOf(buildingsBySection[Section.A]?.first()) }
    var pathCoords by remember {
        mutableStateOf(emptyList<LatLng>())
    }

    Column(
        modifier = modifier.background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        NaverMap(
            modifier = Modifier.fillMaxHeight(0.55f),
            cameraPositionState = cameraPositionState,
            onMapClick = { _, latLng ->
                pathCoords = (pathCoords + latLng)
            },
            locationSource = rememberFusedLocationSource(),
            properties =
                MapProperties(
                    locationTrackingMode = LocationTrackingMode.Follow,
                    extent = MapConstants.SNU_BOUNDARY,
                ),
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

            if (pathCoords.size >= 2) {
                PathOverlay(
                    coords = pathCoords,
                    width = 2.dp,
                    color = Color.Red,
                )
            }

            PolygonOverlay(
                coords = polygonMap[selectedSection]!!,
                color =
                    selectedSection.color().copy(
                        alpha = 0.4f,
                    ),
            )

            selectedBuilding?.let {
                Marker(
                    captionText = it.name,
                    state =
                        MarkerState(
                            position =
                                CameraPosition(
                                    it.location,
                                    6.0,
                                ).target,
                        ),
                )
            }
        }
        Column(
            modifier = Modifier.padding(horizontal = 20.dp),
        ) {
            Text(
                text = "기록 시작",
                modifier =
                    Modifier
                        .align(Alignment.CenterHorizontally)
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
            Row(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.weight(1f),
                ) {
                    Text(
                        text = "구역",
                        style = MaterialTheme.typography.titleMedium,
                    )
                    ScrollPicker(
                        items = Section.entries.map { it.toString() },
                        onItemSelected = { index ->
                            scope.launch {
                                selectedSection = Section.entries[index]
                            }
                        },
                    )
                }
                Spacer(modifier = Modifier.width(30.dp))
                Column(
                    modifier = Modifier.weight(1f),
                ) {
                    Text(
                        text = "건물",
                        style = MaterialTheme.typography.titleMedium,
                    )
                    ScrollPicker(
                        items = buildingsBySection[selectedSection]?.map { it.name } ?: emptyList(),
                        onItemSelected = { index ->
                            selectedBuilding = buildingsBySection[selectedSection]?.get(index)
                            selectedBuilding?.let {
                                scope.launch {
                                    cameraPositionState.animate(
                                        update =
                                            CameraUpdate.toCameraPosition(
                                                CameraPosition(
                                                    it.location,
                                                    15.0,
                                                ),
                                            ),
                                        durationMs = 1000,
                                    )
                                }
                            }
                        },
                    )
                }
            }
        }
    }
}
