[app]
title = App List Generator
package.name = applistgenerator
package.domain = org.example

source.dir = .
version = 1.0
requirements = python3, kivy==2.1.0, android, pyjnius

orientation = portrait
fullscreen = 0

[buildozer]
log_level = 2

[app]
presplash.filename = %(source.dir)s/assets/presplash.png
icon.filename = %(source.dir)s/assets/icon.png

android.permissions = READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE
android.api = 30
android.minapi = 21
android.ndk = 25b
android.sdk = 33
android.allow_backup = true

# Buildozer configuration
p4a.branch = master
android.accept_sdk_license = true

[buildozer]
warn_on_root = 1