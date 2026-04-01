package digital.tonima.autovigia.sensors

import android.content.Context
import android.content.Intent
import android.os.Build
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

actual class PlatformBackgroundController : BackgroundController, KoinComponent {
    private val context: Context by inject()

    override fun start() {
        val intent = Intent(context, DetectionService::class.java).apply {
            action = DetectionService.ACTION_START
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent)
        } else {
            context.startService(intent)
        }
    }

    override fun stop() {
        val intent = Intent(context, DetectionService::class.java).apply {
            action = DetectionService.ACTION_STOP
        }
        context.startService(intent)
    }

    override fun isRunning(): Boolean {
        return DetectionService.isRunning
    }
}
