package br.com.zup.xyinc.config

import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder

@Configuration
class Config {

    @Bean
    Jackson2ObjectMapperBuilderCustomizer addCustomBigDecimalSerialization() {
        return new Jackson2ObjectMapperBuilderCustomizer() {
            @Override
            void customize(Jackson2ObjectMapperBuilder jacksonObjectMapperBuilder) {
                jacksonObjectMapperBuilder.serializerByType(BigDecimal, new BigDecimalSerializer())
            }
        };
    }}
