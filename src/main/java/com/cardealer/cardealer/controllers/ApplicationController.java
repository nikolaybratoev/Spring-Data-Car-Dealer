package com.cardealer.cardealer.controllers;

import com.cardealer.cardealer.dtos.*;
import com.cardealer.cardealer.services.*;
import com.cardealer.cardealer.utils.FileIOUtil;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Controller;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.cardealer.cardealer.constants.GlobalConstraints.*;

@Controller
public class ApplicationController implements CommandLineRunner {

    private final Gson gson;
    private final SupplierService supplierService;
    private final PartService partService;
    private final CarService carService;
    private final CustomerService customerService;
    private final SaleService saleService;
    private final BufferedReader reader;
    private final FileIOUtil fileIOUtil;

    @Autowired
    public ApplicationController(Gson gson, SupplierService supplierService,
                                 PartService partService, CarService carService,
                                 CustomerService customerService, SaleService saleService,
                                 BufferedReader reader, FileIOUtil fileIOUtil) {
        this.gson = gson;
        this.supplierService = supplierService;
        this.partService = partService;
        this.carService = carService;
        this.customerService = customerService;
        this.saleService = saleService;
        this.reader = reader;
        this.fileIOUtil = fileIOUtil;
    }

    @Override
    public void run(String... args) throws Exception {

        System.out.println("Enter number:");
        System.out.println("For info press 0.");
        String input = this.reader.readLine();

        boolean exit = false;

        while (true) {
            switch (input) {
                case "0":
                    this.information();
                    break;
                case "1":
                    this.getAllCustomers();
                    break;
                case "2":
                    this.getAllCarsFromMake();
                    break;
                case "3":
                    this.getAllSuppliers();
                    break;
                case "4":
                    this.getAllCarsWithTheirParts();
                    break;
                case "5":
                    this.getAllCustomersThatBoughtCars();
                    break;
                case "6":
                    break;
                case "8":
                    this.seedData();
                    break;
                case "9":
                    exit = true;
                    break;
                default:
                    System.out.println("invalid number.");
                    System.out.println("For info press 0.");
            }

            if (exit) {
                System.out.println("Bye!");
                break;
            }

            System.out.println("Enter number:");
            System.out.println("For info press 0.");
            input = this.reader.readLine();
        }
    }

    private void getAllCustomersThatBoughtCars() throws IOException {
        List<CustomerTotalSalesDto> dtos = this.customerService.getAllCustomerWithCars();

        String json = this.gson.toJson(dtos);
        this.fileIOUtil.write(json, FIFTH_TASK_FILE_PATH);
    }

    private void getAllCarsWithTheirParts() throws IOException {
        List<CarPartsDto> dtos = this.carService.getAllCarsAndTheirParts();

        String json = this.gson.toJson(dtos);
        this.fileIOUtil.write(json, FOURTH_TASK_FILE_PATH);
    }

    private void getAllSuppliers() throws IOException {
        List<SupplierDto> dtos = this.supplierService.getAllSuppliersThatImportParts(true);

        String json = this.gson.toJson(dtos);
        this.fileIOUtil.write(json, THIRD_TASK_FILE_PATH);
    }

    private void getAllCarsFromMake() throws IOException {
        List<CarDto> cars = this.carService.getAllCarsFromMake("Toyota");

        String json = this.gson.toJson(cars);
        this.fileIOUtil.write(json, SECOND_TASK_FILE_PATH);
    }

    private void getAllCustomers() throws IOException {
        Set<CustomerDto> dtoSet = this.customerService.getAllCustomers();
        List<CustomerDto> dtoList = dtoSet.stream()
                .sorted((a, b) -> Boolean.compare(a.isYoungDriver(), b.isYoungDriver()))
                .collect(Collectors.toList());

        String json = this.gson.toJson(dtoList);
        this.fileIOUtil.write(json, FIRST_TASK_FILE_PATH);
    }

    private void information() {
        System.out.println("To seed the data from json file press 8.");
        System.out.println("To get first query press 1.");
        System.out.println("To get second query press 2.");
        System.out.println("To get third query press 3.");
        System.out.println("To get fourth query press 4.");
        System.out.println("To get fifth query press 5.");
        System.out.println("To exit the application press 9.");
    }

    private void seedData() throws FileNotFoundException {
        this.seedSuppliers();
        this.seedParts();
        this.seedCars();
        this.seedCustomers();
        this.seedSales();
    }

    private void seedSales() {
        this.saleService.seedSales();
    }

    private void seedCustomers() throws FileNotFoundException {
        CustomerSeedDto[] dtos = this.gson
                .fromJson(new FileReader(CUSTOMERS_FILE_PATH),
                        CustomerSeedDto[].class);

        this.customerService.seedCustomers(dtos);
    }

    private void seedCars() throws FileNotFoundException {
        CarSeedDto[] dtos = this.gson
                .fromJson(new FileReader(CARS_FILE_PATH),
                        CarSeedDto[].class);

        this.carService.seedCars(dtos);
    }

    private void seedParts() throws FileNotFoundException {
        PartsSeedDto[] dtos = this.gson
                .fromJson(new FileReader(PARTS_FILE_PATH),
                        PartsSeedDto[].class);

        this.partService.seedParts(dtos);
    }

    private void seedSuppliers() throws FileNotFoundException {
        SupplierSeedDto[] dtos = this.gson
                .fromJson(new FileReader(SUPPLIERS_FILE_PATH),
                        SupplierSeedDto[].class);

        this.supplierService.seedSuppliers(dtos);
    }
}
