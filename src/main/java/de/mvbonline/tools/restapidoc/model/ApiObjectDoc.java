package de.mvbonline.tools.restapidoc.model;

import java.util.LinkedList;
import java.util.List;

public class ApiObjectDoc {
    private String modelClass;

    private String name;

    private String description;
    private List<ApiObjectFieldDoc> fields;
    public ApiObjectDoc(String name, String description, String modelClass) {
        super();
        this.name = name;
        this.description = description;
        this.modelClass = modelClass;
    }

    public void addField(ApiObjectFieldDoc field) {
        if(fields == null) {
            fields = new LinkedList<ApiObjectFieldDoc>();
        }
        fields.add(field);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ApiObjectDoc other = (ApiObjectDoc) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        }
        else if (!name.equals(other.name))
            return false;
        return true;
    }

    public String getDescription() {
        return description;
    }

    public List<ApiObjectFieldDoc> getFields() {
        return fields;
    }

    public String getModelClass() {
        return modelClass;
    }

    public String getName() {
        return name;
    }

    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }
}
