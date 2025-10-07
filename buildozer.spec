[app]
title = App List Generator
package.name = applistgenerator
package.domain = org.example
source.dir = .
version = 1.0
requirements = python3, kivy, pyjnius
orientation = portrait
fullscreen = 0

# CONFIGURACIÓN QUE ENGAÑA A BUILDOZER
android.api = 33
android.minapi = 21
android.sdk = 33
android.ndk = 25b

# ESTA ES LA CLAVE: Especificar build-tools version exacta
android.build_tools = 33.0.0

# Forzar a p4a a usar esta versión
p4a.branch = develop

android.permissions = READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE
presplash.filename = %(source.dir)s/assets/presplash.png
icon.filename = %(source.dir)s/assets/icon.png

[buildozer]
log_level = 2
