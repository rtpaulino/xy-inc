package br.com.zup.xyinc.exception

class BlankValidationException extends ValidationException {

    BlankValidationException(String paramName) {
        super("${paramName} cannot be blank")
    }
}
