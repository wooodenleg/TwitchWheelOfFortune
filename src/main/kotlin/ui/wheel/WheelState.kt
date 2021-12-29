package ui.wheel

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.spring
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import ui.util.randomColor
import kotlin.random.Random

@Composable
fun rememberWheelState(items: List<String> = listOf("Agraelus")) = remember { WheelState(items) }

class WheelState(
    wheelItems: List<String>
) {
    var items by mutableStateOf(wheelItems)
        private set
    val colors by derivedStateOf {
        List(items.size) {
            defaultColors.elementAtOrNull(it) ?: randomColor()
        }
    }

    internal val rotationAnimatable = Animatable(DefaultWheelTurn)

    val rotation by rotationAnimatable.asState()
    val normalizedRotation by derivedStateOf { rotation % 360f }

    val isAnimationRunning get() = rotationAnimatable.isRunning


    fun setWheelItems(newItems: List<String>) {
        items = newItems.toSet().toList()
    }

    val currentSelectedItem = derivedStateOf {
        val itemRadius = 360f / items.count()

        items.filterIndexed { index, _ ->
            val startAngle = ((itemRadius * index) - (itemRadius / 2) + rotation) % 360
            val wrappedStartAngle = if (startAngle < 0) 360f + startAngle else startAngle
            val endAngle = wrappedStartAngle + itemRadius

            DefaultWheelMarkDegree in wrappedStartAngle..endAngle
        }.firstOrNull()
    }

    suspend fun spin() {
        val baseFullRotation = (360f * Random.nextInt(8, 12))
        val randomRotation = Random.nextInt(0, 360)
        val targetRotation = rotationAnimatable.targetValue + baseFullRotation + randomRotation

        rotationAnimatable.animateTo(
            targetValue = targetRotation,
            animationSpec = spring(stiffness = 3f, visibilityThreshold = 0.1f)
        )
    }
}

private const val DefaultWheelMarkDegree = 270f
private const val DefaultWheelTurn = 270f

private val defaultColors = listOf(
    Color(0xFFF94144),
    Color(0xFFF3722C),
    Color(0xFFF8961E),
    Color(0xFFF9844A),
    Color(0xFFF9C74F),
    Color(0xFF90BE6D),
    Color(0xFF43AA8B),
    Color(0xFF4D908E),
    Color(0xFF577590),
    Color(0xFF277DA1),
    Color(0xFF2D6099)
)