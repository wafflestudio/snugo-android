package com.wafflestudio.snugo.features.records

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.wafflestudio.snugo.models.Record
import com.wafflestudio.snugo.network.SNUGORestApi
import com.wafflestudio.snugo.network.dto.core.RecordDto
import javax.inject.Inject

class RecordPageSource @Inject constructor(
    private val api: SNUGORestApi
): PagingSource<Int, Record>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Record> {
        val offset = params.key ?: 0
        return try {
            val response = api.getRecentRecord(
                page = offset,
                size = params.loadSize
            ).result.map { it.toRecord() }
            LoadResult.Page(
                data = response,
                prevKey = if(offset == 0) null else offset-params.loadSize,
                nextKey = if(response.isEmpty()) null else offset + params.loadSize
            )
        } catch (e: Exception){
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Record>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}