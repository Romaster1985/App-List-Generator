package com.appscanner;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AppAdapter extends RecyclerView.Adapter<AppAdapter.AppViewHolder> {
    
    private List<AppInfo> appList;

    public AppAdapter(List<AppInfo> appList) {
        this.appList = appList;
    }

    @NonNull
    @Override
    public AppViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_app, parent, false);
        return new AppViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppViewHolder holder, int position) {
        AppInfo app = appList.get(position);
        holder.bind(app);
    }

    @Override
    public int getItemCount() {
        return appList.size();
    }

    public void updateAppList(List<AppInfo> newAppList) {
        this.appList = new ArrayList<>(newAppList);
        notifyDataSetChanged();
    }

    public List<AppInfo> getAppList() {
        return new ArrayList<>(appList);
    }

    static class AppViewHolder extends RecyclerView.ViewHolder {
        private TextView appNameText;
        private TextView packageNameText;
        private TextView systemAppText;

        public AppViewHolder(@NonNull View itemView) {
            super(itemView);
            appNameText = itemView.findViewById(R.id.appNameText);
            packageNameText = itemView.findViewById(R.id.packageNameText);
            systemAppText = itemView.findViewById(R.id.systemAppText);
        }

        public void bind(AppInfo app) {
            appNameText.setText(app.getAppName());
            packageNameText.setText(app.getPackageName());
            
            if (app.isSystemApp()) {
                systemAppText.setText("Sistema");
                systemAppText.setVisibility(View.VISIBLE);
            } else {
                systemAppText.setVisibility(View.GONE);
            }
        }
    }
}