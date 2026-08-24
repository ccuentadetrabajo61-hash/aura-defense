# Aura Defense

Base nativa Android para Aura Defense, escrita en Kotlin con Jetpack Compose y Material 3. Esta primera fase establece una aplicación estable y compilable, sin módulos de defensa ni permisos sensibles.

## Compilación local

Requisitos: JDK 17 y Android SDK con API 35.

```bash
./gradlew clean assembleRelease
```

El APK se genera en `app/build/outputs/apk/release/`.

## Compilación en Codemagic

El workflow `aura-android-release` configura Java 17 y el Android SDK, hace ejecutable el wrapper, limpia el proyecto y ejecuta `assembleRelease`. El APK se publica como artefacto desde `app/build/outputs/apk/release/*.apk`.

## Fase 1

La pantalla inicial muestra el estado base, permite iniciar el diagnóstico futuro y abre el Centro Aura. El Aura ID se genera localmente como `AURA-XXXXXX` y se persiste con SharedPreferences. No se simulan amenazas ni capacidades de ciberseguridad.

Módulos previstos para fases posteriores:

- Security Posture Engine
- App Scanner
- VPN Service
- Notification Guard
- Share Scanner
- QR Scanner
- Auras LAN
- Reports
- Encrypted Vault
