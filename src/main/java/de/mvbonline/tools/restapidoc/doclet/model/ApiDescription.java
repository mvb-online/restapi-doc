package de.mvbonline.tools.restapidoc.doclet.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Model for storing javadoc comments.
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class ApiDescription implements Serializable {

    @XmlElement()
    private List<ClassDescription> classes;

    public void addClass(ClassDescription clazz) {
        if (this.classes == null) {
            this.classes = new LinkedList<ClassDescription>();
        }
        this.classes.add(clazz);
    }

    public void addClasses(Collection<ClassDescription> clazz) {
        if (this.classes == null) {
            this.classes = new LinkedList<ClassDescription>();
        }
        this.classes.addAll(clazz);
    }

    public List<ClassDescription> getClasses() {
        return classes;
    }

    public void setClasses(List<ClassDescription> classes) {
        this.classes = classes;
    }
}
