import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Shapes
import androidx.compose.material.darkColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun CustomTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colors = darkColors(
            primary = Color(0xFFBE171E),
            primaryVariant = Color(0xFFB83D43),
            secondary = Color(0xFF9147FF),
            secondaryVariant = Color(0xFF9B45D0),
            background = Color(0xFF181925),
            surface = Color(0xFF282A3B)
        ),
        shapes = Shapes(
            medium = RoundedCornerShape(RadiusNormal),
            large = RoundedCornerShape(RadiusLarge)
        ),
        content = content
    )
}

val SpacingXXSmall = 2.dp
val SpacingXSmall = 4.dp
val SpacingSmall = 8.dp
val SpacingNormal = 16.dp
val SpacingLarge = 24.dp
val SpacingXLarge = 32.dp
val SpacingXXLarge = 48.dp

val RadiusNormal = 8.dp
val RadiusLarge = 16.dp