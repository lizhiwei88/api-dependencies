package com.github.lizhiwei88.dependencies.agent.entity;


/**
 * @author Zhiwei Li
 */
public class ApiDependencies {

    private String targetService;

    private String method;

    private String uri;

    public String getTargetService() {
        return targetService;
    }

    public void setTargetService(String targetService) {
        this.targetService = targetService;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
