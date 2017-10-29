package br.com.zup.xyinc.util

import br.com.zup.xyinc.exception.BlankValidationException
import br.com.zup.xyinc.exception.InvalidValueValidationException
import org.apache.commons.lang3.StringUtils

class Validators {

    static void notEmpty(String attributeName, String value) {
        if (StringUtils.isBlank(value)) {
            throw new BlankValidationException(attributeName)
        }
    }

    static void notEmpty(String attributeName, Long value) {
        if (value == null) {
            throw new BlankValidationException(attributeName)
        }
    }

    static void positive(String attributeName, Long value) {
        notEmpty(attributeName, value)
        if (value <= 0) {
            throw new InvalidValueValidationException(attributeName, String.valueOf(value))
        }
    }

    static void nonNegative(String attributeName, Long value) {
        notEmpty(attributeName, value)
        if (value < 0) {
            throw new InvalidValueValidationException(attributeName, String.valueOf(value))
        }
    }
}
