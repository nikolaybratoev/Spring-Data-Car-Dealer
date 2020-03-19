package com.cardealer.cardealer.repositories;

import com.cardealer.cardealer.entities.Part;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PartRepository extends JpaRepository<Part, Long> {

    List<Part> findAllBySupplier_Id(Long id);
}
