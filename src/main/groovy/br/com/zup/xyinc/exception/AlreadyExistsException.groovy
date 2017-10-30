package br.com.zup.xyinc.exception

class AlreadyExistsException extends Exception {
    AlreadyExistsException(String modelName, String paramName, String paramValue) {
        super("${modelName} with ${paramName} '${paramValue}' already exists")
    }
}
