package com.lynn.catchnotify.ui

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.provider.Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS
import android.speech.tts.TextToSpeech
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.lynn.catchnotify.MyNotificationListenerService.Companion.wxMessage
import com.lynn.catchnotify.R
import com.lynn.catchnotify.data.WxMessage
import com.lynn.catchnotify.databinding.ActivityMainBinding
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var tts: TextToSpeech

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
//        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        if (!notificationListenerEnable()) {
            startNotificationSetting()
        }

        tts = TextToSpeech(this) { status ->
            println("linlian status=$status")
            if (TextToSpeech.SUCCESS == status) {

                tts.setLanguage(Locale.CHINESE)

            }

        }


        binding.btnStart.setOnClickListener {
            tts.speak("您好", TextToSpeech.QUEUE_FLUSH, null)
        }

        wxMessage.observe(this, object : Observer<WxMessage> {
            override fun onChanged(value: WxMessage) {
                tts.speak(
                    "收到来自${value.sender}的消息,${value.message}",
                    TextToSpeech.QUEUE_FLUSH,
                    null
                )
            }

        })

    }

    /**
     * 判断应用是否有权限
     */
    private fun notificationListenerEnable(): Boolean {
        var enable = false
        val packageName = packageName
        val flat: String =
            Settings.Secure.getString(contentResolver, "enabled_notification_listeners")
        if (flat != null) {
            enable = flat.contains(packageName)
        }
        return enable
    }

    /*
    显示设置界面，运行我们的应用监听服务
     */
    private fun startNotificationSetting() {
        startActivity(Intent(ACTION_NOTIFICATION_LISTENER_SETTINGS));
    }

    override fun onDestroy() {
        super.onDestroy()

    }
}