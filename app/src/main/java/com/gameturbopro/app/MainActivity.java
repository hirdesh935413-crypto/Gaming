package com.gameturbopro.app;

import android.app.Activity;
import android.content.Intent;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

    private TextView ramValue;
    private TextView batteryValue;
    private TextView boostStatus;
    private TextView statusText;

    private final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        ramValue = findViewById(R.id.ramValue);
        batteryValue = findViewById(R.id.batteryValue);
        boostStatus = findViewById(R.id.boostStatus);
        statusText = findViewById(R.id.statusText);

        TextView boostButton = findViewById(R.id.boostButton);

        Button gameLauncher = findViewById(R.id.gameLauncher);
        Button displaySettings = findViewById(R.id.displaySettings);
        Button developerSettings = findViewById(R.id.developerSettings);

        updateStats();

        boostButton.setOnClickListener(v -> activateBoost());

        gameLauncher.setOnClickListener(v -> openGameLauncher());

        displaySettings.setOnClickListener(v -> openDisplaySettings());

        developerSettings.setOnClickListener(v -> openDeveloperSettings());

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                updateStats();
                handler.postDelayed(this, 3000);
            }
        }, 3000);
    }

    private void activateBoost() {

        statusText.setText("● BOOSTED");
        statusText.setTextColor(
                getResources().getColor(R.color.accent)
        );

        boostStatus.setText(
                "Gaming mode activated"
        );

        Toast.makeText(
                this,
                "GAME TURBO activated",
                Toast.LENGTH_SHORT
        ).show();
    }

    private void updateStats() {

        ActivityManagerHelper helper =
                new ActivityManagerHelper(this);

        int ram = helper.getRamUsagePercent();

        ramValue.setText(ram + "%");

        BatteryManager batteryManager =
                (BatteryManager) getSystemService(
                        Context.BATTERY_SERVICE
                );

        int battery = batteryManager.getIntProperty(
                BatteryManager.BATTERY_PROPERTY_CAPACITY
        );

        if (battery >= 0) {
            batteryValue.setText(battery + "%");
        }
    }

    private void openGameLauncher() {

        PackageManager pm = getPackageManager();

        Intent intent =
                pm.getLaunchIntentForPackage(
                        "com.dts.freefireth"
                );

        if (intent != null) {

            startActivity(intent);

        } else {

            Toast.makeText(
                    this,
                    "Game not installed. Add your game's package ID in MainActivity.java.",
                    Toast.LENGTH_LONG
            ).show();
        }
    }

    private void openDisplaySettings() {

        try {

            Intent intent =
                    new Intent(
                            Settings.ACTION_DISPLAY_SETTINGS
                    );

            startActivity(intent);

        } catch (Exception e) {

            Toast.makeText(
                    this,
                    "Display settings unavailable",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    private void openDeveloperSettings() {

        try {

            Intent intent =
                    new Intent(
                            Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS
                    );

            startActivity(intent);

        } catch (Exception e) {

            Toast.makeText(
                    this,
                    "Developer settings unavailable",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    @Override
    protected void onDestroy() {

        handler.removeCallbacksAndMessages(null);

        super.onDestroy();
    }

    private static class ActivityManagerHelper {

        private final Context context;

        ActivityManagerHelper(Context context) {
            this.context = context;
        }

        int getRamUsagePercent() {

            android.app.ActivityManager.MemoryInfo info =
                    new android.app.ActivityManager.MemoryInfo();

            android.app.ActivityManager manager =
                    (android.app.ActivityManager)
                            context.getSystemService(
                                    Context.ACTIVITY_SERVICE
                            );

            if (manager == null) {
                return 0;
            }

            manager.getMemoryInfo(info);

            long total = info.totalMem;
            long available = info.availMem;

            if (total <= 0) {
                return 0;
            }

            long used = total - available;

            return (int) ((used * 100L) / total);
        }
    }
                                }
