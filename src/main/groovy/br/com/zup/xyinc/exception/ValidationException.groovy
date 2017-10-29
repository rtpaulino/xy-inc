package br.com.zup.xyinc.exception

class ValidationException extends Exception {

    private String paramName

    ValidationException(String paramName, String message) {
        super(message)
        this.paramName = paramName
    }

    String getParamName() {
        return paramName
    }

    void setParamName(String paramName) {
        this.paramName = paramName
    }
}
