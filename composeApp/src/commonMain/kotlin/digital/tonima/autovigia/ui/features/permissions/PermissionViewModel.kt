package digital.tonima.autovigia.ui.features.permissions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import digital.tonima.autovigia.sensors.PermissionManager
import digital.tonima.autovigia.sensors.PermissionStatus
import digital.tonima.autovigia.sensors.PermissionType
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class PermissionState(
    val permissions: Map<PermissionType, PermissionStatus> = emptyMap(),
    val allGranted: Boolean = false
)

sealed interface PermissionIntent {
    data object CheckPermissions : PermissionIntent
    data class RequestPermission(val type: PermissionType) : PermissionIntent
}

class PermissionViewModel(
    private val permissionManager: PermissionManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(PermissionState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            permissionManager.permissionStatuses.collect { statuses ->
                _uiState.update { state ->
                    state.copy(
                        permissions = statuses,
                        allGranted = statuses.values.all { it == PermissionStatus.GRANTED }
                    )
                }
            }
        }
        onIntent(PermissionIntent.CheckPermissions)
    }

    fun onIntent(intent: PermissionIntent) {
        when (intent) {
            PermissionIntent.CheckPermissions -> permissionManager.checkPermissions()
            is PermissionIntent.RequestPermission -> permissionManager.requestPermission(intent.type)
        }
    }
}
