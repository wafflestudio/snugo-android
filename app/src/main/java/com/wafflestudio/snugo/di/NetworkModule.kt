package com.wafflestudio.snugo.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.naver.maps.map.BuildConfig
import com.squareup.moshi.Moshi
import com.wafflestudio.snugo.R
import com.wafflestudio.snugo.network.SNUGORestApi
import com.wafflestudio.snugo.repository.UserRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenHolder @Inject constructor(
    dataStore: DataStore<Preferences>,
    externalScope: CoroutineScope
) {
    val accessToken: StateFlow<String?> = dataStore.data.map { it[UserRepositoryImpl.ACCESS_TOKEN] ?: "" }
        .stateIn(externalScope, SharingStarted.Eagerly, null)
}

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {
    @Provides
    fun provideOkHttpClient(
        tokenHolder: TokenHolder
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor { chain ->
                val newRequest = chain.request().newBuilder()
                    .addHeader(
                        "Authorization",
                        "Bearer ${tokenHolder.accessToken.value}"
                    )
                    .build()
                chain.proceed(newRequest)
            }
            .addNetworkInterceptor(
                HttpLoggingInterceptor().apply {
                    level =
                        if (BuildConfig.DEBUG) {
                            HttpLoggingInterceptor.Level.BODY
                        } else {
                            HttpLoggingInterceptor.Level.NONE
                        }
                },
            )
            .build()
    }

    @Provides
    fun provideRetrofit(
        @ApplicationContext context: Context,
        okHttpClient: OkHttpClient,
        moshi: Moshi,
    ): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(context.getString(R.string.api_base_url))
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    @Provides
    fun provideSNUGORestApi(retrofit: Retrofit): SNUGORestApi {
        return retrofit.create(SNUGORestApi::class.java)
    }
}
