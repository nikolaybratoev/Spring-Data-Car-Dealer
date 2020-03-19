package com.cardealer.cardealer.repositories;

import com.cardealer.cardealer.entities.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {

    List<Car> findAllByMakeOrderByModelAsc(String make);

    List<Car> findAllByIdIsNotNull();
}
