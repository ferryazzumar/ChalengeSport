package com.example.chalengesport.commons.utils

import java.text.SimpleDateFormat
import java.util.*

class TimeUtils {
    companion object {
        fun getTimeZoneById(id: String = "GMT+07:00"): TimeZone {
            return TimeZone.getTimeZone(id)
        }

        fun dateFormatter(format: String = "yyyy-MM-dd"): SimpleDateFormat {
            return SimpleDateFormat(format, Locale("in_ID")).apply {
                timeZone = getTimeZoneById()
            }
        }

        fun Date.formatString(
            parseFormat: String = "yyyy-MM-dd",
            resultFormat: String = "dd-MM-yyyy"
        ): String {
            val dateFormat = dateFormatter(parseFormat)
            val format = dateFormat.format(this)

            val parsedFormat = dateFormatter(resultFormat)
            val formattedDate = parsedFormat.parse(format)

            return parsedFormat.format(formattedDate ?: Calendar.getInstance().time)
        }
    }
}