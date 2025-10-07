[app]
title = App List Generator
package.name = applistgenerator
package.domain = org.example
source.dir = .
version = 1.0
requirements = python3, kivy, pyjnius
orientation = portrait
fullscreen = 0

android.api = 33
android.minapi = 21
android.ndk = 25b
android.build_tools = 33.0.0

android.permissions = READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE
presplash.filename = %(source.dir)s/assets/presplash.png
icon.filename = %(source.dir)s/assets/icon.png

[buildozer]
log_level = 2
