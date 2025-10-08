# App Scanner - Android Application

Una aplicación Android que escanea todas las aplicaciones instaladas en el dispositivo y las exporta a un archivo de texto en formato `app_name = android_package`.

## 🚀 Características

- ✅ Escanea todas las aplicaciones instaladas
- ✅ Filtra entre apps de sistema y usuario
- ✅ Muestra estadísticas detalladas
- ✅ Exporta a archivo TXT con timestamp
- ✅ Interfaz moderna y fácil de usar
- ✅ Compatible con Android 5.0+

## 📦 Build Automático con GitHub Actions

Cada vez que hagas push a `main` o `master`, GitHub Actions construirá automáticamente el APK.

### Descargar APK

1. Ve a la pestaña **Actions**
2. Selecciona el último workflow exitoso
3. Descarga el artefacto `app-scanner-apk`

## 🔧 Build Local

```bash
# Hacer ejecutable gradlew
chmod +x gradlew

# Construir APK
./gradlew assembleDebug

# O usar el script
./build-local.sh