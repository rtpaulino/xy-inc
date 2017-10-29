package br.com.zup.xyinc.util

import org.apache.commons.lang3.StringUtils

import java.math.MathContext
import java.math.RoundingMode
import java.time.ZonedDateTime

class Converters {
    static Date toDate(Object value) {
        String valueAsString = toString(value)
        if (StringUtils.isEmpty(valueAsString)) {
            return null
        }

        ZonedDateTime dateTime = ZonedDateTime.parse(valueAsString)
        return Date.from(dateTime.toInstant())
    }

    static BigDecimal toDecimal(Object value, int precision, int scale) {
        String valueAsString = toString(value)
        if (StringUtils.isEmpty(valueAsString)) {
            return null
        }

        BigDecimal result = new BigDecimal(value, new MathContext(precision, RoundingMode.HALF_UP))
        return result.setScale(scale, RoundingMode.HALF_UP)
    }

    static Long toLong(Object value) {
        if (value == null) {
            return null
        }
        if (value instanceof Integer || value instanceof Long) {
            return (Long)value
        }
        String valueAsString = toString(value)
        if (StringUtils.isEmpty(valueAsString)) {
            return null
        }
        return Long.valueOf(valueAsString.trim())
    }

    static String toString(Object value) {
        return value?.toString()
    }

    static String toString(Object value, int length) {
        return StringUtils.left(toString(value), length)
    }
}
