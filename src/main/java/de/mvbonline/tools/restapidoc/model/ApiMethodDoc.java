package de.mvbonline.tools.restapidoc.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.springframework.web.bind.annotation.RequestMethod;

public class ApiMethodDoc {
	private String path;
	private String description;
	private RequestMethod requestMethod;
	private List<String> produces;

    private List<String> consumes;

    private List<ApiParamDoc> urlparameters;
	private ApiBodyObjectDoc bodyobject;
	private ApiResponseObjectDoc response;
	private List<ApiParamDoc> queryParameters;
	public ApiMethodDoc() {
		super();
		this.urlparameters = new ArrayList<ApiParamDoc>();
	    this.queryParameters = new LinkedList<ApiParamDoc>();
	}
    public ApiBodyObjectDoc getBodyobject() {
		return bodyobject;
	}

    public List<String> getConsumes() {
		return consumes;
	}

    public String getDescription() {
		return description;
	}

	public String getPath() {
		return path;
	}

	public List<String> getProduces() {
		return produces;
	}

	public List<ApiParamDoc> getQueryParameters() {
        return queryParameters;
    }

	public RequestMethod getRequestMethod() {
        return requestMethod;
    }

	public ApiResponseObjectDoc getResponse() {
		return response;
	}

	public List<ApiParamDoc> getUrlparameters() {
		return urlparameters;
	}

	public void setBodyobject(ApiBodyObjectDoc bodyobject) {
		this.bodyobject = bodyobject;
	}

	public void setConsumes(List<String> consumes) {
		this.consumes = consumes;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public void setProduces(List<String> produces) {
		this.produces = produces;
	}

	public void setQueryParameters(List<ApiParamDoc> queryParameters) {
        this.queryParameters = queryParameters;
    }

	public void setRequestMethod(RequestMethod requestMethod) {
        this.requestMethod = requestMethod;
    }

	public void setResponse(ApiResponseObjectDoc response) {
		this.response = response;
	}

	public void setUrlparameters(List<ApiParamDoc> urlparameters) {
		this.urlparameters = urlparameters;
	}
}
