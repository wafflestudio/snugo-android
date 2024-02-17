package com.wafflestudio.snugo.repository

import com.wafflestudio.snugo.models.Building

interface BuildingsRepository {
    suspend fun getBuildings(): List<Building>

    suspend fun getBuilding(buildingId: String): Building

    suspend fun getBuildingsBySection(section: String): List<Building>
}