package com.wafflestudio.snugo.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.wafflestudio.snugo.features.records.RecordPageSource
import com.wafflestudio.snugo.models.Record
import com.wafflestudio.snugo.network.SNUGORestApi
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecordRepositoryImpl
    @Inject
    constructor(
        private val api: SNUGORestApi,
    ) : RecordRepository {
        override suspend fun getRecentRecords(): Flow<PagingData<Record>> {
            return Pager(
                config =
                    PagingConfig(
                        pageSize = 20,
                        enablePlaceholders = false,
                    ),
                pagingSourceFactory = {
                    RecordPageSource(
                        api,
                    )
                },
            ).flow
        }
    }