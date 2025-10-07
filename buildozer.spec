[app]
title = App List Generator
package.name = applistgenerator
package.domain = org.example
source.dir = .
version = 1.0
requirements = python3, kivy==2.1.0, pyjnius, android
orientation = portrait
fullscreen = 0

[buildozer]
log_level = 1

[app]
presplash.filename = %(source.dir)s/assets/presplash.png
icon.filename = %(source.dir)s/assets/icon.png
android.permissions = READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE
android.api = 33
android.minapi = 21
p4a.branch = master
android.accept_sdk_license = true
