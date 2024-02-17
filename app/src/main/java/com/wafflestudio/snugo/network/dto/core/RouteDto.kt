package com.wafflestudio.snugo.network.dto.core

import com.squareup.moshi.JsonClass
import com.wafflestudio.snugo.models.Route

@JsonClass(generateAdapter = true)
data class RouteDto(
    val id: String,
    val buildings: List<BuildingDto>,
    val count: Long,
    val avgPathLength: Long,
    val avgTime: Long,
) {
    fun toRoute(): Route = Route(
        id = id,
        buildings = buildings.map { it.toBuilding() },
        count = count,
        averagePathLength = avgPathLength,
        averageTime = avgTime,
    )
}