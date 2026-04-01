package digital.tonima.autovigia.domain.usecase

import digital.tonima.autovigia.domain.model.AppState
import digital.tonima.autovigia.domain.model.Vehicle
import digital.tonima.autovigia.domain.model.SensorData
import digital.tonima.autovigia.domain.repository.VehicleRepository
import digital.tonima.autovigia.sensors.SensorEngine
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class VehicleStateMachine(
    private val sensorEngine: SensorEngine,
    private val repository: VehicleRepository,
    private val scope: CoroutineScope
) {
    private val _currentState = MutableStateFlow(AppState.ONBOARDING)
    val currentState: StateFlow<AppState> = _currentState.asStateFlow()

    init {
        scope.launch {
            repository.getVehicle().collect { vehicle ->
                if (vehicle != null && _currentState.value == AppState.ONBOARDING) {
                    transitionTo(AppState.PERMISSIONS)
                }
            }
        }
    }

    private val _calibrationProgress = MutableStateFlow(0f) // 0.0 to 1.0
    val calibrationProgress: StateFlow<Float> = _calibrationProgress.asStateFlow()

    fun completeOnboarding(vehicle: Vehicle) {
        // Salvar veículo via repositório
        transitionTo(AppState.PERMISSIONS)
    }

    fun onPermissionsGranted() {
        transitionTo(AppState.CALIBRATING)
    }

    private fun transitionTo(newState: AppState) {
        _currentState.value = newState
        when (newState) {
            AppState.PERMISSIONS -> {} // UI should react and show permission screen
            AppState.CALIBRATING -> startCalibration()
            AppState.MONITORING -> startMonitoring()
            else -> {}
        }
    }

    private fun startCalibration() {
        scope.launch {
            sensorEngine.startListening()
                .onEach { data ->
                    // Lógica de treinamento local (Edge AI)
                    updateCalibrationProgress(data)
                }
                .collect()
        }
    }

    private fun startMonitoring() {
        scope.launch {
            sensorEngine.startListening()
                .onEach { data ->
                    // Lógica de inferência em tempo real
                    if (data.anomalyScore > 0.8f) {
                        notifyAnomaly(data)
                    }
                }
                .collect()
        }
    }

    private fun updateCalibrationProgress(data: SensorData) {
        // Simulação: incrementa progresso até 100%
        val newProgress = _calibrationProgress.value + 0.001f
        _calibrationProgress.value = newProgress.coerceAtMost(1.0f)

        if (newProgress >= 1.0f) {
            transitionTo(AppState.MONITORING)
        }
    }

    private fun notifyAnomaly(data: SensorData) {
        // Enviar para backend via Ktor e disparar Gamification
        println("Anomalia detectada: ${data.anomalyScore}")
    }
}
