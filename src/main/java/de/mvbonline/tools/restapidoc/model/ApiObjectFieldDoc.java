package de.mvbonline.tools.restapidoc.model;


import java.util.List;

public class ApiObjectFieldDoc {
	private String name;
	private String type;
	private boolean multiple;
	private String description;
    private List<String> annotations;

	public ApiObjectFieldDoc() {
		super();
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

	public boolean isMultiple() {
		return multiple;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setMultiple(boolean multiple) {
		this.multiple = multiple;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setType(String type) {
		this.type = type;
	}

    public List<String> getAnnotations() {
        return annotations;
    }

    public void setAnnotations(List<String> annotations) {
        this.annotations = annotations;
    }
}
