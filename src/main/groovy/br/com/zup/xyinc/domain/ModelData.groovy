package br.com.zup.xyinc.domain

import br.com.zup.xyinc.jpa.JpaJsonConverter
import com.fasterxml.jackson.annotation.JsonIgnore

import javax.persistence.Column
import javax.persistence.Convert
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.ManyToOne
import javax.persistence.Version

/**
 * Linhas de dados de um modelo
 */
@Entity
class ModelData {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(updatable = false, nullable = false)
    private Long id

    @Version
    private Long version

    @ManyToOne(optional = false)
    private Model model

    @Convert(converter = JpaJsonConverter)
    @Column(columnDefinition = "CLOB")
    private Map<String, Object> data

    ModelData() {}

    ModelData(Long id, Long version, Model model, Map<String, Object> data) {
        this.id = id
        this.version = version
        this.model = model
        this.data = data
    }

    Long getId() {
        return id
    }

    void setId(Long id) {
        this.id = id
    }

    @JsonIgnore
    Long getVersion() {
        return version
    }

    void setVersion(Long version) {
        this.version = version
    }

    @JsonIgnore
    Model getModel() {
        return model
    }

    void setModel(Model model) {
        this.model = model
    }

    Map<String, Object> getData() {
        return data
    }

    void setData(Map<String, Object> data) {
        this.data = data
    }

}
