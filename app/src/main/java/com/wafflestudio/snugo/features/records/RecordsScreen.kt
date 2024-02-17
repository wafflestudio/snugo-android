package com.wafflestudio.snugo.features.records

import android.util.Log
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemsIndexed

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecordsScreen(
    modifier: Modifier = Modifier,
    viewModel: RecordViewModel,
    navController: NavController,
) {
    LaunchedEffect(Unit) {
        viewModel.fetchRecords()
    }
    Log.d("aaaa", "fetched")
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

    val options = listOf("Recent", "NewHigh", "My")
    var expanded by remember { mutableStateOf(false) }
    var selectedOptionText by remember { mutableStateOf(options[0]) }

    if (recordState.value == RecordState.BOX) {
        /*ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {expanded = !expanded}
        ) {
            TextField(value = selectedOptionText,
                onValueChange = {} )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = {
                    expanded = false
                }
            ) {
                options.forEach { selectionOption ->
                    DropdownMenuItem(
                        onClick = {
                            selectedOptionText = selectionOption
                            expanded = false
                        }
                    ){
                        Text(text = selectionOption)
                    }
                }
            }
        }*/

        Log.d("asdf", recordList.itemCount.toString())
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
}
