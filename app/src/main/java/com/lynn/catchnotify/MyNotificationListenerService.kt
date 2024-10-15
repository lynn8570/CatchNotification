package com.lynn.catchnotify

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import androidx.lifecycle.MutableLiveData
import com.lynn.catchnotify.data.PackageConstant.PACKAGE_WX
import com.lynn.catchnotify.data.WxMessage

class MyNotificationListenerService : NotificationListenerService() {

    companion object{
        val wxMessage = MutableLiveData<WxMessage>()
    }
    override fun onNotificationPosted(sbn: StatusBarNotification) {
        // 捕获通知
        val notification = sbn.notification
        // 根据用户设置过滤通知
        println("linlian ${sbn.packageName},${sbn.notification.tickerText}")

        if (PACKAGE_WX == sbn.packageName) {
            convertToWxMessage(sbn)?.let {
                wxMessage.postValue(it)
            }

        }
    }

    private fun convertToWxMessage(sbn: StatusBarNotification): WxMessage? {
        try {
            sbn.notification.tickerText?.let {
                val splits = it.split(
                    delimiters = arrayOf(":"),
                    ignoreCase = false,
                    limit = 2
                )
                if (splits.size == 2) {
                   return  WxMessage(splits[0], splits[1])
                }

            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null

    }
}
