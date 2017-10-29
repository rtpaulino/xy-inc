package br.com.zup.xyinc.controller

import br.com.zup.xyinc.domain.Model
import br.com.zup.xyinc.service.ModelService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.support.ServletUriComponentsBuilder

@RestController
@RequestMapping("/model")
class ModelController {

    @Autowired
    ModelService modelService

    @RequestMapping(method = RequestMethod.GET)
    List<Model> list() {
        return modelService.findAll()
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    Model get(@PathVariable Long id) {
        return modelService.findOne(id)
    }


    @RequestMapping(method = RequestMethod.POST)
    ResponseEntity<Model> create(@RequestBody Model input) {
        Model result = modelService.create(input)
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(result.id)
                .toUri()

        return ResponseEntity.created(location).body(result)
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.PUT)
    Model update(@PathVariable Long id, @RequestBody Model input) {
        input.id = id
        return modelService.update(input)
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    void delete(@PathVariable Long id) {
        modelService.delete(id)
    }

}
