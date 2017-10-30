package br.com.zup.xyinc.exception

class NotFoundException extends Exception {

    NotFoundException(String modelName, String paramName, String paramValue) {
        super("${modelName} with ${paramName} ${paramValue} not found")
    }
    NotFoundException(String modelName, Long id) {
        this(modelName, "id", String.valueOf(id))
    }
}
