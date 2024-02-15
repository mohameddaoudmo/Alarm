package com.example.alarm.alarmManager

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.IBinder

class AlarmReciever :BroadcastReceiver() {
    override fun onReceive(p0: Context?, p1: Intent?) {
        val message = p1?.getStringExtra("EXTRA_MESSAGE")?: return
        println("mohamed taha $message")
    }

    override fun peekService(myContext: Context?, service: Intent?): IBinder {
        return super.peekService(myContext, service)
    }

    override fun getSentFromUid(): Int {
        return super.getSentFromUid()
    }

    override fun getSentFromPackage(): String? {
        return super.getSentFromPackage()
    }
}