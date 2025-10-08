#!/bin/bash
# build-local.sh
echo "🔨 Construyendo APK..."
./gradlew clean assembleDebug

if [ $? -eq 0 ]; then
    echo "✅ APK construido exitosamente!"
    echo "📱 Archivo: app/build/outputs/apk/debug/app-debug.apk"
    echo "📊 Tamaño: $(du -h app/build/outputs/apk/debug/app-debug.apk | cut -f1)"
else
    echo "❌ Error en la construcción"
    exit 1
fi