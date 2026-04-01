package digital.tonima.autovigia.sensors

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import digital.tonima.autovigia.R
import androidx.core.app.NotificationCompat
import digital.tonima.autovigia.MainActivity
import digital.tonima.autovigia.domain.usecase.DetectionManager
import org.koin.android.ext.android.inject

class DetectionService : Service() {
    private val detectionManager: DetectionManager by inject()

    companion object {
        const val CHANNEL_ID = "detection_channel"
        const val NOTIFICATION_ID = 1
        const val ACTION_START = "ACTION_START"
        const val ACTION_STOP = "ACTION_STOP"
        var isRunning = false
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START -> startForegroundService()
            ACTION_STOP -> stopDetectionService()
        }
        return START_STICKY
    }

    private fun startForegroundService() {
        isRunning = true
        detectionManager.startMonitoring()
        
        val notification = buildNotification()
        startForeground(NOTIFICATION_ID, notification)
    }

    private fun stopDetectionService() {
        isRunning = false
        detectionManager.stopMonitoring()
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    private fun buildNotification(): Notification {
        val stopIntent = Intent(this, DetectionService::class.java).apply {
            action = ACTION_STOP
        }
        val stopPendingIntent = PendingIntent.getService(
            this, 0, stopIntent, PendingIntent.FLAG_IMMUTABLE
        )

        val mainActivityIntent = Intent(this, MainActivity::class.java)
        val mainActivityPendingIntent = PendingIntent.getActivity(
            this, 0, mainActivityIntent, PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("AutoVigIA Ativo")
            .setContentText("Monitoramento em tempo real em execução.")
            .setSmallIcon(R.drawable.ic_notification)
            .setContentIntent(mainActivityPendingIntent)
            .addAction(R.drawable.ic_stop, "Parar", stopPendingIntent)
            .setOngoing(true)
            .build()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID, "AutoVigIA Monitor", NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
