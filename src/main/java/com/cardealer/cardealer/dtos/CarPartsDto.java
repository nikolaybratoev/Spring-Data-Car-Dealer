package com.cardealer.cardealer.dtos;

import com.google.gson.annotations.Expose;

import java.util.List;

public class CarPartsDto {

    @Expose
    private String make;

    @Expose
    private String model;

    @Expose
    private Long travelledDistance;

    @Expose
    List<PartCarDto> parts;

    public CarPartsDto() {
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Long getTravelledDistance() {
        return travelledDistance;
    }

    public void setTravelledDistance(Long travelledDistance) {
        this.travelledDistance = travelledDistance;
    }

    public List<PartCarDto> getParts() {
        return parts;
    }

    public void setParts(List<PartCarDto> parts) {
        this.parts = parts;
    }
}
