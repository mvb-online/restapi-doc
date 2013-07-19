package de.mvbonline.tools.restapidoc.model;

public class ApiParamDoc {
    private String name;
    private String description;
    private String type;
    private boolean required;

    public ApiParamDoc() {
        super();
    }

    public ApiParamDoc(String name, String description, String type, boolean required) {
        super();
        this.name = name;
        this.description = description;
        this.type = type;
        this.required = required;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public boolean isRequired() {
        return required;
    }

}
