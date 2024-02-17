package com.wafflestudio.snugo.network

import com.wafflestudio.snugo.network.dto.GetDepartmentsResponse
import com.wafflestudio.snugo.network.dto.PostSignUpRequestBody
import com.wafflestudio.snugo.network.dto.PostSignUpResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface SNUGORestApi {
    @GET("/v1/departments")
    suspend fun getDepartments(): GetDepartmentsResponse

    @POST("/v1/user/register")
    suspend fun postSignUp(
        @Body body: PostSignUpRequestBody
    ): PostSignUpResponse
}
