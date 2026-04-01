package digital.tonima.autovigia.ui.features.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import digital.tonima.autovigia.domain.model.Vehicle
import digital.tonima.autovigia.domain.model.VehicleStatus
import digital.tonima.autovigia.domain.repository.VehicleRepository
import digital.tonima.autovigia.domain.usecase.VehicleStateMachine
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class OnboardingState(
    val brand: String = "",
    val model: String = "",
    val year: String = "",
    val odometer: String = "",
    val hasIssues: Boolean = false,
    val isLoading: Boolean = false
)

sealed interface OnboardingIntent {
    data class BrandChanged(val value: String) : OnboardingIntent
    data class ModelChanged(val value: String) : OnboardingIntent
    data class YearChanged(val value: String) : OnboardingIntent
    data class OdometerChanged(val value: String) : OnboardingIntent
    data class HasIssuesChanged(val value: Boolean) : OnboardingIntent
    object SaveVehicle : OnboardingIntent
}

class OnboardingViewModel(
    private val repository: VehicleRepository,
    private val stateMachine: VehicleStateMachine
) : ViewModel() {

    private val _uiState = MutableStateFlow(OnboardingState())
    val uiState = _uiState.asStateFlow()

    fun onIntent(intent: OnboardingIntent) {
        when (intent) {
            is OnboardingIntent.BrandChanged -> _uiState.update { it.copy(brand = intent.value) }
            is OnboardingIntent.ModelChanged -> _uiState.update { it.copy(model = intent.value) }
            is OnboardingIntent.YearChanged -> _uiState.update { it.copy(year = intent.value) }
            is OnboardingIntent.OdometerChanged -> _uiState.update { it.copy(odometer = intent.value) }
            is OnboardingIntent.HasIssuesChanged -> _uiState.update { it.copy(hasIssues = intent.value) }
            OnboardingIntent.SaveVehicle -> saveVehicle()
        }
    }

    private fun saveVehicle() {
        val state = _uiState.value
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val vehicle = Vehicle(
                brand = state.brand,
                model = state.model,
                year = state.year.toIntOrNull() ?: 2024,
                engine = "Standard",
                odometer = state.odometer.toLongOrNull() ?: 0,
                initialStatus = if (state.hasIssues) VehicleStatus.ISSUES else VehicleStatus.HEALTHY
            )
            repository.saveVehicle(vehicle)
            _uiState.update { it.copy(isLoading = false) }
        }
    }
}
