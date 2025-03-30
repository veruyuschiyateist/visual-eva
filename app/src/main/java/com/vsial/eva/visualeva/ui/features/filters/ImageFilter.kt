package com.vsial.eva.visualeva.ui.features.filters

import androidx.compose.ui.graphics.ColorMatrix

enum class ImageFilter(val label: String, val matrix: ColorMatrix?) {
    None("None", null),
    Cinematic(
        "Cinematic", ColorMatrix(
            floatArrayOf(
                1.2f, 0f, 0f, 0f, 0f,
                0f, 1.1f, 0f, 0f, 0f,
                0f, 0f, 0.9f, 0f, 0f,
                0f, 0f, 0f, 1f, 0f
            )
        )
    ),
    Warm("Film", ColorMatrix().apply { setToSaturation(0.8f) }),
    Kodak(
        "SL Kodak", ColorMatrix(
            floatArrayOf(
                1.3f, 0.1f, 0f, 0f, 0f,
                0f, 1.1f, 0.1f, 0f, 0f,
                0f, 0f, 0.8f, 0f, 0f,
                0f, 0f, 0f, 1f, 0f
            )
        )
    ),
    BnW("SL B/W", ColorMatrix().apply { setToSaturation(0f) })
}