package br.com.zup.xyinc.jpa

import groovy.json.JsonBuilder
import groovy.json.JsonSlurperClassic

import javax.persistence.AttributeConverter

class JpaJsonConverter implements AttributeConverter<Map, String> {
    @Override
    String convertToDatabaseColumn(Map meta) {
        return new JsonBuilder(meta).toString()
    }

    @Override
    Map convertToEntityAttribute(String dbData) {
        return new JsonSlurperClassic().parseText(dbData)
    }
}
