package com.example.projetseg2505;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Service {

    private String serviceName;
    private List<Requirement<String>> requiredInfo;

    public Service() {
    }

    public Service(String serviceName, String requirements) {
        this.serviceName = serviceName;
        this.requiredInfo = new ArrayList<>(); //Initialize the list to put the requirements in
        //get all the requirement names separated by commas and put them into a List<String>
        String[] wordsArray = requirements.split(",");
        List<String> requirementNames = Arrays.asList(wordsArray);
        //for every requirement listed, create a new Requirement object with corresponding name
        for(String name : requirementNames){
            Requirement<String> x = new Requirement<>(name);
            //test if the element x is non null
            if(x != null){
                requiredInfo.add(x);
            }
        }
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public List<Requirement<String>> getRequiredInfo() {
        return requiredInfo;
    }

    public void setRequiredInfo(List<Requirement<String>> requiredInfo) {
        this.requiredInfo = requiredInfo;
    }

    @Override
    public String toString() {
        return "Service{" +
                "serviceName='" + serviceName + '\'' +
                ", requiredInfo=" + requiredInfo +
                '}';
    }
}
