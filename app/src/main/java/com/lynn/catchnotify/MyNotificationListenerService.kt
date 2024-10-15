package com.lynn.catchnotify

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification

class MyNotificationListenerService : NotificationListenerService() {
    override fun onNotificationPosted(sbn: StatusBarNotification) {
        // 捕获通知
        val notification = sbn.notification
        // 根据用户设置过滤通知
        println("linlian ${sbn.packageName},${sbn.notification.tickerText}")
    }
}
