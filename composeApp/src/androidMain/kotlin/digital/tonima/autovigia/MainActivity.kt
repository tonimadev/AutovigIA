package digital.tonima.autovigia

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import digital.tonima.autovigia.sensors.AndroidPermissionManager
import digital.tonima.autovigia.sensors.PermissionManager
import digital.tonima.autovigia.sensors.PermissionStatus
import digital.tonima.autovigia.sensors.PermissionType
import org.koin.android.ext.android.get

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        
        val permissionManager = get<PermissionManager>() as AndroidPermissionManager
        permissionManager.setActivity(this)
        
        setContent {
            App()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        val type = PermissionType.entries.getOrNull(requestCode) ?: return
        val status = if (grantResults.firstOrNull() == PackageManager.PERMISSION_GRANTED) {
            PermissionStatus.GRANTED
        } else {
            PermissionStatus.DENIED
        }
        (get<PermissionManager>() as AndroidPermissionManager).updateStatus(type, status)
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}