package digital.tonima.autovigia.ui.designsystem

import androidx.compose.material3.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Color Tokens
object AutoVigIATokens {
    val Primary = Color(0xFF6200EE)
    val PrimaryVariant = Color(0xFF3700B3)
    val Secondary = Color(0xFF03DAC6)
    val Background = Color(0xFF121212)
    val Surface = Color(0xFF1E1E1E)
    val Error = Color(0xFFCF6679)
    val OnPrimary = Color.White
    val OnBackground = Color(0xFFE1E1E1) // Um cinza bem claro em vez de branco puro
    val OnSurface = Color(0xFFE1E1E1) // O mesmo para o OnSurface
    val OnSurfaceVariant = Color(0xFFB0B0B0) // Para textos secundários e rótulos
    
    // Status Colors
    val Healthy = Color(0xFF4CAF50)
    val Warning = Color(0xFFFFC107)
    val Critical = Color(0xFFF44336)
}

// Typography
val AutoVigIATypography = Typography(
    headlineLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        letterSpacing = 0.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        letterSpacing = 0.5.sp
    ),
    labelMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        letterSpacing = 0.5.sp
    )
)
