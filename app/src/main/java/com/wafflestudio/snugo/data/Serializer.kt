package com.wafflestudio.snugo.data

import java.lang.reflect.Type

interface Serializer {
    fun <T : Any> serialize(
        raw: T,
        type: Type,
    ): String

    fun <T : Any> deserialize(
        raw: String,
        type: Type,
    ): T
}
