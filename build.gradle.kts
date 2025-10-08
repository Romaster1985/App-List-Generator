plugins {
    id("com.android.application") version "8.2.2" apply false
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}