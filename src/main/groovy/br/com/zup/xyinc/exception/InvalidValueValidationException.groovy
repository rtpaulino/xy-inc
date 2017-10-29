package br.com.zup.xyinc.exception

class InvalidValueValidationException extends ValidationException {

    InvalidValueValidationException(String paramName, String value) {
        super(paramName, "Invalid value for ${paramName}: ${value}")
    }
}
