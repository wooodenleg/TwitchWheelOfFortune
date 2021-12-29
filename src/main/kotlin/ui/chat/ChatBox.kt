@file:OptIn(ExperimentalComposeUiApi::class)

package ui.chat

import SpacingSmall
import SpacingXSmall
import SpacingXXSmall
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.ktmi.tmi.messages.TextMessage
import data.Badges
import data.twitchChatAsState
import io.kamel.image.KamelImage
import io.kamel.image.lazyPainterResource
import ui.util.getColorFromHexString
import ui.util.onScroll

@Composable
fun ChatBox(
    modifier: Modifier = Modifier,
    onMessageClick: (TextMessage) -> Unit
) {
    val listState = rememberLazyListState()
    val chat by twitchChatAsState()
    val controlsState = rememberChatControlsState()

    val filteredChat by derivedStateOf {
        chat.filter(
            controlsState.subOnly,
            controlsState.gifterOnly,
            controlsState.filterValue.text
        )
    }

    LaunchedEffect(chat, controlsState.scrollToTop) {
        if (controlsState.scrollToTop && filteredChat.isNotEmpty())
            listState.scrollToItem(filteredChat.size - 1)
    }

    Column(modifier = modifier.onScroll { controlsState.scrollToTop = false }) {

        ChatControls(controlsState)

        Spacer(Modifier.height(SpacingSmall))
        Divider()
        Spacer(Modifier.height(SpacingSmall))

        LazyColumn(
            state = listState,
            reverseLayout = true,
        ) {
            items(filteredChat, { it.messageId }) { message ->
                ChatMessage(
                    message,
                    Modifier
                        .clip(MaterialTheme.shapes.medium)
                        .clickable { onMessageClick(message) }
                        .padding(SpacingSmall)
                )
            }
        }
    }
}


@Composable
private fun ChatMessage(message: TextMessage, modifier: Modifier = Modifier) {
    val badgeUrl = message.badges?.get("subscriber")?.let { key ->
        Badges.badges?.get(key)
    }?.image_url_2x

    val resource = lazyPainterResource(badgeUrl ?: "")
    Row(modifier) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            KamelImage(
                resource = resource,
                contentDescription = "",
                modifier = Modifier.size(BadgeIconSize)
            )

            Spacer(Modifier.width(SpacingXSmall))

            Text(
                text = "${message.displayName ?: message.username}: ",
                style = MaterialTheme.typography.h6,
                color = message.color?.let(::getColorFromHexString) ?: Color.Unspecified,
            )
        }

        Text(
            message.message,
            style = MaterialTheme.typography.body1,
            modifier = Modifier.padding(start = SpacingXXSmall, top = SpacingXXSmall)
        )
    }
}

private fun List<TextMessage>.filter(subOnly: Boolean, gifterOnly: Boolean, filter: String) =
    asSequence().filter {
        if (!subOnly) true
        else {
            val subLength = it.badges?.get("subscriber")?.toInt() ?: 0
            subLength >= 1
        }
    }.filter {
        if (!gifterOnly) true
        else {
            val subGifter = it.badges?.get("sub-gifter")?.toInt() ?: 0
            subGifter >= 1
        }
    }.filter {
        it.message.lowercase().contains(filter.lowercase())
    }.toList()


private val BadgeIconSize = 24.dp