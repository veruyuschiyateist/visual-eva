package com.vsial.eva.domain_camera.entity

data class CameraInfo(
    val id: String,
    val label: CameraType
)

enum class CameraType(val displayName: String) {
    WIDE("Wide"),
    ULTRA_WIDE("Ultra-Wide"),
    MACRO("Macro"),
    TELEPHOTO("Telephoto"),
    FRONT("Front")
}