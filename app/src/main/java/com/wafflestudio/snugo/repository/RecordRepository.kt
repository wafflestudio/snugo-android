package com.wafflestudio.snugo.repository

import androidx.paging.PagingData
import com.wafflestudio.snugo.models.Record
import kotlinx.coroutines.flow.Flow

interface RecordRepository {
    suspend fun getRecentRecords(): Flow<PagingData<Record>>
}