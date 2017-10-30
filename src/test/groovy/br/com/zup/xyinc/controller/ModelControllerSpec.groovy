package br.com.zup.xyinc.controller

import br.com.zup.xyinc.domain.Model
import br.com.zup.xyinc.repository.ModelRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import spock.lang.Specification

@SpringBootTest(webEnvironment =  SpringBootTest.WebEnvironment.RANDOM_PORT)
class ModelControllerSpec extends Specification {

    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    ModelRepository modelRepository

    def "create model successfully"() {
        when:
        Map response = restTemplate.postForObject("/model", [ name: name ], Map)

        then:
        response != null
        response.id > 0
        response.name == name

        when:
        Model created = modelRepository.findOne(response.id as Long)

        then:
        created.name == name
        created.version >= 0
        created.attributes.size() == 0

        cleanup:
        modelRepository.delete(created.id)

        where:
        name       | _
        "customer" | _
    }

    def "update model successfully"() {
        setup:
        Model saved = modelRepository.save(new Model(name: "customer"))

        when:
        restTemplate.put("/model/${saved.id}", [ name: name ])
        Model changed = modelRepository.findOne(saved.id)

        then:
        changed.name == name
        changed.version == saved.version + 1

        cleanup:
        modelRepository.delete(saved.id)

        where:
        name       | _
        "supplier" | _
    }

    def "delete model successfully"() {
        setup:
        Model saved = modelRepository.save(new Model(name: "customer"))

        when:
        restTemplate.delete("/model/${saved.id}")
        Model deleted = modelRepository.findOne(saved.id)

        then:
        deleted == null
    }

    def "list models successfully"() {
        setup:
        Model model1 = modelRepository.save(new Model(name: "customer"))
        Model model2 = modelRepository.save(new Model(name: "supplier"))

        when:
        List response = restTemplate.getForObject("/model", List)

        then:
        response != null
        response.size() == 2
        response.any { it.id == model1.id && it.name == model1.name }
        response.any { it.id == model2.id && it.name == model2.name }

        cleanup:
        modelRepository.delete(model1)
        modelRepository.delete(model2)
    }

    def "get model by id successfully"() {
        setup:
        Model model = modelRepository.save(new Model(name: "customer"))

        when:
        Map response = restTemplate.getForObject("/model/${model.id}", Map)

        then:
        response != null
        response.id == model.id
        response.name == model.name

        cleanup:
        modelRepository.delete(model)
    }

    def "insert a model with same name as existing"() {
        setup:
        Model saved = modelRepository.save(new Model(name: "customer"))

        when:
        ResponseEntity<Map> response = restTemplate.postForEntity("/model", [name: "customer"], Map)

        then:
        response.statusCode == HttpStatus.UNPROCESSABLE_ENTITY

        cleanup:
        modelRepository.delete(saved.id)
    }

    def "insert a model with invalid model name"() {
        when:
        ResponseEntity<Map> response = restTemplate.postForEntity("/model", [name: name], Map)

        then:
        response.statusCode == HttpStatus.UNPROCESSABLE_ENTITY

        where:
        name         | _
        null         | _
        ""           | _
        "model"      | _
        "/xyz#^"     | _
        "com espaco" | _
    }

    def "get missing model"() {
        when:
        ResponseEntity<Map> response = restTemplate.getForEntity("/model/999", Map)

        then:
        response.statusCode == HttpStatus.NOT_FOUND
    }

    def "delete missing model"() {
        when:
        ResponseEntity<Map> response = restTemplate.exchange("/model/999", HttpMethod.DELETE, null, Map)

        then:
        response.statusCode == HttpStatus.NOT_FOUND
    }

    def "update missing model"() {
        when:
        ResponseEntity<Map> response = restTemplate.exchange("/model/999", HttpMethod.PUT, new HttpEntity<Map>([ name: "tryagain" ]), Map)

        then:
        response.statusCode == HttpStatus.NOT_FOUND
    }
}
