import kivy
from kivy.app import App
from kivy.uix.boxlayout import BoxLayout
from kivy.uix.button import Button
from kivy.uix.label import Label
from kivy.uix.progressbar import ProgressBar
from kivy.clock import Clock
from kivy.logger import Logger
import os
import re
from datetime import datetime

# Módulos Android
try:
    from jnius import autoclass
    PythonActivity = autoclass('org.kivy.android.PythonActivity')
    PackageManager = autoclass('android.content.pm.PackageManager')
    Intent = autoclass('android.content.Intent')
    HAS_ANDROID = True
except Exception as e:
    Logger.error(f'AppScanner: Error loading Android modules: {e}')
    HAS_ANDROID = False

class AppScannerAPK(App):
    def build(self):
        self.title = "App List Generator"
        layout = BoxLayout(orientation='vertical', padding=20, spacing=15)
        
        # Título
        self.title_label = Label(
            text='Generador de Lista de Apps',
            size_hint=(1, 0.15),
            font_size='22sp',
            bold=True
        )
        layout.add_widget(self.title_label)
        
        # Información
        self.info_label = Label(
            text='Genera archivo: nombre = paquete\nGuardado en: Documents/AppScanner/',
            size_hint=(1, 0.25),
            text_size=(400, None),
            halign='center'
        )
        layout.add_widget(self.info_label)
        
        # Botón de escaneo
        self.scan_button = Button(
            text='Generar Lista de Apps',
            size_hint=(1, 0.2),
            background_color=(0.2, 0.7, 0.3, 1),
            font_size='18sp'
        )
        self.scan_button.bind(on_press=self.start_scan)
        layout.add_widget(self.scan_button)
        
        # Barra de progreso
        self.progress_bar = ProgressBar(
            size_hint=(1, 0.1),
            max=100,
            value=0
        )
        layout.add_widget(self.progress_bar)
        
        # Estado
        self.status_label = Label(
            text='Listo para generar lista de aplicaciones',
            size_hint=(1, 0.2),
            font_size='14sp',
            text_size=(400, None),
            halign='center'
        )
        layout.add_widget(self.status_label)
        
        return layout
    
    def clean_app_name(self, app_name):
        """Limpia el nombre de la app según el formato solicitado"""
        cleaned = app_name.lower()
        cleaned = re.sub(r'[^a-z0-9.]', '', cleaned)
        return cleaned if cleaned else "app"
    
    def get_launcher_apps(self):
        """Obtiene aplicaciones con actividad LAUNCHER - Método más confiable"""
        try:
            context = PythonActivity.mActivity.getApplicationContext()
            package_manager = context.getPackageManager()
            
            # Intent para buscar actividades LAUNCHER
            intent = Intent(Intent.ACTION_MAIN)
            intent.addCategory(Intent.CATEGORY_LAUNCHER)
            
            resolve_list = package_manager.queryIntentActivities(intent, 0)
            apps_list = []
            processed_packages = set()
            
            total = resolve_list.size()
            
            for i in range(total):
                resolve_info = resolve_list.get(i)
                activity_info = resolve_info.activityInfo
                package_name = activity_info.packageName
                
                # Evitar duplicados
                if package_name not in processed_packages:
                    processed_packages.add(package_name)
                    try:
                        app_info = package_manager.getApplicationInfo(package_name, 0)
                        app_name = str(package_manager.getApplicationLabel(app_info))
                        apps_list.append((app_name, package_name))
                    except Exception as e:
                        # Si falla, usar el nombre del paquete
                        app_name = package_name.split('.')[-1]
                        apps_list.append((app_name, package_name))
                
                # Actualizar progreso cada 10 apps
                if i % 10 == 0:
                    progress = 10 + (i / total) * 70
                    self.progress_bar.value = progress
                    self.status_label.text = f'Encontradas {len(apps_list)} apps... {i}/{total}'
            
            # Ordenar alfabéticamente
            apps_list.sort(key=lambda x: x[0].lower())
            return apps_list
            
        except Exception as e:
            Logger.error(f'Error en get_launcher_apps: {e}')
            return []
    
    def save_apps_list(self, apps_list):
        """Guarda la lista de apps en un archivo de texto"""
        try:
            # Usar el directorio de Documents que es accesible
            documents_dir = "/storage/emulated/0/Documents"
            if not os.path.exists(documents_dir):
                documents_dir = os.path.join(os.path.expanduser('~'), 'Documents')
                if not os.path.exists(documents_dir):
                    documents_dir = "/sdcard/Documents"
            
            appscanner_dir = os.path.join(documents_dir, "AppScanner")
            os.makedirs(appscanner_dir, exist_ok=True)
            
            timestamp = datetime.now().strftime("%Y%m%d_%H%M%S")
            filename = f"apps_list_{timestamp}.txt"
            filepath = os.path.join(appscanner_dir, filename)
            
            with open(filepath, 'w', encoding='utf-8') as f:
                for app_name, package_name in apps_list:
                    clean_name = self.clean_app_name(app_name)
                    f.write(f"{clean_name} = {package_name}\n")
            
            return filepath, len(apps_list)
            
        except Exception as e:
            Logger.error(f'Error guardando archivo: {e}')
            return None, 0
    
    def start_scan(self, instance):
        """Inicia el escaneo de aplicaciones"""
        if not HAS_ANDROID:
            self.status_label.text = 'Error: No se puede ejecutar en Android'
            return
        
        self.scan_button.disabled = True
        self.status_label.text = 'Buscando aplicaciones...'
        self.progress_bar.value = 10
        
        Clock.schedule_once(lambda dt: self.perform_scan(), 0.5)
    
    def perform_scan(self):
        """Realiza el escaneo y guardado"""
        try:
            # Obtener lista de apps
            apps_list = self.get_launcher_apps()
            
            self.progress_bar.value = 80
            self.status_label.text = f'Guardando {len(apps_list)} aplicaciones...'
            
            # Guardar archivo
            filepath, count = self.save_apps_list(apps_list)
            
            self.progress_bar.value = 100
            
            if filepath and os.path.exists(filepath):
                file_size = os.path.getsize(filepath) / 1024
                self.status_label.text = (
                    f'✅ ¡Lista generada!\n'
                    f'Aplicaciones: {count}\n'
                    f'Archivo: {os.path.basename(filepath)}\n'
                    f'Tamaño: {file_size:.1f} KB\n'
                    f'Ubicación: Documents/AppScanner/'
                )
            else:
                self.status_label.text = '❌ Error: No se pudo guardar el archivo'
            
            self.scan_button.disabled = False
            
        except Exception as e:
            Logger.error(f'Error en perform_scan: {e}')
            self.status_label.text = f'❌ Error: {str(e)}'
            self.scan_button.disabled = False

if __name__ == '__main__':
    AppScannerAPK().run()