package com.example.alarm.workManger

import android.content.Context
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneOffset
import java.util.Calendar
import java.util.Locale

private fun scheduleNotification(time: LocalTime?, date: LocalDate?, message: String, context: Context) {

if(time!= null &&date!= null){
    var selectedDataTime = date.atTime(time)
    val currentTime = LocalDateTime.now()
    val delayInMillis = selectedDataTime.toInstant(ZoneOffset.UTC).toEpochMilli() - currentTime.toInstant(
        ZoneOffset.UTC
    ).toEpochMilli()

    val notificationWork = OneTimeWorkRequestBuilder<NotificationWorker>()
        .setInitialDelay(delayInMillis, java.util.concurrent.TimeUnit.MILLISECONDS)
        .build()
    WorkManager.getInstance().enqueue(notificationWork)

}


}
