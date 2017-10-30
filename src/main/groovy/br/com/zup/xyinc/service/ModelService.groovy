package br.com.zup.xyinc.service

import br.com.zup.xyinc.domain.Attribute
import br.com.zup.xyinc.domain.Model
import br.com.zup.xyinc.exception.AlreadyExistsException
import br.com.zup.xyinc.exception.InvalidValueValidationException
import br.com.zup.xyinc.exception.NotFoundException
import br.com.zup.xyinc.exception.ReservedInvalidValueValidationException
import br.com.zup.xyinc.repository.AttributeRepository
import br.com.zup.xyinc.repository.ModelDataRepository
import br.com.zup.xyinc.repository.ModelRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

import javax.transaction.Transactional
import java.util.regex.Pattern

@Service
@Transactional
class ModelService {

    public static final String MODEL = "Model"
    public static final String ATTRIBUTE = "Model Attribute"

    public static final List<String> RESERVED_MODELS = ["model"]
    public static final List<String> RESERVED_ATTRIBUTES = ["id", "version"]

    // This identifier should work in most databases and is good enough for URL's
    public static final Pattern VALID_IDENTIFIER_REGEXP = Pattern.compile(/^[a-zA-Z_][a-zA-Z0-9_]*$/)

    @Autowired
    private ModelRepository modelRepository

    @Autowired
    private AttributeRepository attributeRepository

    @Autowired
    private ModelDataRepository modelDataRepository

    List<Model> findAll() {
        return modelRepository.findAll().toList()
    }

    Model findOne(Long id) {
        Model model = modelRepository.findOne(id)
        if (model) {
            return model
        } else {
            throw new NotFoundException(MODEL, id)
        }
    }

    Model findByName(String name) {
        return modelRepository.findByName(name)
    }

    private void checkExistingModel(Model model) {
        if (findByName(model.name)) {
            throw new AlreadyExistsException(MODEL, "name", model.name)
        }
    }

    private void checkValidModelName(String name) {
        if (RESERVED_MODELS.contains(name)) {
            throw new ReservedInvalidValueValidationException("name", name)
        }
        if (!VALID_IDENTIFIER_REGEXP.matcher(name).matches()) {
            throw new InvalidValueValidationException("name", name)
        }
    }

    Model create(Model model) {
        model.validate()
        checkValidModelName(model.name)
        checkExistingModel(model)
        return modelRepository.save(new Model().copyFrom(model))
    }

    Model update(Model updated) {
        Model current = findOne(updated.id)
        if (current.name == updated.name) {
            return current
        }

        updated.validate()
        checkValidModelName(updated.name)
        checkExistingModel(updated)

        current.copyFrom(updated)

        return modelRepository.save(current)
    }

    void delete(Long id) {
        Model model = findOne(id)

        // delete all data first
        modelDataRepository.deleteByModel(model)

        // now I can delete the model
        modelRepository.delete(model)
    }

    List<Attribute> getAttributes(Long id) {
        Model model = findOne(id)
        return model.attributes
    }

    private Attribute getAttributeInternal(Model model, Long attributeId) {
        Attribute attribute = model.attributes.find { it.id == attributeId }
        if (attribute) {
            return attribute
        } else {
            throw new NotFoundException(ATTRIBUTE, attributeId)
        }
    }
    Attribute getAttribute(Long id, Long attributeId) {
        Model model = findOne(id)
        return getAttributeInternal(model, attributeId)
    }

    private void checkExistingAttribute(Model model, Attribute attribute) {
        if (model.attributes.any { it.name == attribute.name }) {
            throw new AlreadyExistsException(ATTRIBUTE, "name", attribute.name)
        }
    }
    private void checkValidAttributeName(String name) {
        if (RESERVED_ATTRIBUTES.contains(name)) {
            throw new ReservedInvalidValueValidationException("name", name)
        }
        if (!VALID_IDENTIFIER_REGEXP.matcher(name).matches()) {
            throw new InvalidValueValidationException("name", name)
        }
    }

    Attribute addAttribute(Long id, Attribute attribute) {
        Model model = findOne(id)
        attribute.validate()
        checkValidAttributeName(attribute.name)
        checkExistingAttribute(model, attribute)
        Attribute created = new Attribute().copyFrom(attribute)
        created.model = model
        return attributeRepository.save(created)
    }

    Attribute updateAttribute(Long id, Attribute updated) {
        Model model = findOne(id)

        Attribute current = getAttributeInternal(model, updated.id)
        updated.model = model
        updated.validate()

        if (current.name != updated.name) {
            checkExistingAttribute(model, updated)
            checkValidAttributeName(updated.name)
        }

        current.copyFrom(updated)

        return attributeRepository.save(current)
    }

    void deleteAttribute(Long id, Long attributeId) {
        Model model = findOne(id)
        getAttributeInternal(model, attributeId) // validate if the attribute exist
        attributeRepository.delete(attributeId)
    }

}
