package digital.tonima.autovigia.sensors

import kotlinx.coroutines.flow.StateFlow

enum class PermissionType {
    AUDIO,
    LOCATION,
    SENSORS
}

enum class PermissionStatus {
    GRANTED,
    DENIED,
    NOT_DETERMINED
}

interface PermissionManager {
    val permissionStatuses: StateFlow<Map<PermissionType, PermissionStatus>>
    fun checkPermissions()
    fun requestPermission(type: PermissionType)
}

expect fun providePermissionManager(): PermissionManager