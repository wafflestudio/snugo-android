package com.wafflestudio.snugo.di

import com.wafflestudio.snugo.features.home.LocationRepository
import com.wafflestudio.snugo.features.home.LocationRepositoryImpl
import com.wafflestudio.snugo.repository.BuildingsRepository
import com.wafflestudio.snugo.repository.BuildingsRepositoryImpl
import com.wafflestudio.snugo.repository.UserRepository
import com.wafflestudio.snugo.repository.UserRepositoryImpl
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
    abstract fun bindBuildingsRepository(buildingsRepositoryImpl: BuildingsRepositoryImpl): BuildingsRepository

    @Binds
    abstract fun bindLocationRepository(locationRepositoryImpl: LocationRepositoryImpl): LocationRepository
}
