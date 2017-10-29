package br.com.zup.xyinc.exception

class NotFoundException extends Exception {

    private String modelName
    private String paramName
    private String paramValue

    NotFoundException(String modelName, String paramName, String paramValue) {
        super("${modelName} with ${paramName} ${paramValue} not found")
        this.modelName = modelName
        this.paramName = paramName
        this.paramValue = paramValue
    }
    NotFoundException(String modelName, Long id) {
        this(modelName, "id", String.valueOf(id))
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
