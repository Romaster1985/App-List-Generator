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

# Permisos de Android
android.permissions = READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE

# Configuración de la aplicación Android
android.allow_backup = true
android.presplash_color = #4CAF50

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
