package com.wafflestudio.snugo.features.records

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.wafflestudio.snugo.network.SNUGORestApi
import javax.inject.Inject

class RecordPageSource @Inject constructor(
    private val api: SNUGORestApi
): PagingSource<Int, Record>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Record> {
        val offset = params.key ?: 0
        return try{
            //val response = api. // getRecord
            LoadResult.Page(
                data = response,
                prevKey = if(offset == 0) null else offset-params.loadSize,
                nextKey = if(response.isEmpty()) null else offset + params.loadSize
            )

        }
    }
    override fun getRefreshKey(state: PagingState<Int, Record>): Int? {
        TODO("Not yet implemented")
    }
}