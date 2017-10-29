package br.com.zup.xyinc.util

import br.com.zup.xyinc.exception.BlankValidationException
import br.com.zup.xyinc.exception.InvalidValueValidationException
import spock.lang.Specification

class ValidatorsSpec extends Specification {

    def "notEmpty throwing exception"() {
        when:
        Validators.notEmpty(attributeName, value)

        then:
        thrown exceptionClass

        where:
        attributeName | value | exceptionClass
        "name"        | null  | BlankValidationException
        "name"        | ""    | BlankValidationException

    }

    def "notEmpty NOT throwing exception"() {
        when:
        Validators.notEmpty(attributeName, value)

        then:
        noExceptionThrown()

        where:
        attributeName | value
        "name"        | "teste"
        "name"        | 10
        "name"        | 0

    }

    def "positive throwing exception"() {
        when:
        Validators.positive(attributeName, value)

        then:
        thrown exceptionClass

        where:
        attributeName | value | exceptionClass
        "name"        | null  | BlankValidationException
        "name"        | 0     | InvalidValueValidationException
        "name"        | -1    | InvalidValueValidationException

    }

    def "positive NOT throwing exception"() {
        when:
        Validators.positive(attributeName, value)

        then:
        noExceptionThrown()

        where:
        attributeName | value
        "name"        | 1
        "name"        | 10

    }

    def "non-negative throwing exception"() {
        when:
        Validators.nonNegative(attributeName, value)

        then:
        thrown exceptionClass

        where:
        attributeName | value | exceptionClass
        "name"        | null  | BlankValidationException
        "name"        | -1    | InvalidValueValidationException

    }

    def "non-negative NOT throwing exception"() {
        when:
        Validators.nonNegative(attributeName, value)

        then:
        noExceptionThrown()

        where:
        attributeName | value
        "name"        | 0
        "name"        | 1
        "name"        | 10

    }
}
