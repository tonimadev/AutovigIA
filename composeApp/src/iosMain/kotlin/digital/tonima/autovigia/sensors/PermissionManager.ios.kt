package digital.tonima.autovigia.sensors

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import platform.AVFoundation.*
import platform.AVFAudio.*
import platform.CoreLocation.*
import platform.CoreMotion.*
import platform.Foundation.*
import platform.darwin.NSObject

class IosPermissionManager : PermissionManager {
    private val _permissionStatuses = MutableStateFlow<Map<PermissionType, PermissionStatus>>(
        PermissionType.entries.associateWith { PermissionStatus.NOT_DETERMINED }
    )
    override val permissionStatuses: StateFlow<Map<PermissionType, PermissionStatus>> = _permissionStatuses.asStateFlow()

    private val locationManager = CLLocationManager()
    
    private val locationDelegate = object : NSObject(), CLLocationManagerDelegateProtocol {
        override fun locationManager(manager: CLLocationManager, didChangeAuthorizationStatus: CLAuthorizationStatus) {
            updateLocationStatus(didChangeAuthorizationStatus)
        }

        override fun locationManagerDidChangeAuthorization(manager: CLLocationManager) {
            updateLocationStatus(manager.authorizationStatus)
        }
    }

    init {
        locationManager.delegate = locationDelegate
        checkPermissions()
    }

    override fun checkPermissions() {
        // Áudio
        val audioStatus = when (AVAudioSession.sharedInstance().recordPermission()) {
            AVAudioSessionRecordPermissionGranted -> PermissionStatus.GRANTED
            AVAudioSessionRecordPermissionDenied -> PermissionStatus.DENIED
            else -> PermissionStatus.NOT_DETERMINED
        }
        updateStatus(PermissionType.AUDIO, audioStatus)

        // Localização
        updateLocationStatus(locationManager.authorizationStatus)

        // Sensores (Simplificado para iOS, geralmente não requer prompt)
        updateStatus(PermissionType.SENSORS, PermissionStatus.GRANTED)
    }

    private fun updateLocationStatus(status: CLAuthorizationStatus) {
        val permissionStatus = when (status) {
            kCLAuthorizationStatusAuthorizedAlways,
            kCLAuthorizationStatusAuthorizedWhenInUse -> PermissionStatus.GRANTED
            kCLAuthorizationStatusDenied,
            kCLAuthorizationStatusRestricted -> PermissionStatus.DENIED
            kCLAuthorizationStatusNotDetermined -> PermissionStatus.NOT_DETERMINED
            else -> PermissionStatus.NOT_DETERMINED
        }
        updateStatus(PermissionType.LOCATION, permissionStatus)
    }

    override fun requestPermission(type: PermissionType) {
        when (type) {
            PermissionType.AUDIO -> {
                AVAudioSession.sharedInstance().requestRecordPermission { granted: Boolean ->
                    val status = if (granted) PermissionStatus.GRANTED else PermissionStatus.DENIED
                    updateStatus(PermissionType.AUDIO, status)
                }
            }
            PermissionType.LOCATION -> {
                val status = locationManager.authorizationStatus
                if (status == kCLAuthorizationStatusNotDetermined) {
                    locationManager.requestWhenInUseAuthorization()
                } else if (status == kCLAuthorizationStatusDenied || status == kCLAuthorizationStatusRestricted) {
                    updateStatus(PermissionType.LOCATION, PermissionStatus.DENIED)
                } else {
                    updateStatus(PermissionType.LOCATION, PermissionStatus.GRANTED)
                }
            }
            PermissionType.SENSORS -> {
                updateStatus(PermissionType.SENSORS, PermissionStatus.GRANTED)
            }
        }
    }

    private fun updateStatus(type: PermissionType, status: PermissionStatus) {
        _permissionStatuses.value += (type to status)
    }
}

actual fun providePermissionManager(): PermissionManager = IosPermissionManager()
