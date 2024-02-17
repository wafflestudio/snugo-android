package com.wafflestudio.snugo.network.dto.core

import com.squareup.moshi.JsonClass
import com.wafflestudio.snugo.models.Building
import com.wafflestudio.snugo.models.Section

@JsonClass(generateAdapter = true)
data class BuildingDto(
    val id: String,
    val name: String,
    val location: LocationDto,
    val section: String,
) {
    fun toBuilding(): Building =
        Building(
            id = id,
            name = name,
            location = location.toLatLng(),
            section = Section.valueOf(section),
        )
}
