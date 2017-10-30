package br.com.zup.xyinc.exception

class ReservedInvalidValueValidationException extends ValidationException {

    ReservedInvalidValueValidationException(String paramName, String value) {
        super("Invalid value for ${paramName}: ${value} (reserved)")
    }
}
