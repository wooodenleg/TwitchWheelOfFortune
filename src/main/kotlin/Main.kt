import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import data.Badges
import io.kamel.core.config.KamelConfig
import io.kamel.core.config.takeFrom
import io.kamel.image.config.Default
import io.kamel.image.config.LocalKamelConfig
import io.kamel.image.config.resourcesFetcher

@Composable
@Preview
fun App() {
    val desktopConfig = remember {
        KamelConfig {
            takeFrom(KamelConfig.Default)
            resourcesFetcher() // Available only on Desktop.
        }
    }

    CustomTheme {
        CompositionLocalProvider(LocalKamelConfig provides desktopConfig) {
            Surface(
                color = MaterialTheme.colors.background,
                modifier = Modifier.fillMaxSize()
            ) {
                WheelScreen()
            }
        }
    }
}

fun main() = application {

    LaunchedEffect(Unit) {
        Badges.init()
    }

    Window(
        title = "Vojtova Točka",
        icon = painterResource("agrE.png"),
        onCloseRequest = ::exitApplication,
        state = rememberWindowState(size = DpSize(1600.dp, 1200.dp))
    ) {
        App()
    }
}