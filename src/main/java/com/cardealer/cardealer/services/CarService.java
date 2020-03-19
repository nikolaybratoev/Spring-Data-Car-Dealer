package com.cardealer.cardealer.services;

import com.cardealer.cardealer.dtos.CarDto;
import com.cardealer.cardealer.dtos.CarPartsDto;
import com.cardealer.cardealer.dtos.CarSeedDto;
import com.cardealer.cardealer.entities.Car;

import java.util.List;

public interface CarService {

    void seedCars(CarSeedDto[] dtos);

    Car getRandomCar();

    List<CarDto> getAllCarsFromMake(String make);

    List<CarPartsDto> getAllCarsAndTheirParts();
}
