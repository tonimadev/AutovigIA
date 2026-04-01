package digital.tonima.autovigia.domain.usecase

import digital.tonima.autovigia.domain.model.SensorData
import digital.tonima.autovigia.sensors.SensorEngine
import digital.tonima.autovigia.sensors.BackgroundController
import digital.tonima.autovigia.domain.model.AppState
import digital.tonima.autovigia.domain.repository.VehicleRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlin.math.sqrt

class DetectionManager(
    private val sensorEngine: SensorEngine,
    private val backgroundController: BackgroundController,
    private val repository: VehicleRepository,
    private val scope: CoroutineScope
) {
    private val _audioAmplitude = MutableStateFlow(0f)
    val audioAmplitude: StateFlow<Float> = _audioAmplitude.asStateFlow()

    private val _vibrationMagnitude = MutableStateFlow(0f)
    val vibrationMagnitude: StateFlow<Float> = _vibrationMagnitude.asStateFlow()

    private val _appState = MutableStateFlow(AppState.INACTIVE)
    val appState: StateFlow<AppState> = _appState.asStateFlow()

    private val _calibrationProgress = MutableStateFlow(0f)
    val calibrationProgress: StateFlow<Float> = _calibrationProgress.asStateFlow()

    private var monitoringJob: Job? = null
    private var anomalyCount = 0
    private var healthyCount = 0

    val vehicle = repository.getVehicle()
        .stateIn(scope, SharingStarted.WhileSubscribed(5000), null)

    init {
        scope.launch {
            vehicle.filterNotNull().collect { v ->
                if (v.autoStartEnabled && !backgroundController.isRunning()) {
                    startMonitoring()
                }
            }
        }
    }

    fun toggleAutoStart(enabled: Boolean) {
        scope.launch {
            val currentVehicle = vehicle.value ?: return@launch
            repository.saveVehicle(currentVehicle.copy(autoStartEnabled = enabled))
        }
    }

    fun startMonitoring() {
        if (monitoringJob?.isActive == true) return
        
        monitoringJob = scope.launch {
            _calibrationProgress.value = 0f
            anomalyCount = 0
            healthyCount = 0
            _appState.value = AppState.CALIBRATING
            sensorEngine.startListening().collect { data ->
                processSensorData(data)
            }
        }
        backgroundController.start()
    }

    fun stopMonitoring() {
        monitoringJob?.cancel()
        monitoringJob = null
        sensorEngine.stopListening()
        backgroundController.stop()
        _appState.value = AppState.INACTIVE
    }

    private fun processSensorData(data: SensorData) {
        // Cálculo simplificado de amplitude/magnitude a partir dos sensores
        val audioRMS = if (data.audioFFT.isNotEmpty()) {
            sqrt(data.audioFFT.map { it * it }.average().toFloat())
        } else 0f
        _audioAmplitude.value = audioRMS

        val vibMag = sqrt(
            data.accelerometer.x * data.accelerometer.x +
            data.accelerometer.y * data.accelerometer.y +
            data.accelerometer.z * data.accelerometer.z
        )
        _vibrationMagnitude.value = vibMag
        
        if (_appState.value == AppState.CALIBRATING) {
            updateCalibrationProgress(data)
        } else {
            // Atualiza o estado com Histerese para evitar flickering
            if (data.anomalyScore > 0.8f) {
                anomalyCount++
                healthyCount = 0
                if (anomalyCount >= HYSTERESIS_THRESHOLD) {
                    _appState.value = AppState.ANOMALY
                }
            } else {
                healthyCount++
                anomalyCount = 0
                if (healthyCount >= HYSTERESIS_THRESHOLD && _appState.value == AppState.ANOMALY) {
                    _appState.value = AppState.MONITORING
                }
            }
        }
    }

    private fun updateCalibrationProgress(data: SensorData) {
        val newProgress = _calibrationProgress.value + 0.01f
        _calibrationProgress.value = newProgress.coerceAtMost(1.0f)

        if (newProgress >= 1.0f) {
            _appState.value = AppState.MONITORING
        }
    }

    companion object {
        private const val HYSTERESIS_THRESHOLD = 5 // Precisa de 5 samples para mudar
    }
}
