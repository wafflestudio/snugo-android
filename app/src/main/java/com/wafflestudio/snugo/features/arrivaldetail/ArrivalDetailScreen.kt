package com.wafflestudio.snugo.features.arrivaldetail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.NaverMap
import com.wafflestudio.snugo.components.CtaButton

@OptIn(ExperimentalNaverMapApi::class)
@Composable
fun ArrivalDetailScreen(modifier: Modifier = Modifier) {
    Column(
        modifier =
            modifier
                .background(Color.White)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
    ) {
        NaverMap(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .aspectRatio(0.8f),
        ) {
        }
        Column(
            modifier = Modifier.padding(horizontal = 20.dp),
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            Column {
                Text(
                    text = "소요 시간",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
                )
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "31분 00초",
                        style =
                            MaterialTheme.typography.displayLarge.copy(
                                fontSize = 40.sp,
                                fontWeight = FontWeight.ExtraBold,
                            ),
                        modifier = Modifier.weight(1f),
                    )
                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(5.dp),
                        ) {
                            Text(
                                text = "출발",
                                style = MaterialTheme.typography.titleMedium,
                            )
                            Text(
                                text = "15:30:00",
                                style = MaterialTheme.typography.bodyLarge,
                            )
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(5.dp),
                        ) {
                            Text(
                                text = "도착",
                                style = MaterialTheme.typography.titleMedium,
                            )
                            Text(
                                text = "16:01:00",
                                style = MaterialTheme.typography.bodyLarge,
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(40.dp))
            Column {
                Text(
                    text = "지나온 건물들",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
                )
                Spacer(modifier = Modifier.height(10.dp))
                Column(
                    modifier = Modifier.padding(start = 20.dp),
                ) {
                    val buildings = listOf("자연과학관 6", "관정도서관", "대형구조실험동", "제2공학관")
                    buildings.forEachIndexed { idx, name ->
                        Box(
                            modifier = Modifier.height(30.dp),
                        ) {
                            Text(
                                text = name,
                                style =
                                    MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = if (idx == 0 || idx == buildings.size - 1) FontWeight.Bold else FontWeight.Normal,
                                    ),
                                modifier = Modifier.align(Alignment.Center),
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(30.dp))
            CtaButton(
                onClick = { },
                modifier = Modifier.fillMaxSize(),
            ) {
                Text(
                    text = "업로드",
                )
            }
        }
    }
}
