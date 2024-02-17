package com.wafflestudio.snugo.network

import com.wafflestudio.snugo.network.dto.GetBuildingResponse
import com.wafflestudio.snugo.network.dto.GetBuildingsBySectionResponse
import com.wafflestudio.snugo.network.dto.GetBuildingsResponse
import com.wafflestudio.snugo.network.dto.GetDepartmentsResponse
import com.wafflestudio.snugo.network.dto.PostSignUpRequestBody
import com.wafflestudio.snugo.network.dto.PostSignUpResponse
import com.wafflestudio.snugo.network.dto.core.GetRecordDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface SNUGORestApi {
    @GET("/v1/departments")
    suspend fun getDepartments(): GetDepartmentsResponse

    @POST("/v1/user/register")
    suspend fun postSignUp(
        @Body body: PostSignUpRequestBody,
    ): PostSignUpResponse

    @GET("/v1/buildings")
    suspend fun getBuildings(): GetBuildingsResponse

    @GET("/v1/buildings/{buildingId}")
    suspend fun getBuilding(
        @Path("buildingId") buildingId: String,
    ): GetBuildingResponse

    @GET("/v1/buildings/section/{section}")
    suspend fun getBuildingsBySection(
        @Path("section") section: String,
    ): GetBuildingsBySectionResponse

    @GET("v1/record/recent")
    suspend fun getRecentRecord(
        @Query("page") page: Int,
        @Query("size") size: Int,
    ): GetRecordDto
}
