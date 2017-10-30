package br.com.zup.xyinc.controller

import br.com.zup.xyinc.domain.Attribute
import br.com.zup.xyinc.domain.Model
import br.com.zup.xyinc.domain.ModelData
import br.com.zup.xyinc.repository.AttributeRepository
import br.com.zup.xyinc.repository.ModelDataRepository
import br.com.zup.xyinc.repository.ModelRepository
import br.com.zup.xyinc.service.ModelService
import com.fasterxml.jackson.core.JsonGenerator
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import spock.lang.Specification

@SpringBootTest(webEnvironment =  SpringBootTest.WebEnvironment.RANDOM_PORT)
class ModelDataControllerSpec extends Specification {

    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    ModelRepository modelRepository

    @Autowired
    AttributeRepository attributeRepository

    @Autowired
    ModelDataRepository modelDataRepository

    @Autowired
    ModelService modelService

    Model model

    List<Map> attributeList = [
            [name: "name", type: "STRING", length: 50, scale: null, precision: null],
            [name: "revenues", type: "DECIMAL", length: null, scale: 2, precision: 15],
            [name: "since", type: "DATE", length: null, scale: null, precision: null],
            [name: "employees", type: "INTEGER", length: null, scale: null, precision: null]
    ]

    void createAttributes() {
        attributeList.forEach {
            Attribute attribute = new Attribute(it)
            attribute.model = model
            attributeRepository.save(attribute)
            JsonGenerator
        }
    }

    void insertSampleData() {
        modelDataRepository.save(new ModelData(
                model: model,
                data: [
                        name: "Chico do Caranguejo",
                        revenues: "352450.00",
                        since: "2017-07-14T01:29:39.126Z",
                        employees: 57
                ]
        ))
        modelDataRepository.save(new ModelData(
                model: model,
                data: [
                        name: "Coco Bambu",
                        revenues: "1427352.00",
                        since: "2017-01-02T03:00:00.000Z",
                        employees: 230
                ]
        ))
    }

    def setup() {
        model = modelRepository.save(new Model(name: "customer"))
        createAttributes()
    }

    def reloadModel() {
        model = modelRepository.findOne(model.id)
    }

    def cleanup() {
        modelService.delete(model.id)
    }

    def "Insert data to model"() {
        when:
        Map response = restTemplate.postForObject("/customer", [
                name: name,
                revenues: revenues,
                since: since,
                employees: employees
        ], Map)

        then:
        response != null
        response.id > 0
        response.name == name
        response.revenues == revenues
        response.since == since
        response.employees == employees

        when:
        ModelData modelData = modelDataRepository.findByModelAndId(model, response.id as Long)

        then:
        modelData.version >= 0
        modelData.data.name == name
        modelData.data.revenues == revenues
        modelData.data.since == since
        modelData.data.employees == employees

        where:
        name               | revenues   | since                      | employees
        "Mercadinho Royal" | "32500.00" | "2017-07-14T01:29:39.126Z" | 15
    }

    def "Update model's data"() {
        setup:
        insertSampleData()
        List<ModelData> data = modelDataRepository.findAllByModel(model)

        when:
        restTemplate.put("/customer/${data[index].id}", [
                name: name,
                revenues: revenues,
                since: since,
                employees: employees
        ])
        ModelData modelData = modelDataRepository.findByModelAndId(model, data[index].id)

        then:
        modelData.version > data[index].version
        modelData.data.name == name
        modelData.data.revenues == revenues
        modelData.data.since == since
        modelData.data.employees == employees

        where:
        index | name               | revenues   | since                      | employees
        0     | "Mercadinho Royal" | "32500.00" | "2017-02-07T01:29:39.126Z" | 300
    }

    def "Delete from model"() {
        setup:
        insertSampleData()
        List<ModelData> data = modelDataRepository.findAllByModel(model)

        when:
        restTemplate.delete("/customer/${data[index].id}")
        ModelData modelData = modelDataRepository.findByModelAndId(model, data[index].id)

        then:
        modelData == null

        where:
        index | _
        0     | _
    }

    def "List data from model"() {
        setup:
        insertSampleData()
        List<ModelData> data = modelDataRepository.findAllByModel(model)

        when:
        List<Map> customers = restTemplate.getForObject("/customer", List)

        then:
        customers.size() == data.size()
        data.each { modelData ->
            assert customers.any {
                it.id == modelData.id &&
                it.name == modelData.data.name &&
                it.revenues == modelData.data.revenues &&
                it.since == modelData.data.since &&
                it.employees == modelData.data.employees
            }
        }
    }

    def "Get one item from model"() {
        setup:
        insertSampleData()
        List<ModelData> data = modelDataRepository.findAllByModel(model)

        when:
        Map customer = restTemplate.getForObject("/customer/${data[0].id}", Map)

        then:
        customer != null
        customer.id == data[0].id
        customer.name == data[0].data.name
        customer.revenues == data[0].data.revenues
        customer.since == data[0].data.since
        customer.employees == data[0].data.employees
    }

    def "List data from missing model"() {
        when:
        ResponseEntity<Map> response = restTemplate.getForEntity("/xyz123", Map)

        then:
        response.statusCode == HttpStatus.BAD_REQUEST
    }
}
