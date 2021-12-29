@file:OptIn(ExperimentalFoundationApi::class, ExperimentalComposeUiApi::class)

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import ui.chat.ChatBox
import ui.wheel.SoundLooper
import ui.wheel.Wheel
import ui.wheel.rememberWheelState

@Composable
fun WheelScreen() {
    val wheelState = rememberWheelState()
    val selectedItem by wheelState.currentSelectedItem
    val animationScope = rememberCoroutineScope()
    var winnerOverlayVisible by remember { mutableStateOf(false) }

    SoundLooper("sounds/click.wav", wheelState.currentSelectedItem.value)

    Box {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(SpacingNormal)
        ) {

            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource("agrE.png"),
                    contentDescription = null,
                    modifier = Modifier.size(TitleIconSize).clip(CircleShape)
                )

                Spacer(Modifier.width(SpacingNormal))

                Text("Vojtíkovo kolo štěstí", style = MaterialTheme.typography.h3)
            }

            Spacer(Modifier.height(SpacingXXLarge))

            Row(Modifier.height(MainContentHeight).fillMaxWidth()) {
                Wheel(
                    state = wheelState,
                    modifier = Modifier
                        .padding(start = SpacingNormal)
                        .width(WheelSize)
                        .fillMaxHeight()
                        .clip(CircleShape)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                        ) {
                            animationScope.launch {
                                wheelState.spin()
                                winnerOverlayVisible = true
                            }
                        }
                )

                Card(
                    border = BorderStroke(Dp.Hairline, MaterialTheme.colors.onSurface.copy(0.3f)),
                    shape = MaterialTheme.shapes.large,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .padding(horizontal = SpacingXLarge)
                ) {
                    ChatBox(Modifier.padding(SpacingNormal)) {
                        wheelState.setWheelItems(wheelState.items + (it.displayName ?: it.username))
                    }
                }
            }

            Spacer(Modifier.height(SpacingXXLarge))

            ItemEditor(
                items = wheelState.items,
                onItemsChanged = { wheelState.setWheelItems(it) },
                modifier = Modifier.width(WheelSize)
            )
        }

        AnimatedVisibility(
            visible = winnerOverlayVisible,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Box(
                Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colors.background.copy(alpha = 0.8f))
                    .clickable(remember { MutableInteractionSource() }, null) { winnerOverlayVisible = false }
            ) {
                Text(
                    text = selectedItem ?: "???",
                    style = MaterialTheme.typography.h2,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}

private val WheelSize = 650.dp
private val MainContentHeight = 600.dp
private val TitleIconSize = 64.dp
