package ui.util

import androidx.compose.ui.graphics.Color
import kotlin.random.Random

fun getColorFromHexString(colorString: String): Color? =
    if (colorString.isBlank()) null
    else Color(colorString.removePrefix("#").toInt(16))
        .copy(alpha = 1f)

fun randomColor() = Color(
    Random.nextInt(0, 255),
    Random.nextInt(0, 255),
    Random.nextInt(0, 255),
)
