package com.vsial.eva.visualeva.ui.mappers

import androidx.compose.ui.graphics.ColorMatrix

enum class UiImageFilterType(val label: String, val matrix: ColorMatrix?) {
    NONE("Original", null),
    BLACK_WHITE("B/W", ColorMatrix().apply { setToSaturation(0f) }),
    WARM("Warm", ColorMatrix().apply { setToSaturation(0.7f) }),
    FILM(
        "Film", ColorMatrix(
            floatArrayOf(
                1.1f, -0.05f, 0f, 0f, -10f,
                -0.05f, 1.05f, 0f, 0f, -10f,
                0f, 0f, 1.1f, 0f, -10f,
                0f, 0f, 0f, 1f, 0f
            )
        )
    ),
    CINEMATIC(
        "Cinematic", ColorMatrix(
            floatArrayOf(
                1.2f, -0.2f, 0f, 0f, 0f,
                -0.1f, 1.1f, 0.1f, 0f, 0f,
                0f, -0.1f, 1.3f, 0f, 0f,
                0f, 0f, 0f, 1f, 0f
            )
        )
    ),
    SEPIA(
        "Sepia", ColorMatrix(
            floatArrayOf(
                0.393f, 0.769f, 0.189f, 0f, 0f,
                0.349f, 0.686f, 0.168f, 0f, 0f,
                0.272f, 0.534f, 0.131f, 0f, 0f,
                0f, 0f, 0f, 1f, 0f
            )
        )
    ),
    COOL(
        "Cool", ColorMatrix(
            floatArrayOf(
                0.9f, 0f, 0f, 0f, 0f,
                0f, 1f, 0f, 0f, 0f,
                0f, 0f, 1.2f, 0f, 0f,
                0f, 0f, 0f, 1f, 0f
            )
        )
    ),
    FADE(
        "Fade", ColorMatrix(
            floatArrayOf(
                1f, 0f, 0f, 0f, 20f,
                0f, 1f, 0f, 0f, 20f,
                0f, 0f, 1f, 0f, 20f,
                0f, 0f, 0f, 1f, 0f
            )
        )
    ),
    VIBRANT(
        "Vibrant", ColorMatrix(
            floatArrayOf(
                1.2f, 0f, 0f, 0f, 10f,
                0f, 1.2f, 0f, 0f, 10f,
                0f, 0f, 1.2f, 0f, 10f,
                0f, 0f, 0f, 1f, 0f
            )
        )
    )
}
