package com.wafflestudio.snugo.di

import com.wafflestudio.snugo.features.home.LocationRepository
import com.wafflestudio.snugo.features.home.LocationRepositoryImpl
import com.wafflestudio.snugo.features.onboarding.UserRepository
import com.wafflestudio.snugo.features.onboarding.UserRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindUserRepository(userRepositoryImpl: UserRepositoryImpl): UserRepository

    @Binds
    abstract fun bindLocationRepository(locationRepositoryImpl: LocationRepositoryImpl): LocationRepository
}
