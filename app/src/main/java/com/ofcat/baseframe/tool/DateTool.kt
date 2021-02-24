package com.ofcat.baseframe.tool

import org.joda.time.DateTime
import java.text.SimpleDateFormat
import java.util.*

object DateTool {

    const val DATE_FORMATTER = "yyyy-MM-dd HH:mm:ss"
    const val DATE_PATTERN = "yyyy-MM-dd"
    const val DATE_PATTERN_B = "yyyy/MM/dd"
    const val DATE_PATTERN_C = "yyyy年MM月dd日"
    const val DATE_PATTERN_D = "HH:mm:ss"
    const val DATE_PATTERN_THAI = "dd-MM-yyyy HH:mm"

    fun formatDay(date: Date?): String {
        return if (date != null) DateTime(date).toString(DATE_PATTERN) else ""
    }

    fun formatDayAndHour(date: Date?): String {
        return if (date != null) DateTime(date).toString(DATE_FORMATTER) else ""
    }

    fun formatTime(date: Date?): String {
        return if (date != null) DateTime(date).toString(DATE_PATTERN_D) else ""
    }

    fun formatPreMonths(months: Int): String {
        return formatDay(DateTime.now().minusMonths(months).plusDays(1).toDate())
    }

    fun formatBirthday(years: Int, months: Int, days: Int): String {
        return "$years-$months-$days"
    }

    fun getDateTimeFromStr(dateStr: String, dateFormat: String): DateTime {
        val sdf1 = SimpleDateFormat(dateFormat, Locale.getDefault())
        return DateTime(sdf1.parse(dateStr).time)
    }

    /**
     * @param compareDateTime 被比較的時間
     * @param startDateTime 比較的起始時間
     * @param endDateTime 比較的結束時間
     */
    fun isContainsBetweenDateTime(compareDateTime: DateTime, startDateTime: DateTime, endDateTime: DateTime): Boolean {
        return compareDateTime.isAfter(startDateTime.minusSeconds(1)) && compareDateTime.isBefore(endDateTime)
    }

    fun isSameDay(compareDateTime1: DateTime?, compareDateTime2: DateTime?): Boolean {
        return when {
            compareDateTime1 == null -> {
                false
            }
            compareDateTime2 == null -> {
                false
            }
            compareDateTime1.year == compareDateTime2.year &&
                    compareDateTime1.monthOfYear == compareDateTime2.monthOfYear &&
                    compareDateTime1.dayOfMonth == compareDateTime2.dayOfMonth -> {
                true
            }
            else -> {
                false
            }
        }
    }

    fun isAfterToday(dateTime: DateTime): Boolean {
        val now = DateTime.now()
        return when {
            dateTime.year > now.year -> {
                true
            }
            dateTime.year == now.year && dateTime.monthOfYear > now.monthOfYear -> {
                true
            }
            dateTime.year == now.year && dateTime.monthOfYear == now.monthOfYear && dateTime.dayOfMonth > now.dayOfMonth -> {
                true
            }
            else -> {
                false
            }
        }
    }

    fun isBeforeToday(dateTime: DateTime): Boolean {
        val now = DateTime.now()
        return when {
            dateTime.year < now.year -> {
                true
            }
            dateTime.year == now.year && dateTime.monthOfYear < now.monthOfYear -> {
                true
            }
            dateTime.year == now.year && dateTime.monthOfYear == now.monthOfYear && dateTime.dayOfMonth < now.dayOfMonth -> {
                true
            }
            else -> {
                false
            }
        }
    }

    fun isToday(dateTime: DateTime): Boolean {
        val now = DateTime.now()
        return dateTime.year == now.year && dateTime.monthOfYear == now.monthOfYear && dateTime.dayOfMonth == now.dayOfMonth
    }

    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    fun formatToMMddhhmm(dateStr: String): String {
        val sdf1 = SimpleDateFormat("dd / MM HH:mm", Locale.getDefault())
        val dt = sdf1.parse(dateStr)
        val sdf2 = SimpleDateFormat("MM/dd HH:mm", Locale.getDefault())

        return sdf2.format(dt)
    }

    @Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    fun isAfterNowDateTime(dateStr: String): Boolean {
        if (dateStr.isBlank()) {
            return false
        }

        val sdf1 = SimpleDateFormat("dd / MM HH:mm", Locale.getDefault())
        val inputDate = DateTime(sdf1.parse(dateStr).time)
        return inputDate.isAfterNow
    }

}