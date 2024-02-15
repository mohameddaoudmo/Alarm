package com.example.alarm.alarmManager

interface AlarmScheudelr  {
    fun schedule(item: AlarmItem)
    fun cancel (item: AlarmItem)
}