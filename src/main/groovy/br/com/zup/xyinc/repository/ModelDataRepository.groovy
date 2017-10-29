package br.com.zup.xyinc.repository

import br.com.zup.xyinc.domain.Model
import br.com.zup.xyinc.domain.ModelData
import org.springframework.data.repository.CrudRepository

interface ModelDataRepository extends CrudRepository<ModelData, Long> {
    ModelData findByModelAndId(Model model, Long id)
    List<ModelData> findAllByModel(Model model)
}
