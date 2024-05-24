package com.orpatservice.app.ui.admin.notification;

import android.os.Handler;

import androidx.annotation.MainThread;

import com.pushwoosh.notification.NotificationServiceExtension;
import com.pushwoosh.notification.PushMessage;

public class MyNotificationServiceExtension extends NotificationServiceExtension {
    @Override
    public boolean onMessageReceived(final PushMessage message) {
        if (isAppOnForeground()) {
            Handler mainHandler = new Handler(getApplicationContext().getMainLooper());
            mainHandler.post(() -> {
                handlePush(message);
            });

            // this indicates that notification should not be displayed
            return true;
        }

        return false;
    }

    @Override
    protected void startActivityForPushMessage(PushMessage message) {
        super.startActivityForPushMessage(message);
        handlePush(message);
    }

    @MainThread
    private void handlePush(PushMessage message) {
        // TODO: handle push message
    }
}