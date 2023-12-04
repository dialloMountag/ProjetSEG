package com.example.projetseg2505;

import java.util.*;

public class Location {

    private String name;
    private String city;
    private String address;
    private List<Service> offeredServices;
    private String openingTime;
    private String closingTime;
    private String userName;

    // Void Succursale
    public Location() {
    }

    public Location(String name,String city, String address, List<Service> offeredServices, String openingTime, String closingTime, String userName) {
        this.name = name;
        this.city = city;
        this.address = address;
        this.offeredServices = offeredServices;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
        this.userName = userName;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return this.city;
    }

    public void setCity(String city) {
        this.city = this.city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<Service> getOfferedServices() {
        return offeredServices;
    }

    public void setOfferedServices(List<Service> offeredServices) {
        this.offeredServices = offeredServices;
    }

    public String getOpeningTime() {
        return openingTime;
    }

    public void setOpeningTime(String openingTime) {
        this.openingTime = openingTime;
    }

    public String getClosingTime() {
        return closingTime;
    }

    public void setClosingTime(String closingTime) {
        this.closingTime = closingTime;
    }

    public String getUserName(){
        return userName;
    }

    @Override
    public String toString() {
        return "Location{" +
                "name='" + name + '\'' +
                "city='" + city + '\'' +
                ", address='" + address + '\'' +
                ", openingTime='" + openingTime + '\'' +
                ", closingTime='" + closingTime + '\'' +
                ", userName='" + userName + '\'' +
                '}';
    }
}
