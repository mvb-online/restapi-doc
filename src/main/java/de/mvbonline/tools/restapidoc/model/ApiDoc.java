package de.mvbonline.tools.restapidoc.model;

import java.util.ArrayList;
import java.util.List;

public class ApiDoc {
	private String name;
	private String description;
	private List<ApiMethodDoc> methods;

	public ApiDoc() {
		this.methods = new ArrayList<ApiMethodDoc>();
	}

	public void addMethod(ApiMethodDoc apiMethod) {
		this.methods.add(apiMethod);
	}

	public String getDescription() {
		return description;
	}

	public List<ApiMethodDoc> getMethods() {
		return methods;
	}

	public String getName() {
		return name;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setMethods(List<ApiMethodDoc> methods) {
		this.methods = methods;
	}

	public void setName(String name) {
		this.name = name;
	}

}
