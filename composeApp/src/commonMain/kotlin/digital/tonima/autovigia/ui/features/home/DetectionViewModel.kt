package digital.tonima.autovigia.ui.features.home

import androidx.lifecycle.ViewModel
import digital.tonima.autovigia.domain.usecase.DetectionManager
import digital.tonima.autovigia.domain.model.AppState

class DetectionViewModel(
    private val detectionManager: DetectionManager
) : ViewModel() {
    val appState = detectionManager.appState
    val audioAmplitude = detectionManager.audioAmplitude
    val vibrationMagnitude = detectionManager.vibrationMagnitude
    val vehicle = detectionManager.vehicle
    val calibrationProgress = detectionManager.calibrationProgress

    fun toggleMonitoring() {
        if (appState.value == AppState.INACTIVE) {
            detectionManager.startMonitoring()
        } else {
            detectionManager.stopMonitoring()
        }
    }

    fun toggleAutoStart(enabled: Boolean) {
        detectionManager.toggleAutoStart(enabled)
    }
}
