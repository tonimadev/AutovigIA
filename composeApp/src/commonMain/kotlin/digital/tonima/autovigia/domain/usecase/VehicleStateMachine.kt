package digital.tonima.autovigia.domain.usecase

import digital.tonima.autovigia.domain.model.AppState
import digital.tonima.autovigia.domain.model.Vehicle
import digital.tonima.autovigia.domain.repository.VehicleRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class VehicleStateMachine(
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

    fun completeOnboarding(vehicle: Vehicle) {
        scope.launch {
            repository.saveVehicle(vehicle)
            // transitionTo(AppState.PERMISSIONS) // Já é feito pelo collect no init
        }
    }

    fun onPermissionsGranted() {
        transitionTo(AppState.INACTIVE)
    }

    private fun transitionTo(newState: AppState) {
        _currentState.value = newState
    }
}
