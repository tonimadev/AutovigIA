package digital.tonima.autovigia.domain.model

data class Vehicle(
    val brand: String,
    val model: String,
    val year: Int,
    val engine: String,
    val odometer: Long,
    val initialStatus: VehicleStatus = VehicleStatus.HEALTHY,
    val autoStartEnabled: Boolean = false
)

enum class VehicleStatus {
    HEALTHY,
    ISSUES
}

enum class AppState {
    ONBOARDING,
    PERMISSIONS,
    CALIBRATING,
    MONITORING,
    INACTIVE,
    ANOMALY
}
