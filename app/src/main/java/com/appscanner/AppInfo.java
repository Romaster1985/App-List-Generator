package com.appscanner;

public class AppInfo {
    private String appName;
    private String packageName;
    private boolean isSystemApp;

    public AppInfo(String appName, String packageName, boolean isSystemApp) {
        this.appName = appName;
        this.packageName = packageName;
        this.isSystemApp = isSystemApp;
    }

    public String getAppName() { return appName; }
    public String getPackageName() { return packageName; }
    public boolean isSystemApp() { return isSystemApp; }

    public String getFormattedLine() {
        return String.format("%s = %s", appName, packageName);
    }
}