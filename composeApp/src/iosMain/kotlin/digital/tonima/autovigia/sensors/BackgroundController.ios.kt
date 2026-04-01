package digital.tonima.autovigia.sensors

import platform.AVFAudio.AVAudioSession
import platform.AVFAudio.AVAudioSessionCategoryPlayAndRecord
import platform.AVFAudio.AVAudioSessionModeMeasurement
import platform.AVFAudio.setActive
import platform.Foundation.NSError
import kotlinx.cinterop.ExperimentalForeignApi

actual class PlatformBackgroundController : BackgroundController {
    private var isServiceRunning = false

    @OptIn(ExperimentalForeignApi::class)
    override fun start() {
        val session = AVAudioSession.sharedInstance()
        session.setCategory(AVAudioSessionCategoryPlayAndRecord, error = null)
        session.setMode(AVAudioSessionModeMeasurement, error = null)
        session.setActive(true, error = null)
        isServiceRunning = true
    }

    @OptIn(ExperimentalForeignApi::class)
    override fun stop() {
        val session = AVAudioSession.sharedInstance()
        session.setActive(false, error = null)
        isServiceRunning = false
    }

    override fun isRunning(): Boolean = isServiceRunning
}
