package br.com.zup.xyinc.controller

import br.com.zup.xyinc.domain.Attribute
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
@RequestMapping("/model/{id}/attribute")
class ModelAttributeController {
    @Autowired
    ModelService modelService

    @RequestMapping(method = RequestMethod.GET)
    List<Attribute> list(@PathVariable Long id) {
        return modelService.getAttributes(id)
    }

    @RequestMapping(path = "/{attributeId}", method = RequestMethod.GET)
    Attribute get(@PathVariable Long id, @PathVariable Long attributeId) {
        return modelService.getAttribute(id, attributeId)
    }

    @RequestMapping(method = RequestMethod.POST)
    ResponseEntity<Attribute> create(@PathVariable Long id, @RequestBody Attribute attribute) {
        Attribute result = modelService.addAttribute(id, attribute)
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(result.id)
                .toUri()

        return ResponseEntity.created(location).body(result)
    }

    @RequestMapping(path = "/{attributeId}", method = RequestMethod.PUT)
    Attribute update(@PathVariable Long id, @PathVariable Long attributeId, @RequestBody Attribute attribute) {
        attribute.id = attributeId
        return modelService.updateAttribute(id, attribute)
    }

    @RequestMapping(path = "/{attributeId}", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    void delete(@PathVariable Long id, @PathVariable Long attributeId) {
        modelService.deleteAttribute(id, attributeId)
    }
}
