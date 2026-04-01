package digital.tonima.autovigia

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import org.jetbrains.compose.resources.painterResource

import autovigia.composeapp.generated.resources.Res
import autovigia.composeapp.generated.resources.compose_multiplatform

import digital.tonima.autovigia.domain.model.AppState
import digital.tonima.autovigia.domain.model.VehicleStatus
import digital.tonima.autovigia.domain.usecase.VehicleStateMachine
import digital.tonima.autovigia.ui.designsystem.AutoVigIATokens
import digital.tonima.autovigia.ui.designsystem.AutoVigIATypography
import digital.tonima.autovigia.ui.designsystem.VehicleStatusCard
import digital.tonima.autovigia.ui.features.onboarding.OnboardingScreen
import digital.tonima.autovigia.ui.features.permissions.PermissionScreen
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.unit.dp
import org.koin.compose.koinInject

@Composable
@Preview
fun App(
    stateMachine: VehicleStateMachine = koinInject()
) {
    val currentState by stateMachine.currentState.collectAsState()

    MaterialTheme(
        colorScheme = MaterialTheme.colorScheme.copy(
            primary = AutoVigIATokens.Primary,
            onPrimary = AutoVigIATokens.OnPrimary,
            secondary = AutoVigIATokens.Secondary,
            surface = AutoVigIATokens.Surface,
            onSurface = AutoVigIATokens.OnSurface,
            onSurfaceVariant = AutoVigIATokens.OnSurfaceVariant,
            background = AutoVigIATokens.Background,
            onBackground = AutoVigIATokens.OnBackground,
            error = AutoVigIATokens.Error
        ),
        typography = AutoVigIATypography
    ) {
        Column(
            modifier = Modifier
                .background(AutoVigIATokens.Background)
                .safeContentPadding()
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            when (currentState) {
                AppState.ONBOARDING -> OnboardingScreen()
                AppState.PERMISSIONS -> PermissionScreen(
                    onAllPermissionsGranted = { stateMachine.onPermissionsGranted() }
                )
                AppState.CALIBRATING -> Text("Calibrando...")
                AppState.MONITORING -> Text("Monitorando...")
            }
        }
    }
}