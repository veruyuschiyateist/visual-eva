package com.vsial.eva.data_photos.mappers

import android.graphics.ColorMatrix
import com.vsial.eva.domain_photos.entities.ImageFilterType

fun ImageFilterType.toColorMatrix(): ColorMatrix? {
    return when (this) {
        ImageFilterType.NONE -> null
        ImageFilterType.BLACK_WHITE -> ColorMatrix().apply { setSaturation(0f) }
        ImageFilterType.WARM -> ColorMatrix().apply { setSaturation(0.7f) }
        ImageFilterType.FILM -> ColorMatrix(
            floatArrayOf(
                1.1f, -0.05f, 0f, 0f, -10f,
                -0.05f, 1.05f, 0f, 0f, -10f,
                0f, 0f, 1.1f, 0f, -10f,
                0f, 0f, 0f, 1f, 0f
            )
        )

        ImageFilterType.CINEMATIC -> ColorMatrix(
            floatArrayOf(
                1.2f, -0.2f, 0f, 0f, 0f,
                -0.1f, 1.1f, 0.1f, 0f, 0f,
                0f, -0.1f, 1.3f, 0f, 0f,
                0f, 0f, 0f, 1f, 0f
            )
        )
            
        ImageFilterType.SEPIA -> ColorMatrix(
            floatArrayOf(
                0.393f, 0.769f, 0.189f, 0f, 0f,
                0.349f, 0.686f, 0.168f, 0f, 0f,
                0.272f, 0.534f, 0.131f, 0f, 0f,
                0f, 0f, 0f, 1f, 0f
            )
        )

        ImageFilterType.COOL -> ColorMatrix(
            floatArrayOf(
                0.9f, 0f, 0f, 0f, 0f,
                0f, 1f, 0f, 0f, 0f,
                0f, 0f, 1.2f, 0f, 0f,
                0f, 0f, 0f, 1f, 0f
            )
        )

        ImageFilterType.FADE -> ColorMatrix(
            floatArrayOf(
                1f, 0f, 0f, 0f, 20f,
                0f, 1f, 0f, 0f, 20f,
                0f, 0f, 1f, 0f, 20f,
                0f, 0f, 0f, 1f, 0f
            )
        )

        ImageFilterType.VIBRANT -> ColorMatrix(
            floatArrayOf(
                1.2f, 0f, 0f, 0f, 10f,
                0f, 1.2f, 0f, 0f, 10f,
                0f, 0f, 1.2f, 0f, 10f,
                0f, 0f, 0f, 1f, 0f
            )
        )
    }
}