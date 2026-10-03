package com.godwatch.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Gravity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= 33) {
            requestPermissions(new String[]{"android.permission.POST_NOTIFICATIONS"}, 1);
        }

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setGravity(Gravity.CENTER);
        layout.setPadding(48, 48, 48, 48);

        TextView tv = new TextView(this);
        tv.setText("برای فعال شدن، سرویس GodWatch را در تنظیمات دسترسی‌پذیری روشن کن.");
        tv.setTextSize(18);
        tv.setGravity(Gravity.CENTER);

        Button btn = new Button(this);
        btn.setText("باز کردن تنظیمات Accessibility");
        btn.setOnClickListener(v ->
                startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)));

        layout.addView(tv);
        layout.addView(btn);
        setContentView(layout);
    }
}
