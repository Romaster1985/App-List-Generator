package com.appscanner;

public class AppInfo {
    private String appName;
    private String packageName;
    private boolean isSystemApp;
    private boolean isGoogleApp;

    public AppInfo(String appName, String packageName, boolean isSystemApp, boolean isGoogleApp) {
        this.appName = appName;
        this.packageName = packageName;
        this.isSystemApp = isSystemApp;
        this.isGoogleApp = isGoogleApp;
    }

    public String getAppName() { return appName; }
    public String getPackageName() { return packageName; }
    public boolean isSystemApp() { return isSystemApp; }
    public boolean isGoogleApp() { return isGoogleApp; }

    public String getFormattedLine() {
        return String.format("%s = %s", appName, packageName);
    }
}