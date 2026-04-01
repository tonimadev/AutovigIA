package digital.tonima.autovigia.ui.designsystem

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import digital.tonima.autovigia.domain.model.VehicleStatus

@Composable
fun VehicleStatusCard(
    brand: String,
    model: String,
    status: VehicleStatus,
    modifier: Modifier = Modifier
) {
    val statusColor = when (status) {
        VehicleStatus.HEALTHY -> AutoVigIATokens.Healthy
        VehicleStatus.ISSUES -> AutoVigIATokens.Critical
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(AutoVigIATokens.Surface)
            .padding(16.dp)
    ) {
        Column {
            Text(
                text = "$brand $model",
                style = MaterialTheme.typography.headlineSmall,
                color = AutoVigIATokens.OnSurface
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(statusColor)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (status == VehicleStatus.HEALTHY) "Saudável" else "Anomalia Detectada",
                    style = MaterialTheme.typography.bodyMedium,
                    color = statusColor
                )
            }
        }
    }
}
