package ui.chat

import SpacingNormal
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.input.TextFieldValue

@Composable
fun ChatControls(
    chatControlsState: ChatControlsState
) {
    val scrollToTopButtonBackground by animateColorAsState(
        (if (chatControlsState.scrollToTop) MaterialTheme.colors.primary else MaterialTheme.colors.surface)
            .copy(alpha = 0.5f)
    )

    Row(verticalAlignment = Alignment.CenterVertically) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Gifter only")
            Checkbox(
                chatControlsState.gifterOnly,
                { chatControlsState.gifterOnly = it }
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Sub only")
            Checkbox(
                chatControlsState.subOnly,
                { chatControlsState.subOnly = it }
            )
        }

        Spacer(Modifier.weight(1f))

        OutlinedTextField(
            value = chatControlsState.filterValue,
            onValueChange = { chatControlsState.filterValue = it },
            placeholder = { Text("Filter messages") },
            trailingIcon = {
                IconButton({ chatControlsState.filterValue = TextFieldValue() }) {
                    Icon(Icons.Default.Clear, null)
                }
            }
        )

        Spacer(Modifier.width(SpacingNormal))

        IconButton(
            onClick = {
                chatControlsState.scrollToTop = !chatControlsState.scrollToTop
            },
            modifier = Modifier.background(
                color = scrollToTopButtonBackground,
                shape = CircleShape
            )
        ) {
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = null,
                modifier = Modifier.rotate(-90f)
            )
        }
    }
}

class ChatControlsState {
    var subOnly by mutableStateOf(true)
    var gifterOnly by mutableStateOf(false)
    var filterValue by mutableStateOf(TextFieldValue())
    var scrollToTop by mutableStateOf(true)
}

@Composable
inline fun rememberChatControlsState() = remember { ChatControlsState() }