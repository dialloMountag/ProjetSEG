package com.example.projetseg2505;

public class Requirement <T> {
    private String name;
    private T information;

    //No argument constructor for firebase to be happy
    public Requirement(){}

    //Constructor
    public Requirement(String name){
        this.name = name;
        this.information = (T) "test";
    }

    //Getter for the name
    public String getName(){
        return this.name;
    }

    //Setter for the name
    public void setName(String newName){
        this.name = newName;
    }

    //Getter for the information
    public T getInformation(){
        return information;
    }

    //Setter for the information
    public void setInformation(T newInfo){
        this.information = newInfo;
    }

}
