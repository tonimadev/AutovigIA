package digital.tonima.autovigia.ui.features.home

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import digital.tonima.autovigia.domain.model.AppState
import digital.tonima.autovigia.ui.designsystem.AutoVigIATokens

@Composable
fun HomeScreen(viewModel: DetectionViewModel) {
    val appState by viewModel.appState.collectAsState()
    val audioAmplitude by viewModel.audioAmplitude.collectAsState()
    val vibrationMagnitude by viewModel.vibrationMagnitude.collectAsState()
    val vehicle by viewModel.vehicle.collectAsState()
    val calibrationProgress by viewModel.calibrationProgress.collectAsState()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = AutoVigIATokens.Background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Text(
                text = "AutovigIA",
                style = MaterialTheme.typography.headlineMedium,
                color = AutoVigIATokens.Secondary
            )

            DashboardStatusCard(appState, calibrationProgress)

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                RadarCanvas(
                    modifier = Modifier.fillMaxSize(),
                    audioAmplitude = audioAmplitude,
                    vibrationMagnitude = vibrationMagnitude
                )
            }

            ControlsSection(
                isMonitoring = appState == AppState.MONITORING || appState == AppState.ANOMALY,
                autoStartEnabled = vehicle?.autoStartEnabled ?: false,
                onToggleMonitoring = { viewModel.toggleMonitoring() },
                onToggleAutoStart = { viewModel.toggleAutoStart(it) }
            )
        }
    }
}

@Composable
fun DashboardStatusCard(appState: AppState, calibrationProgress: Float) {
    val (statusText, statusColor) = when (appState) {
        AppState.CALIBRATING -> "Calibrando..." to AutoVigIATokens.Warning
        AppState.MONITORING -> "Monitoramento Ativo" to AutoVigIATokens.Healthy
        AppState.INACTIVE -> "Inativo" to AutoVigIATokens.OnSurfaceVariant
        AppState.ANOMALY -> "Anomalia Detectada" to AutoVigIATokens.Critical
        else -> "AutoVigIA Ativo" to AutoVigIATokens.Secondary
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = AutoVigIATokens.Surface),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = statusText,
                style = MaterialTheme.typography.headlineSmall,
                color = statusColor
            )
            if (appState == AppState.CALIBRATING) {
                LinearProgressIndicator(
                    progress = { calibrationProgress },
                    modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
                    color = statusColor,
                    trackColor = statusColor.copy(alpha = 0.2f)
                )
            }
        }
    }
}

@Composable
fun RadarCanvas(
    modifier: Modifier = Modifier,
    audioAmplitude: Float,
    vibrationMagnitude: Float
) {
    val infiniteTransition = rememberInfiniteTransition()
    val pulse by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    Canvas(modifier = modifier) {
        val center = Offset(size.width / 2, size.height / 2)
        val maxRadius = (minOf(size.width, size.height) / 2) * 0.8f

        // Desenhar círculos de grade (radar)
        for (i in 1..4) {
            drawCircle(
                color = AutoVigIATokens.OnSurfaceVariant.copy(alpha = 0.1f),
                radius = maxRadius * (i / 4f),
                center = center,
                style = Stroke(width = 1.dp.toPx())
            )
        }

        // Gradiente dinâmico baseado na intensidade
        val intensity = (audioAmplitude + vibrationMagnitude).coerceIn(0f, 1f)
        val waveColor = lerp(AutoVigIATokens.Healthy, AutoVigIATokens.Critical, intensity)

        // Ondas de Áudio (Anéis pulsantes)
        drawCircle(
            color = waveColor.copy(alpha = 0.4f * (1f - pulse)),
            radius = maxRadius * pulse * (0.2f + audioAmplitude.coerceIn(0f, 1f)),
            center = center,
            style = Stroke(width = 3.dp.toPx())
        )

        // Magnitude de Vibração (Gradiente radial)
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(waveColor.copy(alpha = 0.3f), Color.Transparent),
                center = center,
                radius = maxRadius * vibrationMagnitude.coerceIn(0.1f, 1f)
            ),
            radius = maxRadius * vibrationMagnitude.coerceIn(0.1f, 1f),
            center = center
        )
    }
}

@Composable
fun ControlsSection(
    isMonitoring: Boolean,
    autoStartEnabled: Boolean,
    onToggleMonitoring: () -> Unit,
    onToggleAutoStart: (Boolean) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Button(
            onClick = onToggleMonitoring,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isMonitoring) AutoVigIATokens.Critical else AutoVigIATokens.Secondary
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = if (isMonitoring) "Parar AutovigiA" else "Iniciar AutovigiA",
                style = MaterialTheme.typography.titleMedium,
                color = Color.Black
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(AutoVigIATokens.Surface)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Iniciar automaticamente ao abrir o app",
                style = MaterialTheme.typography.bodyMedium,
                color = AutoVigIATokens.OnSurface
            )
            Switch(
                checked = autoStartEnabled,
                onCheckedChange = onToggleAutoStart,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = AutoVigIATokens.Secondary,
                    checkedTrackColor = AutoVigIATokens.Secondary.copy(alpha = 0.5f)
                )
            )
        }
    }
}
