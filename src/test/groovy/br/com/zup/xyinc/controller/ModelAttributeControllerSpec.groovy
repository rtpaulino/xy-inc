package br.com.zup.xyinc.controller

import br.com.zup.xyinc.domain.Attribute
import br.com.zup.xyinc.domain.Model
import br.com.zup.xyinc.repository.AttributeRepository
import br.com.zup.xyinc.repository.ModelRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import spock.lang.Specification

@SpringBootTest(webEnvironment =  SpringBootTest.WebEnvironment.RANDOM_PORT)
class ModelAttributeControllerSpec extends Specification {
    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    ModelRepository modelRepository

    @Autowired
    AttributeRepository attributeRepository

    Model model

    List<Map> attributeList = [
            [name: "name", type: "STRING", length: 50, scale: null, precision: null],
            [name: "revenues", type: "DECIMAL", length: null, scale: 2, precision: 15],
            [name: "since", type: "DATE", length: null, scale: null, precision: null],
            [name: "employees", type: "INTEGER", length: null, scale: null, precision: null]
    ]


    def setup() {
        model = modelRepository.save(new Model(name: "customer"))
    }

    def reloadModel() {
        model = modelRepository.findOne(model.id)
    }

    def cleanup() {
        reloadModel()
        modelRepository.delete(model)
    }

    void createAttributes() {
        attributeList.forEach {
            Attribute attribute = new Attribute(it)
            attribute.model = model
            attributeRepository.save(attribute)
        }
    }

    def "Create a model attribute successfully"() {
        when:
        def responses = attributeList.collect {
            return restTemplate.postForObject("/model/${model.id}/attribute", [
                    name: it.name,
                    type: it.type,
                    length: it.length,
                    scale: it.scale,
                    precision: it.precision
            ], Map)
        }

        then:
        attributeList.eachWithIndex { attribute, index ->
            assert responses[index] != null
            assert responses[index].id > 0
            assert responses[index].name == attribute.name
            assert responses[index].type == attribute.type
            assert responses[index].length == attribute.length
            assert responses[index].scale == attribute.scale
            assert responses[index].precision == attribute.precision
        }

        when:
        List<Attribute> created = responses.collect { attributeRepository.findOne(it.id as Long) }

        then:
        attributeList.eachWithIndex { attribute, index ->
            assert created[index].model.id == model.id
            assert created[index].name == attribute.name
            assert created[index].version >= 0
            assert created[index].type.toString() == attribute.type
            assert created[index].length == attribute.length
            assert created[index].scale == attribute.scale
            assert created[index].precision == attribute.precision
        }

        when:
        Model reloadedModel = modelRepository.findOne(model.id)

        then:
        reloadedModel.attributes.size() == attributeList.size()
        created.each { createdAttr ->
            assert reloadedModel.attributes.any { it.id == createdAttr.id }
        }

    }

    def "Update a model attribute successfully"() {
        setup:
        createAttributes()
        reloadModel()

        when:
        Attribute attribute = model.attributes.find { it.name == name }
        Long id = attribute.id
        restTemplate.put("/model/${model.id}/attribute/${id}", [
                name: newName,
                type: type,
                length: length,
                scale: scale,
                precision: precision
        ])
        reloadModel()
        attribute = model.attributes.find { it.id == id }

        then:
        attribute.name == newName
        attribute.type.toString() == type
        attribute.length == length
        attribute.scale == scale
        attribute.precision == precision

        where:
        name        | newName     | type      | length | scale | precision
        "name"      | "name"      | "STRING"  | 100    | null  | null
        "revenues"  | "revenues"  | "INTEGER" | null   | null  | null
        "since"     | "custSince" | "DATE"    | null   | null  | null
        "employees" | "employees" | "STRING"  | 10     | null  | null

    }

    def "Delete a model attribute successfully"() {
        setup:
        createAttributes()
        reloadModel()

        when:
        Attribute attribute = model.attributes.find { it.name == name }
        Long id = attribute.id
        restTemplate.delete("/model/${model.id}/attribute/${id}")
        reloadModel()
        attribute = model.attributes.find { it.id == id }

        then:
        attribute == null

        where:
        name        | _
        "name"      | _
        "revenues"  | _
        "since"     | _
        "employees" | _
    }

    def "List model attributes successfully"() {
        setup:
        createAttributes()
        reloadModel()

        when:
        List result = restTemplate.getForObject("/model/${model.id}/attribute", List)

        then:
        result != null
        result.size() == attributeList.size()
        model.attributes.each { attr ->
            assert result.any {
                it.id == attr.id &&
                        it.name == attr.name &&
                        it.type == attr.type.toString() &&
                        it.length == attr.length &&
                        it.scale == attr.scale &&
                        it.precision == attr.precision
            }
        }
    }

    def "Get a model attributes by id successfully"() {
        setup:
        createAttributes()
        reloadModel()

        when:
        Attribute attribute = model.attributes.find { it.name == "name" }
        Map result = restTemplate.getForObject("/model/${model.id}/attribute/${attribute.id}", Map)

        then:
        result != null
        result.name == "name"
        result.type == "STRING"
        result.length == 50
        result.scale == null
        result.precision == null
    }

}
