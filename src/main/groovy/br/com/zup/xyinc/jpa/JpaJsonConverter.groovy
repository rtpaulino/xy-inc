package br.com.zup.xyinc.jpa

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.BeansException
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.stereotype.Component

import javax.persistence.AttributeConverter

@Component
class JpaJsonConverter implements AttributeConverter<Map, String>, ApplicationContextAware {

    public static ObjectMapper mapper

    @Override
    void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        mapper = applicationContext.getBean(ObjectMapper)
    }

    @Override
    String convertToDatabaseColumn(Map meta) {
        if (meta == null)
            return null

        return mapper.writeValueAsString(meta);
    }

    @Override
    Map convertToEntityAttribute(String dbData) {
        if (dbData == null)
            return null

        return mapper.readValue(dbData, Map)

    }

}
