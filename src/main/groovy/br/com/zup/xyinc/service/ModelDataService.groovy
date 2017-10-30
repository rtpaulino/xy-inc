package br.com.zup.xyinc.service

import br.com.zup.xyinc.domain.Model
import br.com.zup.xyinc.domain.ModelData
import br.com.zup.xyinc.exception.BadRequestException
import br.com.zup.xyinc.exception.NotFoundException
import br.com.zup.xyinc.repository.ModelDataRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

import javax.transaction.Transactional

@Service
@Transactional
class ModelDataService {

    @Autowired
    ModelService modelService

    @Autowired
    ModelDataRepository modelDataRepository

    private Model getModel(String modelName) {
        Model model = modelService.findByName(modelName)
        if (model) {
            return model
        } else {
            throw new BadRequestException("There is no ${modelName}")
        }
    }

    /**
     * Transforma os dados de acordo com o modelo definido
     * @param modelData
     * @return
     */
    private Map transform(ModelData modelData, Map data = null) {
        if (data == null) {
            data = modelData.data
        }
        return modelData.model.attributes.collectEntries { attribute ->
            [(attribute.name): attribute.getValueFrom(data)]
        }
    }

    /**
     * Transforma os dados para o formato esperado pelo usuário
     * @param modelData
     * @return
     */
    private Map toOutput(ModelData modelData) {
        Map result = transform(modelData)
        result.id = modelData.id
        return result
    }

    List<Map> findAll(String modelName) {
        Model model = getModel(modelName)
        List<ModelData> results = modelDataRepository.findAllByModel(model)
        return results.collect { toOutput(it) }
    }

    private ModelData findOneInternal(Model model, Long id) {
        ModelData modelData = modelDataRepository.findByModelAndId(model, id)
        if (modelData) {
            return modelData
        } else {
            throw new NotFoundException(model.name, id)
        }
    }

    Map findOne(String modelName, Long id) {
        Model model = getModel(modelName)
        return toOutput(findOneInternal(model, id))
    }

    Map create(String modelName, Map input) {
        Model model = getModel(modelName)
        ModelData modelData = new ModelData()
        modelData.model = model
        modelData.data = transform(modelData, input)

        return toOutput(modelDataRepository.save(modelData))
    }

    Map update(String modelName, Map updated) {
        Model model = getModel(modelName)
        ModelData current = findOneInternal(model, updated.id)
        current.data = transform(current, updated)
        return toOutput(modelDataRepository.save(current))
    }

    Map delete(String modelName, Long id) {
        Model model = getModel(modelName)
        ModelData current = findOneInternal(model, id)
        modelDataRepository.delete(current)
    }

}
