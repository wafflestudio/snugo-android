package com.wafflestudio.snugo.location

import com.naver.maps.geometry.LatLng
import com.naver.maps.geometry.LatLngBounds

object MapConstants {
    val SNU_BOUNDARY =
        LatLngBounds(
            LatLng(37.44353826994081, 126.94341358078691),
            LatLng(37.47154628496232, 126.96383505009055),
        )
}
