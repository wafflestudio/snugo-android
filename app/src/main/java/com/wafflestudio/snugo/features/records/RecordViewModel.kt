package com.wafflestudio.snugo.features.records

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.wafflestudio.snugo.models.Record
import com.wafflestudio.snugo.models.SortMethod
import com.wafflestudio.snugo.repository.RecordRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class RecordViewModel
    @Inject
    constructor(
        private val recordRepository: RecordRepository,
    ) : ViewModel() {

        private val querySignal = MutableStateFlow(Unit)

        @OptIn(ExperimentalCoroutinesApi::class)
        val myRecords: StateFlow<PagingData<Record>> =
            querySignal.flatMapLatest {
                recordRepository.getRecentRecords()
                    .cachedIn(viewModelScope)
            }
                .stateIn(viewModelScope, SharingStarted.Eagerly, PagingData.empty())

        suspend fun fetchRecords() {
            querySignal.emit(Unit)
        }

        suspend fun getRecord(method: SortMethod)  {
        /*when(method){
            SortMethod.BASIC -> {
                _myRecords.value = api.getBasicRecord()
            }
            SortMethod.TOP -> {
                _myRecords.value = api.getTopRecord()
            }
            SortMethod.RECOMMEND -> {
                _myRecords.value = api.getRecommendedRecord()
            }
        }*/
        }
    }
