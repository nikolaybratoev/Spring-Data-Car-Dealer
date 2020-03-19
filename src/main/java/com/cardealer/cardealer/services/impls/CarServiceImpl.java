package com.cardealer.cardealer.services.impls;

import com.cardealer.cardealer.dtos.*;
import com.cardealer.cardealer.entities.Car;
import com.cardealer.cardealer.entities.Part;
import com.cardealer.cardealer.repositories.CarRepository;
import com.cardealer.cardealer.services.CarService;
import com.cardealer.cardealer.services.PartService;
import com.cardealer.cardealer.utils.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class CarServiceImpl implements CarService {

    private final CarRepository carRepository;
    private final ValidationUtil validationUtil;
    private final ModelMapper modelMapper;
    private final PartService partService;

    @Autowired
    public CarServiceImpl(CarRepository carRepository, ValidationUtil validationUtil,
                          ModelMapper modelMapper, PartService partService) {
        this.carRepository = carRepository;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
        this.partService = partService;
    }

    @Override
    public void seedCars(CarSeedDto[] dtos) {
        if (this.carRepository.count() != 0) {
            return;
        }

        Arrays.stream(dtos)
                .forEach(dto -> {
                    if (this.validationUtil.isValid(dto)) {
                        Car car = this.modelMapper
                                .map(dto, Car.class);

                        Set<PartDto> partDtos = this.partService.getRandomParts();
                        Set<Part> parts = new HashSet<>();
                        for (PartDto partDto : partDtos) {
                            Part part = this.modelMapper
                                    .map(partDto, Part.class);
                            parts.add(part);
                        }
                        car.setParts(parts);

                        this.carRepository.saveAndFlush(car);
                    } else {
                        this.validationUtil.getViolations(dto)
                                .stream()
                                .map(ConstraintViolation::getMessage)
                                .forEach(System.out::println);
                    }
                });
    }

    @Override
    public Car getRandomCar() {
        Random random = new Random();
        long randomId = random.nextInt((int) this.carRepository.count()) + 1;
        return this.carRepository.getOne(randomId);
    }

    @Override
    public List<CarDto> getAllCarsFromMake(String make) {
        List<Car> cars = this.carRepository.findAllByMakeOrderByModelAsc(make);
        List<CarDto> dtos = new ArrayList<>();
        cars.forEach(car -> {
            CarDto dto = this.modelMapper
                    .map(car, CarDto.class);
            dtos.add(dto);
        });

        return dtos.stream()
                .sorted((a, b) -> b.getTravelledDistance().compareTo(a.getTravelledDistance()))
                .collect(Collectors.toList());
    }

    @Override
    public List<CarPartsDto> getAllCarsAndTheirParts() {
        List<Car> cars = this.carRepository.findAllByIdIsNotNull();
        List<CarPartsDto> dtos = new ArrayList<>();

        for (Car car : cars) {
            CarPartsDto carPartsDto = this.modelMapper
                    .map(car, CarPartsDto.class);

            List<PartCarDto> partDtos = new ArrayList<>();
            car.getParts()
                    .forEach(part -> {
                        PartCarDto partCarDto = this.modelMapper
                                .map(part, PartCarDto.class);
                        partDtos.add(partCarDto);
                    });

            carPartsDto.setParts(partDtos);
            dtos.add(carPartsDto);
        }

        return dtos;
    }
}
