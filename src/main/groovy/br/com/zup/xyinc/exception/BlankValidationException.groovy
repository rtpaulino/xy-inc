package br.com.zup.xyinc.exception

class BlankValidationException extends ValidationException {

    BlankValidationException(String paramName) {
        super(paramName, "${paramName} cannot be blank")
    }
}
