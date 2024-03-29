package com.wafflestudio.snugo.repository

import kotlinx.coroutines.flow.Flow

interface UserRepository {
    val accessToken: Flow<String?>

    val nickname: Flow<String?>

    val department: Flow<String?>

    suspend fun getDepartments(): List<String>

    suspend fun signUp(
        nickname: String,
        department: String,
    )

    suspend fun signOut()
}
