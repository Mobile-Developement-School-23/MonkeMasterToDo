package com.monke.yandextodo.utils

import java.text.SimpleDateFormat
import java.util.*

object DateUtils {

    // Форматирование даты
    // Возвращает дату в виде в строки в формате "27 июля 2022"
    fun formatDate(day: Int, month: Int, year: Int): String {
        var sDay = day.toString()
        var sMonth = month.toString()
        if (day < 10)
            sDay = "0$day"
        if (month < 10)
            sMonth = "0$month"
        val strDate = "$year-$month-$day"
        val dateParser = SimpleDateFormat("yyyy-MM-dd")
        val date = dateParser.parse(strDate)
        val dateFormatter = SimpleDateFormat("d MMMM yyyy", Locale("ru"))
        return dateFormatter.format(date!!)
    }

    fun formatDate(date: Calendar): String {
        return formatDate(
            day = date.get(Calendar.DAY_OF_MONTH),
            month = date.get(Calendar.MONTH),
            year = date.get(Calendar.YEAR)
        )
    }

    fun formatTime(date: Calendar): String {
        val hour = date.get(Calendar.HOUR_OF_DAY)
        var sHour = hour.toString()
        if (hour < 10)
            sHour = "0$hour"
        val minute = date.get(Calendar.MINUTE)
        var sMinute = minute.toString()
        if (minute < 10)
            sMinute = "0$minute"
        return "$sHour:$sMinute"
    }
}