package com.wafflestudio.snugo.network

import com.wafflestudio.snugo.network.dto.GetDepartmentsResponse
import retrofit2.http.GET

interface SNUGORestApi {
    @GET("/v1/departments")
    suspend fun getDepartments(): GetDepartmentsResponse
}
