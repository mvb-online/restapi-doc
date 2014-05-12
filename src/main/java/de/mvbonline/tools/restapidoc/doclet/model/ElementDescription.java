package de.mvbonline.tools.restapidoc.doclet.model;

import java.io.Serializable;

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

    @XmlAttribute
    private String onixCodelist;

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

    public ElementDescription(String name, String description, String onixCodelist) {
        this(name, description);
        this.onixCodelist = onixCodelist;
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

    public String getOnixCodelist() {
        return onixCodelist;
    }

    public void setOnixCodelist(String onixCodelist) {
        this.onixCodelist = onixCodelist;
    }
}
