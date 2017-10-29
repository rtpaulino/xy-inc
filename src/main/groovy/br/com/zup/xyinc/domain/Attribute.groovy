package br.com.zup.xyinc.domain

import br.com.zup.xyinc.exception.BlankValidationException
import br.com.zup.xyinc.util.Converters
import br.com.zup.xyinc.util.Validators
import com.fasterxml.jackson.annotation.JsonIgnore
import org.apache.commons.lang3.StringUtils

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.ManyToOne
import javax.persistence.Table
import javax.persistence.Transient
import javax.persistence.UniqueConstraint
import javax.persistence.Version

/**
 * Tabela para cadastro dos atributos de um modelo.
 * Foi decidido não utilizar herança nesse cenário por tornar o código muito mais simples, especialmente no caso
 * em que é necessário mudar o "type" do atributo.
 */
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = ["model_id", "name"]))
class Attribute {

    static enum Type {
        DATE,
        DECIMAL,
        INTEGER,
        STRING
    }

    static interface Meta<T> {
        void validate()
        T parse(Object value)
    }

    @Transient
    private Map<Attribute.Type, Attribute.Meta<?>> meta = [
            (Attribute.Type.DATE): new Attribute.Meta<Date>() {
                @Override
                void validate() {}

                @Override
                Date parse(Object value) {
                    return Converters.toDate(value)
                }
            },
            (Attribute.Type.DECIMAL): new Attribute.Meta<BigDecimal>() {
                @Override
                void validate() {
                    Validators.nonNegative("scale", scale)
                    Validators.nonNegative("precision", precision)
                }

                @Override
                BigDecimal parse(Object value) {
                    return Converters.toDecimal(value, precision, scale)
                }
            },
            (Attribute.Type.INTEGER): new Attribute.Meta<Long>() {
                @Override
                void validate() {}

                @Override
                Long parse(Object value) {
                    return Converters.toLong(value)
                }
            },
            (Attribute.Type.STRING): new Attribute.Meta<String>() {
                @Override
                void validate() {
                    Validators.positive("length", length)
                }

                @Override
                String parse(Object value) {
                    return Converters.toString(value, length)
                }
            }
    ]


    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(updatable = false, nullable = false)
    private Long id

    @Version
    private Long version

    @ManyToOne(optional = false)
    private Model model

    @Column(nullable = false)
    @Enumerated(EnumType.STRING) // using string is easier when we query database directly
    private Attribute.Type type

    @Column(nullable = false)
    private String name

    @Column
    private Integer length

    @Column
    private Integer scale

    @Column
    private Integer precision

    Attribute() {}

    Attribute(Long id, Long version, Model model, Type type, String name, Integer length, Integer scale, Integer precision) {
        this.id = id
        this.version = version
        this.model = model
        this.type = type
        this.name = name
        this.length = length
        this.scale = scale
        this.precision = precision
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

    String getName() {
        return name
    }

    void setName(String name) {
        this.name = name
    }

    Type getType() {
        return type
    }

    void setType(Type type) {
        this.type = type
    }

    Integer getLength() {
        return length
    }

    void setLength(Integer length) {
        this.length = length
    }

    Integer getScale() {
        return scale
    }

    void setScale(Integer scale) {
        this.scale = scale
    }

    Integer getPrecision() {
        return precision
    }

    void setPrecision(Integer precision) {
        this.precision = precision
    }

    void validate() {
        if (StringUtils.isBlank(name)) {
            throw new BlankValidationException("name")
        }
        if (type == null) {
            throw new BlankValidationException("type")
        }
        meta[type].validate()
    }

    /**
     * Copia as propriedades básicas do objeto (não inclui id nem relacionamentos)
     * @param other
     */
    Attribute copyFrom(Attribute other) {
        ["type", "name", "length", "scale", "precision"].each {
            owner[it] = other[it]
        }
        return this
    }

    @JsonIgnore
    Object getValueFrom(Map data) {
        if (!data.containsKey(name)) {
            return null
        }
        return meta[type].parse(data.get(name))
    }

}
