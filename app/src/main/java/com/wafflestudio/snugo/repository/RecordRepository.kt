package com.wafflestudio.snugo.repository

import androidx.paging.PagingData
import com.wafflestudio.snugo.models.Record
import com.wafflestudio.snugo.models.SortMethod
import kotlinx.coroutines.flow.Flow

interface RecordRepository {
    suspend fun getRecords(): Flow<PagingData<Record>>
}
