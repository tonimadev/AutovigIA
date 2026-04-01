# AutoVigIA

AutoVigIA is a cutting-edge mobile application designed to monitor vehicle health and detect mechanical anomalies using Edge AI. By analyzing acoustic (microphone) and vibration (IMU - accelerometer/gyroscope) data in real-time, the app creates a "healthy baseline" for your car and identifies predictive mechanical failures before they become costly repairs.

## Key Features
- **Real-time Monitoring**: Continuous analysis of engine noise and vehicle vibrations.
- **Edge AI Analysis**: On-device machine learning (TensorFlow Lite for Android, Core ML for iOS) to detect anomalies without compromising privacy.
- **Predictive Maintenance**: Early detection of mechanical issues based on deviations from the vehicle's unique signature.
- **Gamification**: Earn points and rewards for contributing anonymized data to fleet analysis, helping improve the AI for everyone.
- **Cross-Platform**: Built with Kotlin Multiplatform and Compose Multiplatform for a consistent experience on both Android and iOS.

## Project Architecture
The project follows **Clean Architecture** principles and **MVI (Model-View-Intent)** pattern for the UI layer:
- **Domain Layer**: Contains business logic, state machines, and models.
- **Data Layer**: Implements repositories and local persistence using **Room (KMP)**.
- **UI Layer**: Built with **Compose Multiplatform**, ensuring a unified Design System.
- **Sensors Engine**: Platform-specific implementations (expect/actual) for high-frequency data collection from microphone and IMU sensors.

## Tech Stack
- **Core**: Kotlin Multiplatform (KMP)
- **UI**: Compose Multiplatform (iOS & Android)
- **Dependency Injection**: Koin
- **Local Storage**: Room (KMP)
- **Networking**: Ktor Client
- **Machine Learning**: TensorFlow Lite (Android) & Core ML (iOS)
- **Async**: Coroutines & Flow

## Getting Started

### Android
To build the Android application:
```bash
./gradlew :composeApp:assembleDebug
```

### iOS
To build the iOS application, open the `iosApp` directory in Xcode or use the Gradle task:
```bash
./gradlew :composeApp:compileKotlinIosSimulatorArm64
```

---
*AutoVigIA - Vigilance for your vehicle.*