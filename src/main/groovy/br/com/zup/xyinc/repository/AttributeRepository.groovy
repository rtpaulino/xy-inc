package br.com.zup.xyinc.repository

import br.com.zup.xyinc.domain.Attribute
import org.springframework.data.repository.CrudRepository

interface AttributeRepository extends CrudRepository<Attribute, Long> {
}
