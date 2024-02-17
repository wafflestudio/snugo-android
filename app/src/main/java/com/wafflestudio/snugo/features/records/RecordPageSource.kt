package com.wafflestudio.snugo.features.records

import android.util.Log
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.wafflestudio.snugo.models.Record
import com.wafflestudio.snugo.network.SNUGORestApi
import javax.inject.Inject

class RecordPageSource(
        private val api: SNUGORestApi,
    ) : PagingSource<Int, Record>() {
        override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Record> {
            val page = params.key ?: START_PAGE
            return try {
                Log.d("asdf", "page=$page")
                val response =
                    api.getRecentRecord(
                        page = page,
                        size = params.loadSize,
                    )


                LoadResult.Page(
                    data = response.result.map{it.toRecord()},
                    prevKey = if (page == START_PAGE) null else page - 1,
                    nextKey = if (response.hasNext){
                        if (page == START_PAGE) {
                            page + params.loadSize / PAGE_SIZE
                        } else {
                            page + 1
                        }
                    } else {
                        null
                    }
                )
            } catch (e: Exception) {
                LoadResult.Error(e)
            }
        }

        override fun getRefreshKey(state: PagingState<Int, Record>): Int? {
            return state.anchorPosition?.let { anchorPosition ->
                state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                    ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
            }
        }

    companion object {
        private const val START_PAGE = 0
        private const val PAGE_SIZE = 7
        val Config = PagingConfig(pageSize = PAGE_SIZE)
    }
    }
