package com.example.alarm.workManger

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.alarm.R

class NotificationWorker(context: Context, params: WorkerParameters) : Worker(context, params) {

    override fun doWork(): Result {
        // Perform the task you want to do, e.g., show a notification
        showNotification()

        // Indicate whether the work finished successfully or failed
        return Result.success()
    }

    private fun showNotification() {
        // Customize your notification here
        val notification = NotificationCompat.Builder(applicationContext, "channelId")
            .setContentTitle("Notification Title")
            .setContentText("Notification Text")
            .setSmallIcon(androidx.core.R.id.right_icon)
            .build()

        val notificationManager = NotificationManagerCompat.from(applicationContext)
        if (ActivityCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        notificationManager.notify(1, notification)
    }
}
