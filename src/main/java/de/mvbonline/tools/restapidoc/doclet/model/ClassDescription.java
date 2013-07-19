package de.mvbonline.tools.restapidoc.doclet.model;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Model for javadoc comments on a class.
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType
public class ClassDescription extends ElementDescription implements Serializable {

    @XmlAttribute
    private String fullClassName;

    @XmlElement
    private List<ElementDescription> properties;

    @XmlElement
    private List<MethodDescription> methods;

    public void addMethod(MethodDescription doc) {
        if (this.methods == null) {
            this.methods = new LinkedList<MethodDescription>();
        }
        this.methods.add(doc);
    }

    public void addProperty(ElementDescription doc) {
        if (this.properties == null) {
            this.properties = new LinkedList<ElementDescription>();
        }
        this.properties.add(doc);
    }

    public String getFullClassName() {
        return fullClassName;
    }

    public List<MethodDescription> getMethods() {
        return methods;
    }

    public List<ElementDescription> getProperties() {
        return properties;
    }

    public void setFullClassName(String fullClassName) {
        this.fullClassName = fullClassName;
    }

    public void setMethods(List<MethodDescription> methods) {
        this.methods = methods;
    }

    public void setProperties(List<ElementDescription> properties) {
        this.properties = properties;
    }
}
