package data

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.request.*
import kotlinx.serialization.Serializable

object Badges {
    private val client = HttpClient(CIO) {
        install(JsonFeature) {
            serializer = KotlinxSerializer(
                kotlinx.serialization.json.Json {
                    ignoreUnknownKeys = true
                }
            )
        }
    }

    var badges: Map<String, Badge>? = null
        private set

    suspend fun init() {
        badges = getBadges()
    }

    suspend fun getBadges() = client
        .get<BadgesResponse>("https://badges.twitch.tv/v1/badges/channels/36620767/display")
        .badge_sets.subscriber.versions
}

@Serializable
private data class BadgesResponse(
    val badge_sets: BadgeSets
)

@Serializable
private data class BadgeSets(
    val subscriber: Subscriber
)

@Serializable
private data class Subscriber(
    val versions: Map<String, Badge>
)

@Serializable
data class Badge(
    val click_action: String,
    val click_url: String,
    val description: String,
    val image_url_1x: String,
    val image_url_2x: String,
    val image_url_4x: String,
    val title: String
)