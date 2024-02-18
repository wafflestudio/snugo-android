package com.wafflestudio.snugo.repository

import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.wafflestudio.snugo.models.Record
import com.wafflestudio.snugo.network.SNUGORestApi
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MyRecordsPagingSource
    @Inject
    constructor(
        private val api: SNUGORestApi,
    ) : PagingSource<Int, Record>() {
        override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Record> {
            val currentPage = params.key ?: START_PAGE
            return try {
                val response =
                    api.getMyRecord(
                        page = currentPage,
                        size = params.loadSize,
                    )
                LoadResult.Page(
                    data = response.result.map { it.toRecord() },
                    prevKey = if (currentPage == START_PAGE) null else currentPage - 1,
                    nextKey =
                        if (response.hasNext) {
                            if (currentPage == START_PAGE) {
                                currentPage + params.loadSize / PAGE_SIZE
                            } else {
                                currentPage + 1
                            }
                        } else {
                            null
                        },
                )
            } catch (e: HttpException) {
                LoadResult.Error(e)
            }
        }

        override fun getRefreshKey(state: PagingState<Int, Record>): Int? {
            return START_PAGE
        }

        companion object {
            private const val START_PAGE = 0
            private const val PAGE_SIZE = 7
            val Config = PagingConfig(pageSize = PAGE_SIZE)
        }
    }
