package de.mvbonline.tools.restapidoc.doclet.model;

/**
 * Represents Annotation Key/Value pairs
 */
public class AnnotationKeyValue {

    private String name;

    private String value;

    public AnnotationKeyValue() {
        super();
    }

    public AnnotationKeyValue(String name, String value) {
        this();
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
