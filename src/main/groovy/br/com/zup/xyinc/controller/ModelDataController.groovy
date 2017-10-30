package br.com.zup.xyinc.controller

import br.com.zup.xyinc.service.ModelDataService
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.servlet.support.ServletUriComponentsBuilder

@org.springframework.web.bind.annotation.RestController
@RequestMapping("/{modelName}")
class ModelDataController {

    @Autowired
    ModelDataService modelDataService

    @Autowired
    ObjectMapper mapper

    @RequestMapping(method = RequestMethod.GET)
    List<Map> list(@PathVariable modelName) {
        return modelDataService.findAll(modelName)
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    Map get(@PathVariable modelName, @PathVariable Long id) {
        return modelDataService.findOne(modelName, id)
    }

    @RequestMapping(method = RequestMethod.POST)
    ResponseEntity<Map> create(@PathVariable modelName, @RequestBody Map input) {
        Map result = modelDataService.create(modelName, input)
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(result.id)
                .toUri()

        return ResponseEntity.created(location).body(result)
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.PUT)
    Map update(@PathVariable modelName, @PathVariable Long id, @RequestBody Map input) {
        input.id = id
        return modelDataService.update(modelName, input)
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    void delete(@PathVariable modelName, @PathVariable Long id) {
        modelDataService.delete(modelName, id)
    }
}
