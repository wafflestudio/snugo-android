package com.wafflestudio.snugo.features.records

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.lang.Record
import javax.inject.Inject

class RecordViewModel @Inject constructor(
    private val repository: RecordRepository
): ViewModel(){
    private val _myRecords = MutableStateFlow<List<Record>>(listOf())
    val myRecords = _myRecords.asStateFlow()
    suspend fun getRecord(method: SortMethod){
        when(method){
            SortMethod.BASIC -> {
                _myRecords.value = api.getBasicRecord()
            }
            SortMethod.TOP -> {
                _myRecords.value = api.getTopRecord()
            }
            SortMethod.RECOMMEND -> {
                _myRecords.value = api.getRecommendedRecord()
            }
        }

    }
}