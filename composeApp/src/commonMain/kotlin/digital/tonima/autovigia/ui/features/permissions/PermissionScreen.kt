package digital.tonima.autovigia.ui.features.permissions

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import digital.tonima.autovigia.sensors.PermissionStatus
import digital.tonima.autovigia.sensors.PermissionType
import digital.tonima.autovigia.ui.designsystem.AutoVigIATokens
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun PermissionScreen(
    viewModel: PermissionViewModel = koinViewModel(),
    onAllPermissionsGranted: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.allGranted) {
        if (uiState.allGranted) {
            onAllPermissionsGranted()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Permissões Necessárias",
            style = MaterialTheme.typography.headlineMedium,
            color = AutoVigIATokens.Primary,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "Para que o AutoVigIA funcione corretamente e proteja seu veículo, precisamos das seguintes permissões:",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        PermissionItem(
            title = "Áudio do Motor",
            description = "Análise acústica para detecção de falhas mecânicas via Edge AI.",
            status = uiState.permissions[PermissionType.AUDIO] ?: PermissionStatus.NOT_DETERMINED,
            onRequest = { viewModel.onIntent(PermissionIntent.RequestPermission(PermissionType.AUDIO)) }
        )
        
        PermissionItem(
            title = "Localização",
            description = "Rastreamento de trajetos e contexto geográfico das anomalias.",
            status = uiState.permissions[PermissionType.LOCATION] ?: PermissionStatus.NOT_DETERMINED,
            onRequest = { viewModel.onIntent(PermissionIntent.RequestPermission(PermissionType.LOCATION)) }
        )
        
        PermissionItem(
            title = "Sensores (Vibração)",
            description = "Uso de acelerômetro e giroscópio para monitorar a saúde mecânica.",
            status = uiState.permissions[PermissionType.SENSORS] ?: PermissionStatus.NOT_DETERMINED,
            onRequest = { viewModel.onIntent(PermissionIntent.RequestPermission(PermissionType.SENSORS)) }
        )
        
        Spacer(modifier = Modifier.height(48.dp))
        
        if (uiState.allGranted) {
            Button(
                onClick = onAllPermissionsGranted,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = AutoVigIATokens.Healthy)
            ) {
                Text("Tudo Pronto!")
            }
        }
    }
}

@Composable
private fun PermissionItem(
    title: String,
    description: String,
    status: PermissionStatus,
    onRequest: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = AutoVigIATokens.Surface
        )
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = AutoVigIATokens.OnSurface,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = AutoVigIATokens.OnSurfaceVariant
                )
            }
            
            Button(
                onClick = onRequest,
                enabled = status != PermissionStatus.GRANTED,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (status == PermissionStatus.GRANTED) AutoVigIATokens.Healthy else AutoVigIATokens.Primary
                )
            ) {
                Text(if (status == PermissionStatus.GRANTED) "✓" else "OK")
            }
        }
    }
}
