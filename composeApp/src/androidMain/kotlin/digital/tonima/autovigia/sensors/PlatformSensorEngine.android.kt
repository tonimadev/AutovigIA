package digital.tonima.autovigia.sensors

import digital.tonima.autovigia.domain.model.IMUData
import digital.tonima.autovigia.domain.model.SensorData
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

actual class PlatformSensorEngine {
    actual fun getEngine(): SensorEngine = AndroidSensorEngine()
}

class AndroidSensorEngine : SensorEngine {
    private var isRunning = false

    override fun startListening(): Flow<SensorData> = flow {
        isRunning = true
        // Aqui entraria a integração com AudioRecord e SensorManager
        // Usando callbackFlow para converter eventos de hardware em Flow
        while (isRunning) {
            emit(
                SensorData(
                    timestamp = System.currentTimeMillis(),
                    audioFFT = FloatArray(128) { 0f }, // Mock
                    accelerometer = IMUData(0f, 0f, 0f),
                    gyroscope = IMUData(0f, 0f, 0f),
                    anomalyScore = (0..100).random() / 100f
                )
            )
            delay(100) // 10Hz para exemplo
        }
    }

    override fun stopListening() {
        isRunning = false
    }

    override fun isListening(): Boolean = isRunning
}
