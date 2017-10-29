package br.com.zup.xyinc.exception

class AlreadyExistsException extends Exception {

    private String modelName
    private String paramName
    private String paramValue

    AlreadyExistsException(String modelName, String paramName, String paramValue) {
        super("${modelName} with ${paramName} '${paramValue}' already exists")
        this.modelName = modelName
        this.paramName = paramName
        this.paramValue = paramValue
    }

    String getModelName() {
        return modelName
    }

    void setModelName(String modelName) {
        this.modelName = modelName
    }

    String getParamName() {
        return paramName
    }

    void setParamName(String paramName) {
        this.paramName = paramName
    }

    String getParamValue() {
        return paramValue
    }

    void setParamValue(String paramValue) {
        this.paramValue = paramValue
    }
}
