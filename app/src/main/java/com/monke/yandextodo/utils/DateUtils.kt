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
}