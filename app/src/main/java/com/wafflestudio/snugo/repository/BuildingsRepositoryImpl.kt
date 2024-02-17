package com.wafflestudio.snugo.repository

import com.wafflestudio.snugo.models.Building
import com.wafflestudio.snugo.network.SNUGORestApi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BuildingsRepositoryImpl @Inject constructor(
    private val api: SNUGORestApi,
) : BuildingsRepository {
    override suspend fun getBuildings(): List<Building> {
        return api.getBuildings().map { it.toBuilding() }
    }

    override suspend fun getBuilding(buildingId: String): Building {
        return api.getBuilding(buildingId).toBuilding()
    }

    override suspend fun getBuildingsBySection(section: String): List<Building> {
        return api.getBuildingsBySection(section).map { it.toBuilding() }
    }
}