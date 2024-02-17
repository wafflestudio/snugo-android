package com.wafflestudio.snugo.features.records

import android.util.Log
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.wafflestudio.snugo.LocalNavController
import com.wafflestudio.snugo.features.onboarding.UserViewModel
import com.wafflestudio.snugo.models.Record

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
    val recordState = remember { mutableStateOf(RecordState.BOX) }
    val boxIndex = remember { mutableStateOf(0) }
    val myRecords = recordViewModel.getPagedMyRecords().collectAsLazyPagingItems()
    val recordList = recordViewModel.myRecords.collectAsLazyPagingItems()

    if (recordState.value == RecordState.BOX) {
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
    } else {
        Log.d("aaaa", "changed")
        if (recordList.itemCount != 0) {
            RecordMap(path = recordList[boxIndex.value]?.path?.values.orEmpty().toList())
        }
    }

    MyRecordsList(myRecords)
}

@Composable
fun MyRecordsList(
    lazyPagingItems: LazyPagingItems<Record>,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier,
    ) {
        items(count = lazyPagingItems.itemCount) {
            Row(
                modifier = Modifier.fillMaxWidth(),
            ) {
            }
            lazyPagingItems[it]?.route
        }
    }
}
