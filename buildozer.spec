[app]
title = App List Generator
package.name = applistgenerator
package.domain = org.example
source.dir = .
version = 1.0
requirements = python3, kivy, pyjnius
orientation = portrait
fullscreen = 0

# USA API 33 - compatible con build-tools 30.0.3 o 36.1.0
android.api = 33
android.minapi = 21
android.permissions = READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE

# Build tools específicos (opcional, comenta si no funciona)
# android.build_tools_version = 30.0.3

presplash.filename = %(source.dir)s/assets/presplash.png
icon.filename = %(source.dir)s/assets/icon.png

[buildozer]
log_level = 1
