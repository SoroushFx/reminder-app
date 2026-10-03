package com.godwatch.app;

import android.accessibilityservice.AccessibilityService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.List;

public class WatchService extends AccessibilityService {

    private static final String CHANNEL_ID = "godwatch";
    private static final long COOLDOWN_MS = 30000;
    private long lastShown = 0;

    // آدرس‌بار مرورگرها
    private static final String[] URL_BAR_IDS = {
            "com.android.chrome:id/url_bar",
            "org.mozilla.firefox:id/mozac_browser_toolbar_url_view",
            "com.brave.browser:id/url_bar",
            "com.microsoft.emmx:id/url_bar",
            "com.sec.android.app.sbrowser:id/location_bar_edit_text",
            "com.opera.browser:id/url_field"
    };

    // کلمات/دامنه‌هایی که باید تشخیص داده شوند (خودت می‌توانی اضافه کنی)
    private static final String[] KEYWORDS = {
            "porn", "xvideos", "xnxx", "xhamster", "redtube",
            "youporn", "xxx", "brazzers", "spankbang", "chaturbate"
    };

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        AccessibilityNodeInfo root = getRootInActiveWindow();
        if (root == null) return;

        for (String id : URL_BAR_IDS) {
            List<AccessibilityNodeInfo> nodes = root.findAccessibilityNodeInfosByViewId(id);
            if (nodes == null) continue;
            for (AccessibilityNodeInfo node : nodes) {
                CharSequence text = node.getText();
                if (text != null && matches(text.toString())) {
                    showReminder();
                    return;
                }
            }
        }
    }

    private boolean matches(String url) {
        String u = url.toLowerCase();
        for (String k : KEYWORDS) {
            if (u.contains(k)) return true;
        }
        return false;
    }

    private void showReminder() {
        long now = System.currentTimeMillis();
        if (now - lastShown < COOLDOWN_MS) return;
        lastShown = now;

        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel ch = new NotificationChannel(
                    CHANNEL_ID, "GodWatch", NotificationManager.IMPORTANCE_HIGH);
            nm.createNotificationChannel(ch);
        }

        Notification.Builder b = Build.VERSION.SDK_INT >= 26
                ? new Notification.Builder(this, CHANNEL_ID)
                : new Notification.Builder(this);

        b.setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("یادت باشه")
                .setContentText("خدا داره می‌بینه")
                .setAutoCancel(true);

        nm.notify(1, b.build());
    }

    @Override
    public void onInterrupt() { }
}
