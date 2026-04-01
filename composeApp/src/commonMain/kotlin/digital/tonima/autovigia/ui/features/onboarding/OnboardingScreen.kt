package digital.tonima.autovigia.ui.features.onboarding

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import digital.tonima.autovigia.ui.designsystem.AutoVigIATokens
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun OnboardingScreen(
    viewModel: OnboardingViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Bem-vindo ao AutoVigIA",
            style = MaterialTheme.typography.headlineMedium,
            color = AutoVigIATokens.Primary
        )

        Text(
            text = "Vamos começar configurando seu veículo.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        OutlinedTextField(
            value = uiState.brand,
            onValueChange = { viewModel.onIntent(OnboardingIntent.BrandChanged(it)) },
            label = { Text("Marca (Fabricante)") },
            modifier = Modifier.fillMaxWidth(),
            enabled = !uiState.isLoading
        )

        OutlinedTextField(
            value = uiState.model,
            onValueChange = { viewModel.onIntent(OnboardingIntent.ModelChanged(it)) },
            label = { Text("Modelo") },
            modifier = Modifier.fillMaxWidth(),
            enabled = !uiState.isLoading
        )

        OutlinedTextField(
            value = uiState.year,
            onValueChange = { viewModel.onIntent(OnboardingIntent.YearChanged(it)) },
            label = { Text("Ano") },
            modifier = Modifier.fillMaxWidth(),
            enabled = !uiState.isLoading
        )

        OutlinedTextField(
            value = uiState.odometer,
            onValueChange = { viewModel.onIntent(OnboardingIntent.OdometerChanged(it)) },
            label = { Text("Quilometragem (Odomêtro)") },
            modifier = Modifier.fillMaxWidth(),
            enabled = !uiState.isLoading
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
        ) {
            Text(
                "O carro apresenta algum defeito?",
                color = MaterialTheme.colorScheme.onSurface
            )
            Switch(
                checked = uiState.hasIssues,
                onCheckedChange = { viewModel.onIntent(OnboardingIntent.HasIssuesChanged(it)) },
                enabled = !uiState.isLoading
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { viewModel.onIntent(OnboardingIntent.SaveVehicle) },
            modifier = Modifier.fillMaxWidth(),
            enabled = !uiState.isLoading && uiState.brand.isNotBlank() && uiState.model.isNotBlank(),
            colors = ButtonDefaults.buttonColors(
                containerColor = AutoVigIATokens.Primary
            )
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = AutoVigIATokens.OnPrimary,
                    strokeWidth = 2.dp
                )
            } else {
                Text("Salvar e Continuar")
            }
        }
    }
}
