package com.cardealer.cardealer.services;

import com.cardealer.cardealer.dtos.CustomerDto;
import com.cardealer.cardealer.dtos.CustomerSeedDto;
import com.cardealer.cardealer.dtos.CustomerTotalSalesDto;
import com.cardealer.cardealer.entities.Customer;

import java.util.List;
import java.util.Set;

public interface CustomerService {

    void seedCustomers(CustomerSeedDto[] dtos);

    Customer getRandomCustomer();

    Set<CustomerDto> getAllCustomers();

    List<CustomerTotalSalesDto> getAllCustomerWithCars();
}
