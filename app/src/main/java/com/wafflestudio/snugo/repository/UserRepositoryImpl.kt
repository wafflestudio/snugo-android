package com.wafflestudio.snugo.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.wafflestudio.snugo.network.SNUGORestApi
import com.wafflestudio.snugo.network.dto.PostSignUpRequestBody
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl
    @Inject
    constructor(
        private val api: SNUGORestApi,
        private val dataStore: DataStore<Preferences>,
    ) : UserRepository {
        override val accessToken: Flow<String?> = dataStore.data.map { it[ACCESS_TOKEN] ?: "" }

        override val nickname: Flow<String?> = dataStore.data.map { it[NICKNAME] }

        override val department: Flow<String?> = dataStore.data.map { it[DEPARTMENT] }

        override suspend fun getDepartments(): List<String> {
            return api.getDepartments()
        }

        override suspend fun signUp(
            nickname: String,
            department: String,
        ) {
            val response = api.postSignUp(PostSignUpRequestBody(nickname, department))
            dataStore.edit { preferences ->
                preferences[ACCESS_TOKEN] = response.token
                preferences[NICKNAME] = nickname
                preferences[DEPARTMENT] = department
            }
        }

        override suspend fun signOut() {
            dataStore.edit { preferences ->
                preferences.remove(ACCESS_TOKEN)
                preferences.remove(NICKNAME)
                preferences.remove(DEPARTMENT)
            }
        }

        companion object {
            val ACCESS_TOKEN = stringPreferencesKey("access_token")
            val NICKNAME = stringPreferencesKey("nickname")
            val DEPARTMENT = stringPreferencesKey("department")
        }
    }
