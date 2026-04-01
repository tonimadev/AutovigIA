package digital.tonima.autovigia.sensors

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AndroidPermissionManager(private val context: Context) : PermissionManager {
    private var activity: Activity? = null

    fun setActivity(activity: Activity) {
        this.activity = activity
    }

    private val _permissionStatuses = MutableStateFlow<Map<PermissionType, PermissionStatus>>(
        PermissionType.entries.associateWith { PermissionStatus.NOT_DETERMINED }
    )
    override val permissionStatuses = _permissionStatuses.asStateFlow()

    override fun checkPermissions() {
        _permissionStatuses.update { current ->
            current.mapValues { (type, _) ->
                val permission = getManifestPermission(type)
                if (permission == null) {
                    PermissionStatus.GRANTED
                } else if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED) {
                    PermissionStatus.GRANTED
                } else {
                    PermissionStatus.DENIED
                }
            }
        }
    }

    override fun requestPermission(type: PermissionType) {
        val permission = getManifestPermission(type) ?: return
        activity?.let {
            ActivityCompat.requestPermissions(it, arrayOf(permission), type.ordinal)
        }
    }

    private fun getManifestPermission(type: PermissionType): String? {
        return when (type) {
            PermissionType.AUDIO -> Manifest.permission.RECORD_AUDIO
            PermissionType.LOCATION -> Manifest.permission.ACCESS_FINE_LOCATION
            PermissionType.SENSORS -> if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
                Manifest.permission.HIGH_SAMPLING_RATE_SENSORS
            } else null
        }
    }
    
    fun updateStatus(type: PermissionType, status: PermissionStatus) {
        _permissionStatuses.update { it + (type to status) }
    }
}

actual fun providePermissionManager(): PermissionManager = error("Must be provided by Koin with context")
