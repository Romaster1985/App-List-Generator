package com.appscanner;

import android.Manifest;
import android.content.ContentValues;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
    private Button scanButton, exportButton, filterButton, statsButton;
    private TextView statsText;
    
    private List<AppInfo> allApps = new ArrayList<>();
    private boolean showSystemApps = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        initializeViews();
        setupRecyclerView();
        setupClickListeners();
    }

    private void initializeViews() {
        recyclerView = findViewById(R.id.recyclerView);
        scanButton = findViewById(R.id.scanButton);
        exportButton = findViewById(R.id.exportButton);
        filterButton = findViewById(R.id.filterButton);
        statsButton = findViewById(R.id.statsButton);
        statsText = findViewById(R.id.statsText);
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
        statsButton.setOnClickListener(v -> showStatistics());
    }

    private void scanApps() {
    scanButton.setEnabled(false);
    statsText.setText(getString(R.string.scanning));

    new Thread(() -> {
        PackageManager pm = getPackageManager();
        List<PackageInfo> packages;
        
        // MEJORAR: Usar flags más específicos
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            packages = pm.getInstalledPackages(PackageManager.MATCH_ALL);
        } else {
            packages = pm.getInstalledPackages(PackageManager.GET_META_DATA);
        }

        allApps.clear();
        for (PackageInfo packageInfo : packages) {
            String appName = packageInfo.applicationInfo.loadLabel(pm).toString();
            String packageName = packageInfo.packageName;
            
            // MEJORAR: Detección más precisa de apps de sistema
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
        showSystemApps = !showSystemApps;
        filterButton.setText(showSystemApps ? 
            getString(R.string.show_user_apps) : 
            getString(R.string.show_all_apps));
        applyFilter();
    }

    private void applyFilter() {
        List<AppInfo> filteredApps = new ArrayList<>();
        for (AppInfo app : allApps) {
            if (showSystemApps || !app.isSystemApp()) {
                filteredApps.add(app);
            }
        }
        adapter.updateAppList(filteredApps);
        updateStats();
    }

    private void showStatistics() {
        int totalApps = allApps.size();
        int systemApps = 0;
        int userApps = 0;
        
        for (AppInfo app : allApps) {
            if (app.isSystemApp()) {
                systemApps++;
            } else {
                userApps++;
            }
        }
        
        String stats = String.format(Locale.getDefault(),
            "Total: %d\nUsuario: %d\nSistema: %d",
            totalApps, userApps, systemApps);
            
        statsText.setText(stats);
    }

    private void updateStats() {
        int visibleCount = adapter.getItemCount();
        statsText.setText(getString(R.string.apps_found, visibleCount));
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
                statsText.setText("Exportación completada");
            }
        });
    }
}
