package com.monke.yandextodo

import com.monke.yandextodo.utils.DateUtils
import org.junit.Assert
import org.junit.Test

class DateUtilsTest {

    @Test
    fun single_digit_month() {
        Assert.assertEquals("16 июня 2023", DateUtils.formatDate(16, 6, 2023))
    }

    @Test
    fun single_digit_day() {
        Assert.assertEquals("1 октября 2023", DateUtils.formatDate(1, 10, 2023))
    }

    @Test
    fun normal_date() {
        Assert.assertEquals("16 ноября 2023", DateUtils.formatDate(16, 11, 2023))
    }
}