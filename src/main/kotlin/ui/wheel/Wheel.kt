package ui.wheel

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFontLoader
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.MultiParagraph
import androidx.compose.ui.text.MultiParagraphIntrinsics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@ExperimentalFoundationApi
@ExperimentalComposeUiApi
@Composable
fun Wheel(
    modifier: Modifier = Modifier,
    state: WheelState = rememberWheelState()
) {
    val animScope = rememberCoroutineScope()
    val rotation by state.rotationAnimatable.asState()

    Box(modifier) {
        Surface(
            elevation = 4.dp,
            shape = CircleShape,
            modifier = Modifier
                .aspectRatio(1f)
                .border(Dp.Hairline, Color.Black, CircleShape)
        ) {
            WheelContent(rotation, state.items, state.colors)
        }

        Icon(
            imageVector = Icons.Default.PlayArrow,
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .offset((-9).dp)
        )
    }
}


private const val defaultRotation = 45f

@Composable
private fun WheelContent(
    rotation: Float,
    items: List<String>,
    colors: List<Color>,
    modifier: Modifier = Modifier
) {
    val textStyle = MaterialTheme.typography.h5
    val density = LocalDensity.current
    val fontLoader = LocalFontLoader.current

    val names = remember(items) {
        items.map { item ->
            val maxIntrinsics = MultiParagraphIntrinsics(
                annotatedString = AnnotatedString(item),
                style = textStyle,
                placeholders = emptyList(),
                density = density,
                resourceLoader = fontLoader
            )

            MultiParagraph(
                intrinsics = maxIntrinsics,
                maxLines = 1,
                width = maxIntrinsics.maxIntrinsicWidth
            )
        }
    }


    Canvas(modifier) {
        rotate(defaultRotation) {
            rotate(rotation) {
                drawBackground(colors)
                drawNames(names)
            }
        }
    }
}

private fun DrawScope.drawNames(names: List<MultiParagraph>) {
    val count = names.size
    val stepDegree = 360f / count

    names.forEachIndexed { index, name ->
        rotate(stepDegree * index, center) {
            drawIntoCanvas { canvas ->
                canvas.rotate(defaultRotation)
                canvas.translate(
                    dx = (center.getDistance() / 1.5f) - (name.width / 2),
                    dy = name.height / -2
                )

                name.paint(canvas, Color.Black)
            }
        }
    }
}

private const val DefaultCanvasArcRotation = 90f
private fun DrawScope.drawBackground(colors: List<Color>) {
    val count = colors.size
    val stepDegree = 360f / count

    rotate(-defaultRotation) {
        colors.forEachIndexed { index, color ->
            val startAngle = (stepDegree * index) - (stepDegree / 2) - DefaultCanvasArcRotation
            drawArc(color, startAngle, stepDegree, true)
        }
    }
}