package com.wafflestudio.snugo.features.records

import android.util.Log
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import androidx.paging.compose.itemsIndexed
import com.wafflestudio.snugo.features.onboarding.UserViewModel
import com.wafflestudio.snugo.models.Record

@Composable
fun RecordsScreen(
    modifier: Modifier = Modifier,
    viewModel: RecordViewModel,
    navController: NavController,
) {
    LaunchedEffect(Unit){
        viewModel.fetchRecords()
    }
    Log.d("aaaa","fetched")
    val recordState = remember { mutableStateOf(RecordState.BOX) }
    val boxIndex = remember { mutableStateOf(0) }
//    val record1 =
//        Record(
//            "id",
//            "dummyuser",
//            "nickname",
//            listOf(
//                Building(id = "", name = "asdf", location = LatLng(0,0), section = Section.A),
//                Building(id = "", name = "asdf", location = LatLng(0,0), section = Section.A)
//            ),
//            listOf(Pair(20L, LatLng(37.465, 126.95)), Pair(40L, LatLng(36.466, 127.951))),
//            System.currentTimeMillis(),
//            6109789L,
//        )
    val recordList = viewModel.myRecords.collectAsLazyPagingItems()
    /*val recordList = viewModel.myRecords.collectAsState().value
    LaunchedEffect(Unit){
        viewModel.getRecord(SortMethod.BASIC)
    }*/
    if (recordState.value == RecordState.BOX) {
        Log.d("aaaa",recordList.itemCount.toString())
        LazyColumn {
            itemsIndexed(recordList) { idx, it ->
                /*Log.d("aaaa",idx.toString())
                it ?: return@itemsIndexed*/
                it?.let {
                    RecordBox(
                        record = it,
                        navController = navController,
                        boxClicked = {
                            boxIndex.value = idx
                            recordState.value = RecordState.MAP
                        },
                    )
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
