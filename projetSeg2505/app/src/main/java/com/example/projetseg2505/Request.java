package com.example.projetseg2505;

import java.util.*;

public class Request {

    private String id;
    private String associatedService;
    private List<String> requiredInfos;


    public Request () {
    }

    public Request (String id, String service, List<String> infos) {
        this.id = id;
        this.associatedService = service;
        this.requiredInfos = infos;
    }

    public String getId () {
        return this.id;
    }

    public String getAssociatedService () {
        return this.associatedService;
    }

    public void setAssociatedService (String service) {
        this.associatedService = service;
    }

    public List<String> getRequiredInfos () {
        return this.requiredInfos;
    }

    public void setAssociatedService (List<String> infos) {
        this.requiredInfos = infos;
    }


}
