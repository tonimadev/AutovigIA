package digital.tonima.autovigia.sensors

import digital.tonima.autovigia.domain.model.SensorData
import kotlinx.coroutines.flow.Flow

interface SensorEngine {
    fun startListening(): Flow<SensorData>
    fun stopListening()
    fun isListening(): Boolean
}

// Para seguir o pedido de 'expect', vamos definir uma factory ou provider
expect class PlatformSensorEngine() {
    fun getEngine(): SensorEngine
}
