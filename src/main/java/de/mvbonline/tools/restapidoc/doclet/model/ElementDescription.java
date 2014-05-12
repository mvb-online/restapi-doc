package de.mvbonline.tools.restapidoc.doclet.model;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Base Model for javadoc comments.
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType
public class ElementDescription implements Serializable {

    @XmlAttribute
    private String name;

    @XmlElement
    private List<Annotation> annotations;

    @XmlElement
    private String description;

    public ElementDescription() {
        super();
    }

    public ElementDescription(String name, String description) {
        this();
        this.name = name;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Annotation> getAnnotations() {
        return annotations;
    }

    public void setAnnotations(List<Annotation> annotations) {
        this.annotations = annotations;
    }
}
