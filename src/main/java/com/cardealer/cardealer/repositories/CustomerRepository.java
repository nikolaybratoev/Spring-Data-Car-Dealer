package com.cardealer.cardealer.repositories;

import com.cardealer.cardealer.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    @Query("SELECT c FROM Customer AS c ORDER BY c.birthDate ASC")
    Set<Customer> getAllCustomers();

    List<Customer> findAllBySalesIsNotNull();
}
