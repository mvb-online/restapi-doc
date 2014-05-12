package de.mvbonline.tools.restapidoc.doclet.model;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

/**
 * All Annotations on Fields are put in this Class.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType
public class Annotation {

    @XmlAttribute
    private String name;

    @XmlElement
    private List<AnnotationKeyValue> keyValues;

    public Annotation() {
        super();
    }

    public Annotation(String name) {
        this();
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<AnnotationKeyValue> getKeyValues() {
        if (keyValues == null) {
            keyValues = new ArrayList<AnnotationKeyValue>();
        }
        return keyValues;
    }

    public void setKeyValues(List<AnnotationKeyValue> keyValues) {
        this.keyValues = keyValues;
    }
}
