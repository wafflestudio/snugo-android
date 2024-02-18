package com.wafflestudio.snugo.features.records

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.wafflestudio.snugo.LocalNavController
import com.wafflestudio.snugo.features.onboarding.UserViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecordsScreen(
    modifier: Modifier = Modifier,
    userViewModel: UserViewModel = hiltViewModel(),
    recordViewModel: RecordViewModel = hiltViewModel(),
) {
    LaunchedEffect(Unit) {
        recordViewModel.fetchRecords()
    }
    val navController = LocalNavController.current
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState { 2 }
    val recordState = remember { mutableStateOf(RecordState.BOX) }
    val boxIndex = remember { mutableStateOf(0) }
    val myRecords = recordViewModel.myRecords.collectAsLazyPagingItems()
    val recordList = recordViewModel.recentRecords.collectAsLazyPagingItems()

    if (recordState.value == RecordState.BOX) {
        Column(
            modifier =
                Modifier
                    .background(Color.White)
                    .fillMaxSize(),
        ) {
            TabRow(
                selectedTabIndex = pagerState.currentPage,
                modifier = Modifier.fillMaxWidth(),
            ) {
                repeat(2) {
                    Tab(
                        selected = it == pagerState.currentPage,
                        onClick = {
                            scope.launch {
                                pagerState.animateScrollToPage(it)
                            }
                        },
                        text = {
                            Text(
                                text =
                                    when (it) {
                                        0 -> "최근"
                                        1 -> "내 기록"
                                        else -> "invalid"
                                    },
                            )
                        },
                    )
                }
            }
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize(),
            ) {
                when (it) {
                    0 -> {
                        LazyColumn {
                            items(recordList.itemCount) {
                                /*Log.d("aaaa",idx.toString())
                                it ?: return@itemsIndexed*/
                                recordList[it]?.let { record ->
                                    Log.d("aaaa", record.toString())
                                    RecordBox(
                                        record = record,
                                        navController = navController,
                                        boxClicked = {
                                            boxIndex.value = it
                                            recordState.value = RecordState.MAP
                                        },
                                    )
                                }
                            }
                        }
                    }

                    1 -> {
                        if (myRecords.itemCount == 0) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                            ) {
                                Text(
                                    text = "기록이 없습니다.",
                                    modifier = Modifier.align(Alignment.Center),
                                    style = MaterialTheme.typography.titleLarge,
                                )
                            }
                        } else {
                            LazyColumn {
                                items(myRecords.itemCount) {
                                    recordList[it]?.let { record ->
                                        Log.d("aaaa", record.toString())
                                        RecordBox(
                                            record = record,
                                            navController = navController,
                                            boxClicked = {
                                                boxIndex.value = it
                                                recordState.value = RecordState.MAP
                                            },
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    } else {
        Log.d("aaaa", "changed")
        if (recordList.itemCount != 0) {
            RecordMap(path = recordList[boxIndex.value]?.path?.values.orEmpty().toList())
        }
    }
}
