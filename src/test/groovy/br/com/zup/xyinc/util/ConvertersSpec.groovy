package br.com.zup.xyinc.util

import spock.lang.Specification

class ConvertersSpec extends Specification {

    def "toDate"() {
        expect:
        Converters.toDate(value)?.getTime() == result

        where:
        value                      | result
        null                       | null
        ""                         | null
        "2017-10-29T14:32:39.582Z" | 1509287559582
    }

    def "toDecimal"() {
        expect:
        Converters.toDecimal(value, precision, scale)?.toPlainString() == result

        where:
        value     | precision | scale | result
        null      | 10        | 2     | null
        ""        | 10        | 2     | null
        30        | 10        | 2     | "30.00"
        "1"       | 10        | 2     | "1.00"
        "1.346"   | 10        | 2     | "1.35"
        "-1.346"  | 10        | 2     | "-1.35"
    }

    def "toLong"() {
        expect:
        Converters.toLong(value) == result

        where:
        value     | result
        null      | null
        ""        | null
        30        | 30L
        "1"       | 1L
        "-40"     | -40L
    }

    def "toString without length"() {
        expect:
        Converters.toString(value) == result

        where:
        value                    | result
        null                     | null
        ""                       | ""
        "joao da silva sauro"    | "joao da silva sauro"
        40                       | "40"
        "-43"                    | "-43"
        true                     | "true"
        false                    | "false"
    }

    def "toString with length"() {
        expect:
        Converters.toString(value, length) == result

        where:
        value                    | length | result
        null                     | 10     | null
        ""                       | 10     | ""
        "joao da silva sauro"    | 10     | "joao da si"
        "< 10"                   | 10     | "< 10"
    }
}
