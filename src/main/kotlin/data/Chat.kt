package data

import ChatToken
import androidx.compose.runtime.*
import com.ktmi.tmi.client.TmiClient
import com.ktmi.tmi.commands.join
import com.ktmi.tmi.dsl.builder.scopes.MainScope
import com.ktmi.tmi.dsl.builder.scopes.channel
import com.ktmi.tmi.events.onConnected
import com.ktmi.tmi.events.onMessage
import com.ktmi.tmi.messages.TextMessage

private const val DefaultChannelName = "agraelus"
private const val DefaultMaxChatSize = 200

@Composable
fun twitchChatAsState(
    channel: String = DefaultChannelName,
    maxChatSize: Int = DefaultMaxChatSize
): State<List<TextMessage>> {
    val chat = remember { mutableStateOf(emptyList<TextMessage>()) }
    val mainChatScope = remember {
        val client = TmiClient(ChatToken)
        MainScope(client)
    }


    DisposableEffect(Unit) {
        with(mainChatScope) {
            connect()
            onConnected { join(channel) }

            channel(channel) {
                onMessage {
                    chat.value = (chat.value + message).takeLast(200)
                }
            }
        }

        onDispose {
            mainChatScope.disconnect()
        }
    }

    return chat
}