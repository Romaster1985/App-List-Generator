[app]
# Título de la aplicación
title = App List Generator

# Nombre del paquete (debe ser único)
package.name = applistgenerator

# Dominio del paquete
package.domain = org.example

# Directorio fuente
source.dir = .

# Versión de la aplicación
version = 1.0

# Requerimientos de Python
requirements = python3, kivy==2.1.0, android, pyjnius, pillow

# Orientación de la pantalla
orientation = portrait

# Pantalla completa
fullscreen = 0

# Versiones de Android
android.api = 33
android.minapi = 21
android.ndk = 25b
android.sdk = 33

# Permisos de Android (completos)
android.permissions = READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE

# Configuración de la aplicación Android
android.allow_backup = true
android.presplash_color = #4CAF50
android.wakelock = false

# Branch de python-for-android
p4a.branch = master

# Aceptar licencias del SDK
android.accept_sdk_license = true

# Archivos de assets
presplash.filename = %(source.dir)s/assets/presplash.png
icon.filename = %(source.dir)s/assets/icon.png

[buildozer]
# Nivel de log
log_level = 2

# Mostrar advertencias
warn_on_root = 1

# Directorio de build
build_dir = .buildozer

# Configuración por defecto
default.fatal = 1
default.warn_on_root = 1

# Comando para limpiar
clean = clean

# Configuración de target
target = android

# Configuración de release (opcional para futuras versiones)
; (app) release
;package.retain_debug_symbols = False
;version.regex = __version__ = ['"](.*)['"]
;version.filename = %(source.dir)s/main.py

# Configuración de logcat
logcat_filters = *:S python:D

# Estrategia de build
strategy = buildozer

# Configuración de repositorios
p4a.source_dir = .
android.add_src = .

# Configuración de arquitecturas (opcional - para reducir tamaño)
;android.arch = armeabi-v7a

# Configuración de intent filters (opcional)
;android.intent_filters = 
;android.manifest.intent_filters = 

# Características de hardware (opcional)
;android.features = 
