package com.wafflestudio.snugo.features.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.naver.maps.geometry.LatLng
import com.naver.maps.geometry.LatLngBounds
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
import com.naver.maps.map.util.MarkerIcons
import com.wafflestudio.snugo.R
import com.wafflestudio.snugo.location.MapConstants
import com.wafflestudio.snugo.models.Section
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

enum class HomePageMode {
    NORMAL,
    MOVING,
}

enum class Point {
    START,
    END,
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
    val density = LocalDensity.current

    val buildingsBySection by homeViewModel.buildingsBySection.collectAsState()
    var selectedSection by remember { mutableStateOf(Section.A) }
    val startBuilding by homeViewModel.startBuilding.collectAsState()
    val endBuilding by homeViewModel.endBuilding.collectAsState()
    var editingPoint by remember { mutableStateOf(Point.START) }

    LaunchedEffect(startBuilding, endBuilding) {
        val points =
            listOfNotNull(
                startBuilding?.location,
                endBuilding?.location,
            ).distinct()
        if (points.isNotEmpty()) {
            scope.launch {
                cameraPositionState.animate(
                    update =
                        if (points.size == 1) {
                            CameraUpdate.toCameraPosition(
                                CameraPosition(
                                    points.first(),
                                    15.0,
                                ),
                            )
                        } else {
                            CameraUpdate.fitBounds(
                                LatLngBounds.from(points),
                                with(density) { 80.dp.toPx() }.roundToInt(),
                            )
                        },
                    durationMs = 1000,
                )
            }
        }
    }

    Column(
        modifier = modifier.background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        NaverMap(
            modifier = Modifier.fillMaxHeight(0.55f),
            cameraPositionState = cameraPositionState,
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

            PolygonOverlay(
                coords = polygonMap[selectedSection]!!,
                color =
                    selectedSection.color().copy(
                        alpha = 0.4f,
                    ),
            )

            startBuilding?.let {
                Marker(
                    state =
                        MarkerState(
                            position =
                                CameraPosition(
                                    it.location,
                                    6.0,
                                ).target,
                        ),
                    icon = MarkerIcons.BLUE,
                    captionText = it.name,
                )
            }
            endBuilding?.let {
                Marker(
                    state =
                        MarkerState(
                            position =
                                CameraPosition(
                                    it.location,
                                    6.0,
                                ).target,
                        ),
                    icon = MarkerIcons.RED,
                    captionText = it.name,
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(7.dp),
            ) {
                Box(
                    modifier =
                        Modifier
                            .weight(1f)
                            .height(40.dp)
                            .then(
                                when (editingPoint) {
                                    Point.START ->
                                        Modifier.border(
                                            width = 2.dp,
                                            color = Color.Blue,
                                            shape = RoundedCornerShape(10.dp),
                                        )

                                    Point.END ->
                                        Modifier.border(
                                            width = 0.5.dp,
                                            color = Color.LightGray,
                                            shape = RoundedCornerShape(10.dp),
                                        )
                                },
                            )
                            .clickable {
                                editingPoint = Point.START
                            },
                ) {
                    Text(
                        text = startBuilding?.name ?: "출발지 선택",
                        modifier = Modifier.align(Alignment.Center),
                    )
                }
                Icon(
                    painter = painterResource(R.drawable.ic_arrow_right),
                    contentDescription = "right arrow",
                    modifier = Modifier.size(30.dp),
                    tint = Color.LightGray,
                )
                Box(
                    modifier =
                        Modifier
                            .weight(1f)
                            .height(40.dp)
                            .then(
                                when (editingPoint) {
                                    Point.END ->
                                        Modifier.border(
                                            width = 2.dp,
                                            color = Color.Red,
                                            shape = RoundedCornerShape(10.dp),
                                        )

                                    Point.START ->
                                        Modifier.border(
                                            width = 0.5.dp,
                                            color = Color.LightGray,
                                            shape = RoundedCornerShape(10.dp),
                                        )
                                },
                            )
                            .clickable {
                                editingPoint = Point.END
                            },
                ) {
                    Text(
                        text = endBuilding?.name ?: "도착지 선택",
                        modifier = Modifier.align(Alignment.Center),
                    )
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(20.dp),
            ) {
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
                            when (editingPoint) {
                                Point.START -> {
                                    buildingsBySection[selectedSection]?.get(index)?.let {
                                        homeViewModel.setStartBuilding(it)
                                    }
                                }
                                Point.END -> {
                                    buildingsBySection[selectedSection]?.get(index)?.let {
                                        homeViewModel.setEndBuilding(it)
                                    }
                                }
                            }
                        },
                    )
                }
            }
        }
    }
}
