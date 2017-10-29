package br.com.zup.xyinc.repository

import br.com.zup.xyinc.domain.Model
import org.springframework.data.repository.CrudRepository

interface ModelRepository extends CrudRepository<Model, Long> {

    Model findByName(String name)
}
