package de.mvbonline.tools.restapidoc.model;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Defines Blacklists for Fields, Methods, Classes as well as Annotations.
 *
 * The blacklist.xml is a static configuration file, which is read during the transformation of the
 * documentation objects into the xml-representation.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class Blacklist {

    @XmlElementWrapper(name="classes")
    @XmlElement(name="class")
    private List<String> classesOnBlacklist = new ArrayList<String>();

    @XmlElementWrapper(name="methods")
    @XmlElement(name="method")
    private List<String> methodsOnBlacklist = new ArrayList<String>();

    @XmlElementWrapper(name="fields")
    @XmlElement(name="field")
    private List<String> fieldsOnBlacklist = new ArrayList<String>();

    @XmlElementWrapper(name="annotations")
    @XmlElement(name="annotation")
    private List<String> annotationsOnBlacklist = new ArrayList<String>();

    public Blacklist() {
        super();
    }

    public List<String> getClassesOnBlacklist() {
        return classesOnBlacklist;
    }

    public void setClassesOnBlacklist(List<String> classesOnBlacklist) {
        this.classesOnBlacklist = classesOnBlacklist;
    }

    public List<String> getMethodsOnBlacklist() {
        return methodsOnBlacklist;
    }

    public void setMethodsOnBlacklist(List<String> methodsOnBlacklist) {
        this.methodsOnBlacklist = methodsOnBlacklist;
    }

    public List<String> getFieldsOnBlacklist() {
        return fieldsOnBlacklist;
    }

    public void setFieldsOnBlacklist(List<String> fieldsOnBlacklist) {
        this.fieldsOnBlacklist = fieldsOnBlacklist;
    }

    public List<String> getAnnotationsOnBlacklist() {
        return annotationsOnBlacklist;
    }

    public void setAnnotationsOnBlacklist(List<String> annotationsOnBlacklist) {
        this.annotationsOnBlacklist = annotationsOnBlacklist;
    }
}
