package com.github.lizhiwei88.dependencies.agent.entity;


import java.util.List;

/**
 * @author Zhiwei Li
 */
public class DependenciesInfo {

    private String service;

    private List<ApiDependencies> apiDependenciesList;

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public List<ApiDependencies> getApiDependenciesList() {
        return apiDependenciesList;
    }

    public void setApiDependenciesList(List<ApiDependencies> apiDependenciesList) {
        this.apiDependenciesList = apiDependenciesList;
    }
}
