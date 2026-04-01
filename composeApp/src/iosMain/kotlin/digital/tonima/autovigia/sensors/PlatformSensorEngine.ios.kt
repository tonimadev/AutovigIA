package digital.tonima.autovigia.sensors

import digital.tonima.autovigia.domain.model.IMUData
import digital.tonima.autovigia.domain.model.SensorData
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import platform.Foundation.NSDate
import platform.Foundation.timeIntervalSince1970

actual class PlatformSensorEngine {
    actual fun getEngine(): SensorEngine = IosSensorEngine()
}

class IosSensorEngine : SensorEngine {
    private var isRunning = false

    override fun startListening(): Flow<SensorData> = flow {
        isRunning = true
        // Aqui entraria a integração com CoreMotion e AVAudioEngine
        while (isRunning) {
            emit(
                SensorData(
                    timestamp = (NSDate().timeIntervalSince1970 * 1000).toLong(),
                    audioFFT = FloatArray(128) { 0f },
                    accelerometer = IMUData(0f, 0f, 0f),
                    gyroscope = IMUData(0f, 0f, 0f),
                    anomalyScore = (0..100).random() / 100f
                )
            )
            delay(100)
        }
    }

    override fun stopListening() {
        isRunning = false
    }

    override fun isListening(): Boolean = isRunning
}
