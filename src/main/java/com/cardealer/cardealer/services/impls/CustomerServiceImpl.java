package com.cardealer.cardealer.services.impls;

import com.cardealer.cardealer.dtos.CustomerDto;
import com.cardealer.cardealer.dtos.CustomerSeedDto;
import com.cardealer.cardealer.dtos.CustomerTotalSalesDto;
import com.cardealer.cardealer.entities.Customer;
import com.cardealer.cardealer.entities.Part;
import com.cardealer.cardealer.entities.Sale;
import com.cardealer.cardealer.repositories.CustomerRepository;
import com.cardealer.cardealer.services.CustomerService;
import com.cardealer.cardealer.utils.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final ModelMapper modelMapper;
    private final CustomerRepository customerRepository;
    private final ValidationUtil validationUtil;

    public CustomerServiceImpl(ModelMapper modelMapper, CustomerRepository customerRepository,
                               ValidationUtil validationUtil) {
        this.modelMapper = modelMapper;
        this.customerRepository = customerRepository;
        this.validationUtil = validationUtil;
    }

    @Override
    @Transactional
    public void seedCustomers(CustomerSeedDto[] dtos) {
        if (this.customerRepository.count() != 0) {
            return;
        }

        Arrays.stream(dtos)
                .forEach(dto -> {
                    if (this.validationUtil.isValid(dto)) {
                        Customer customer = this.modelMapper
                                .map(dto, Customer.class);

                        String date = dto.getBirthDate();
                        LocalDateTime birthDate = parseDate(date);
                        customer.setBirthDate(birthDate);

                        this.customerRepository.saveAndFlush(customer);
                    } else {
                        this.validationUtil.getViolations(dto)
                                .stream()
                                .map(ConstraintViolation::getMessage)
                                .forEach(System.out::println);
                    }
                });
    }

    @Override
    public Customer getRandomCustomer() {
        Random random = new Random();
        long randomId = random.nextInt((int) this.customerRepository.count()) + 1;
        return this.customerRepository.getOne(randomId);
    }

    @Override
    public Set<CustomerDto> getAllCustomers() {
        Set<Customer> customers = this.customerRepository.getAllCustomers();
        Set<CustomerDto> dtos = new HashSet<>();
        for (Customer customer : customers) {
            CustomerDto dto = this.modelMapper
                    .map(customer, CustomerDto.class);
            dtos.add(dto);
        }

        return dtos;
    }

    @Override
    public List<CustomerTotalSalesDto> getAllCustomerWithCars() {
        List<Customer> customers = this.customerRepository.findAllBySalesIsNotNull();
        List<CustomerTotalSalesDto> dtos = new ArrayList<>();
        for (Customer customer : customers) {
            CustomerTotalSalesDto dto = new CustomerTotalSalesDto();
            dto.setFullName(customer.getName());
            dto.setBoughtCars(customer.getSales().size());
            boolean contains = false;
            for (CustomerTotalSalesDto totalSalesDto : dtos) {
                if (totalSalesDto.getFullName().equals(dto.getFullName())) {
                    contains = true;
                    break;
                }
            }
            double total = 0;
            for (Sale sale : customer.getSales()) {
                for (Part part : sale.getCar().getParts()) {
                    String result = this.getResult(part);
                    double partPrice = Double.parseDouble(result);
                    total += partPrice;
                }
            }
            String result = String.format("%.2f", total);
            String finalResult = this.getRidOfComma(result);
            dto.setSpentMoney(Double.parseDouble(finalResult));

            if (contains) {
                continue;
            } else {
                dtos.add(dto);
            }
        }
        return dtos;
    }

    private String getRidOfComma(String result) {
        if (result.contains(",")) {
            String s = result.substring(0, result.indexOf(","));
            String s1 = result.substring(result.indexOf(",") + 1);
            return s + "." + s1;
        } else {
            return result;
        }
    }

    private String getResult(Part part) {
        DecimalFormat formatter = new DecimalFormat("#.##");
        String str = formatter.format(part.getPrice());
        if (str.contains(",")) {
            String s = str.substring(0, str.indexOf(","));
            String s1 = str.substring(str.indexOf(",") + 1);
            return s + "." + s1;
        } else {
            return str;
        }
    }

    private LocalDateTime parseDate(String date) {
        return LocalDateTime.parse(date,
                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
    }
}
