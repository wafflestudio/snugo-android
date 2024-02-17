package com.wafflestudio.snugo.features.onboarding

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.wafflestudio.snugo.network.SnugoRestApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl
    @Inject
    constructor(
        private val api: SnugoRestApi,
        private val dataStore: DataStore<Preferences>,
    ) : UserRepository {
        override val accessToken: Flow<String?> = dataStore.data.map { it[ACCESS_TOKEN] ?: "" }

        override val nickname: Flow<String?> = dataStore.data.map { it[NICKNAME] }

        override val department: Flow<String?> = dataStore.data.map { it[DEPARTMENT] }

        override suspend fun getDepartments(): List<String> {
            return api.getDepartments()
        }

        override suspend fun signIn(
            nickname: String,
            department: String,
        ) {
            dataStore.edit { preferences ->
                preferences[ACCESS_TOKEN] = "testaccesstoken"
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
