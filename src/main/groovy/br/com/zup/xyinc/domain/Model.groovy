package br.com.zup.xyinc.domain

import br.com.zup.xyinc.util.Validators
import com.fasterxml.jackson.annotation.JsonIgnore

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.OneToMany
import javax.persistence.Version

@Entity
class Model {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(updatable = false, nullable = false)
    private Long id

    @Version
    private Long version

    @Column(nullable = false, unique = true)
    private String name

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "model")
    private List<Attribute> attributes

    Model() {
    }

    Model(String name) {
        this.name = name
    }

    Model(Long id, Long version, String name, List<Attribute> attributes) {
        this.id = id
        this.version = version
        this.name = name
        this.attributes = attributes
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

    String getName() {
        return name
    }

    void setName(String name) {
        this.name = name
    }

    // Incluído JsonIgnore, mas pode ser desejável manter os atributos ao serializar o "model".
    @JsonIgnore
    List<Attribute> getAttributes() {
        return attributes
    }

    void setAttributes(List<Attribute> attributes) {
        this.attributes = attributes
    }

    void validate() {
        Validators.notEmpty("name", name)
    }

    /**
     * Copia as propriedades básicas do objeto (não inclui id nem relacionamentos)
     * @param other
     */
    Model copyFrom(Model other) {
        name = other.name
        return this
    }
}
