package ui.wheel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.res.useResource
import java.io.BufferedInputStream
import javax.sound.sampled.AudioSystem

@Composable
fun SoundLooper(path: String, key: Any?) {
    val sound = remember(path) {
        val clip = AudioSystem.getClip()
        useResource(path) { stream ->
            clip.open(
                AudioSystem.getAudioInputStream(
                    BufferedInputStream(stream)
                )
            )
        }
        clip.framePosition = clip.frameLength - 1 // move to end
        clip
    }

    DisposableEffect(key) {
        if (!sound.isRunning)
            sound.loop(0)

        onDispose {
            sound.stop()
            sound.framePosition = sound.frameLength / 5
        }
    }
}