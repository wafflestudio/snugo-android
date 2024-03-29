package com.wafflestudio.snugo.models

data class Route(
    val id: String,
    val buildings: List<Building>,
    val count: Long,
    val averagePathLength: Double,
    val averageTime: Double,
)
