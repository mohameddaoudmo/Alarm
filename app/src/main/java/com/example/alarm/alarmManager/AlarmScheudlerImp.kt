package com.example.alarm.alarmManager

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import java.time.ZoneId

class AlarmScheudlerImp(private val context: Context) : AlarmScheudelr {
    private val alarmManager = context.getSystemService(AlarmManager::class.java)
    override fun schedule(item: AlarmItem) {
        val intent = Intent(context, AlarmReciever::class.java).apply {
            putExtra("EXTRA_MESSAGE", item.messege)
        }
        alarmManager.setAndAllowWhileIdle(
            AlarmManager.RTC,
            item.time.atZone(ZoneId.systemDefault()).toEpochSecond() * 1000,
            PendingIntent.getBroadcast(
                context,
                item.hashCode(),
                intent,
                PendingIntent.FLAG_MUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
        )
    }

    override fun cancel(item: AlarmItem) {
        alarmManager.cancel(
            PendingIntent.getBroadcast(
                context,
                item.hashCode(),
                Intent(context, AlarmReciever::class.java),
                PendingIntent.FLAG_MUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
        )
    }
}