# App Scanner - Generador de Lista de Aplicaciones

![Android](https://img.shields.io/badge/Android-21%2B-brightgreen?logo=android)
![GitHub Actions](https://img.shields.io/badge/GitHub_Actions-Build_APK-blue?logo=githubactions)
![Kotlin](https://img.shields.io/badge/Kotlin-Gradle_8.2.1-purple?logo=kotlin)

Una aplicaci�n Android que escanea todas las aplicaciones instaladas en el dispositivo y las exporta a un archivo de texto en formato `app_name = android_package`.

##  Caracter�sticas Principales

- **Escaneo completo** de todas las aplicaciones instaladas
- **4 filtros inteligentes**: usuario, sistema, Google Apps y todas
- **Estad�sticas en tiempo real** con contadores actualizados
- **Exportaci�n flexible** con timestamp para evitar sobreescrituras
- **Interfaz Material Design** con carteles identificadores
- **Build autom�tico** con GitHub Actions

##  Descarga R�pida

### M�todo Recomendado: GitHub Actions
1. Ve a la pesta�a **Actions**
2. Selecciona el �ltimo workflow exitoso
3. Descarga el artifact **app-scanner-apk**
4. Instala el APK en tu dispositivo Android

### Build Local
```bash
git clone https://github.com/Romaster1985/App-List-Generator.git
cd App-List-Generator
chmod +x gradlew
./gradlew assembleDebug

 Tecnolog�as Utilizadas

� Lenguaje: Java
� Framework: Android SDK
� Build Tool: Gradle 8.2.1
� UI: Material Design Components
� CI/CD: GitHub Actions
� Target API: 34 (Android 14)
� Min API: 21 (Android 5.0)

 Estructura del Proyecto

App-List-Generator/
 .github/workflows/
    build-apk.yml          # GitHub Actions workflow
 app/
    src/main/java/com/appscanner/
       MainActivity.java  # Actividad principal
       AppInfo.java       # Modelo de datos
       AppAdapter.java    # Adapter para RecyclerView
    src/main/res/          # Recursos y layouts
    build.gradle.kts       # Configuraci�n del m�dulo
 gradle/wrapper/
    gradle-wrapper.properties
 build.gradle.kts           # Configuraci�n del proyecto
 settings.gradle.kts        # Configuraci�n de settings
 gradle.properties          # Propiedades de Gradle
 gradlew                    # Script de Gradle para Unix
 gradlew.bat                # Script de Gradle para Windows
 README.md                  # Este archivo
 User_Manual.md             # Manual de usuario

 Autor

Romaster1985

�  Email: roman.ignacio.romero@gmail.com
�  GitHub: https://github.com/Romaster1985

 Agradecimientos

� Deepseek - Asistencia en desarrollo
� ChatGPT - Soporte en resoluci�n de problemas
� Replit - Entorno de desarrollo inicial
� Gradle - Sistema de build: https://github.com/gradle/gradle

---

 Para instrucciones detalladas de uso, consulta el Manual de Usuario
