package com.appscanner;

import android.Manifest;
import android.content.ContentValues;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    
    private RecyclerView recyclerView;
    private AppAdapter adapter;
    private Button scanButton, exportButton, filterButton, aboutButton;
    private TextView statsText;
    
    private List<AppInfo> allApps = new ArrayList<>();
    private int filterState = 0; // 0: Solo usuario, 1: Solo sistema, 2: Todas

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        initializeViews();
        setupRecyclerView();
        setupClickListeners();
        updateFilterButtonText();
    }

    private void initializeViews() {
        recyclerView = findViewById(R.id.recyclerView);
        scanButton = findViewById(R.id.scanButton);
        exportButton = findViewById(R.id.exportButton);
        filterButton = findViewById(R.id.filterButton);
        aboutButton = findViewById(R.id.statsButton);
        statsText = findViewById(R.id.statsText);
        
        // Cambiar texto del botón de estadísticas a "ACERCA DE"
        aboutButton.setText("ACERCA DE");
    }

    private void setupRecyclerView() {
        adapter = new AppAdapter(new ArrayList<>());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void setupClickListeners() {
        scanButton.setOnClickListener(v -> scanApps());
        exportButton.setOnClickListener(v -> exportToFile());
        filterButton.setOnClickListener(v -> toggleFilter());
        aboutButton.setOnClickListener(v -> showAboutDialog());
    }

    private void scanApps() {
        scanButton.setEnabled(false);
        statsText.setText("Escaneando aplicaciones...");
        statsText.setTextColor(Color.BLACK);

        new Thread(() -> {
            PackageManager pm = getPackageManager();
            List<PackageInfo> packages;
            
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                packages = pm.getInstalledPackages(PackageManager.MATCH_ALL);
            } else {
                packages = pm.getInstalledPackages(PackageManager.GET_META_DATA);
            }

            allApps.clear();
            for (PackageInfo packageInfo : packages) {
                String appName = packageInfo.applicationInfo.loadLabel(pm).toString();
                String packageName = packageInfo.packageName;
                boolean isSystemApp = (packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0 
                                   || (packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0;
                
                allApps.add(new AppInfo(appName, packageName, isSystemApp));
            }

            runOnUiThread(() -> {
                applyFilter();
                scanButton.setEnabled(true);
                exportButton.setEnabled(!allApps.isEmpty());
                updateStats();
            });
        }).start();
    }

    private void toggleFilter() {
        filterState = (filterState + 1) % 3; // Ciclar entre 0, 1, 2
        updateFilterButtonText();
        applyFilter();
    }

    private void updateFilterButtonText() {
        switch (filterState) {
            case 0:
                filterButton.setText("SOLO USUARIO");
                break;
            case 1:
                filterButton.setText("SOLO SISTEMA");
                break;
            case 2:
                filterButton.setText("MOSTRAR TODAS");
                break;
        }
    }

    private void applyFilter() {
        List<AppInfo> filteredApps = new ArrayList<>();
        for (AppInfo app : allApps) {
            switch (filterState) {
                case 0: // Solo usuario
                    if (!app.isSystemApp()) {
                        filteredApps.add(app);
                    }
                    break;
                case 1: // Solo sistema
                    if (app.isSystemApp()) {
                        filteredApps.add(app);
                    }
                    break;
                case 2: // Mostrar todas
                    filteredApps.add(app);
                    break;
            }
        }
        adapter.updateAppList(filteredApps);
        updateStats();
    }

    private void showAboutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Acerca de App Scanner");
        builder.setMessage(
            "Autor: Romaster1985\n\n" +
            "e-mail: roman.ignacio.romero@gmail.com\n\n" +
            "GitHub: https://github.com/Romaster1985/App-List-Generator.git\n\n" +
            "Agradecimientos:\n" +
            "• Deepseek\n" +
            "• ChatGPT\n" +
            "• Replit Bots"
        );
        builder.setPositiveButton("CERRAR", (dialog, which) -> dialog.dismiss());
        builder.setCancelable(true);
        
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void updateStats() {
        if (allApps.isEmpty()) {
            statsText.setText("Presiona 'ESCANEAR APPS' para comenzar");
            statsText.setTextColor(Color.BLACK);
            return;
        }

        int totalApps = allApps.size();
        int userApps = 0;
        int systemApps = 0;
        
        for (AppInfo app : allApps) {
            if (app.isSystemApp()) {
                systemApps++;
            } else {
                userApps++;
            }
        }

        String statsTextContent;
        switch (filterState) {
            case 0: // Solo usuario
                statsTextContent = String.format(Locale.getDefault(),
                    "APPS DE USUARIO: %d\n" +
                    "Total instaladas: %d\n" +
                    "Apps de sistema: %d",
                    adapter.getItemCount(), totalApps, systemApps);
                break;
            case 1: // Solo sistema
                statsTextContent = String.format(Locale.getDefault(),
                    "APPS DE SISTEMA: %d\n" +
                    "Total instaladas: %d\n" +
                    "Apps de usuario: %d",
                    adapter.getItemCount(), totalApps, userApps);
                break;
            case 2: // Mostrar todas
                statsTextContent = String.format(Locale.getDefault(),
                    "TODAS LAS APPS: %d\n" +
                    "• Usuario: %d\n" +
                    "• Sistema: %d",
                    adapter.getItemCount(), userApps, systemApps);
                break;
            default:
                statsTextContent = "Error en filtro";
        }

        statsText.setText(statsTextContent);
        statsText.setTextColor(Color.RED);
    }

    private void exportToFile() {
        exportButton.setEnabled(false);

        new Thread(() -> {
            try {
                String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
                    .format(new Date());
                String fileName = "installed_apps_" + timestamp + ".txt";
                
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    exportWithMediaStore(fileName);
                } else {
                    exportLegacy(fileName);
                }

            } catch (IOException e) {
                handleExportError(e);
            }
        }).start();
    }

    private void exportWithMediaStore(String fileName) throws IOException {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Downloads.DISPLAY_NAME, fileName);
        values.put(MediaStore.Downloads.MIME_TYPE, "text/plain");
        values.put(MediaStore.Downloads.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS);

        Uri uri = getContentResolver().insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values);
        if (uri == null) {
            throw new IOException("Failed to create file in Downloads");
        }

        try (OutputStream outputStream = getContentResolver().openOutputStream(uri)) {
            if (outputStream == null) {
                throw new IOException("Failed to open output stream");
            }
            writeAppListToStream(outputStream);
        }

        final String successMessage = String.format("Exportado a: Downloads/%s", fileName);
        showExportResult(successMessage, true);
    }

    private void exportLegacy(String fileName) throws IOException {
        java.io.File downloadsDir = Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_DOWNLOADS);
        if (!downloadsDir.exists()) {
            downloadsDir.mkdirs();
        }

        java.io.File file = new java.io.File(downloadsDir, fileName);
        try (java.io.FileOutputStream fos = new java.io.FileOutputStream(file)) {
            writeAppListToStream(fos);
        }

        final String successMessage = String.format("Exportado a: %s", file.getAbsolutePath());
        showExportResult(successMessage, true);
    }

    private void writeAppListToStream(OutputStream outputStream) throws IOException {
        List<AppInfo> appList = adapter.getAppList();
        for (AppInfo app : appList) {
            String line = app.getFormattedLine() + "\n";
            outputStream.write(line.getBytes());
        }
    }

    private void handleExportError(Exception e) {
        runOnUiThread(() -> {
            Toast.makeText(MainActivity.this,
                "Error de exportación: " + e.getMessage(),
                Toast.LENGTH_LONG).show();
            exportButton.setEnabled(true);
        });
    }

    private void showExportResult(String message, boolean success) {
        runOnUiThread(() -> {
            Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
            exportButton.setEnabled(true);
            
            if (success) {
                updateStats(); // Actualizar estadísticas después de exportar
            }
        });
    }
}