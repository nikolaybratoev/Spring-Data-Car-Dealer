package com.cardealer.cardealer.services.impls;

import com.cardealer.cardealer.dtos.SupplierDto;
import com.cardealer.cardealer.dtos.SupplierSeedDto;
import com.cardealer.cardealer.entities.Part;
import com.cardealer.cardealer.entities.Supplier;
import com.cardealer.cardealer.repositories.PartRepository;
import com.cardealer.cardealer.repositories.SupplierRepository;
import com.cardealer.cardealer.services.PartService;
import com.cardealer.cardealer.services.SupplierService;
import com.cardealer.cardealer.utils.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class SupplierServiceImpl implements SupplierService {

    private final SupplierRepository supplierRepository;
    private final ValidationUtil validationUtil;
    private final ModelMapper modelMapper;

    @Autowired
    public SupplierServiceImpl(SupplierRepository supplierRepository, ValidationUtil validationUtil,
                               ModelMapper modelMapper) {
        this.supplierRepository = supplierRepository;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional
    public void seedSuppliers(SupplierSeedDto[] dtos) {
        if (this.supplierRepository.count() != 0) {
            return;
        }

        Arrays.stream(dtos)
                .forEach(dto -> {
                    if (this.validationUtil.isValid(dto)) {
                        Supplier supplier = this.modelMapper
                                .map(dto, Supplier.class);

                        if (dto.isImporter()) {
                            supplier.setImported(true);
                        } else {
                            supplier.setImported(false);
                        }

                        this.supplierRepository.saveAndFlush(supplier);
                    } else {
                        this.validationUtil.getViolations(dto)
                                .stream()
                                .map(ConstraintViolation::getMessage)
                                .forEach(System.out::println);
                    }
                });
    }

    @Override
    public List<Supplier> getRandomSuppliers() {
        Random random = new Random();
        int counter = random.nextInt(3) + 1;
        List<Supplier> list = new ArrayList<>();
        for (int i = 0; i < counter; i++) {
            long id = random.nextInt((int) this.supplierRepository.count()) + 1;
            list.add(this.supplierRepository.getOne(id));
        }
        return list;
    }

    @Override
    public Supplier getRandomSupplier() {
        Random random = new Random();
        long id = random.nextInt((int) this.supplierRepository.count()) + 1;
        return this.supplierRepository.getOne(id);
    }

    @Override
    public List<SupplierDto> getAllSuppliersThatImportParts(boolean value) {
        List<Supplier> suppliers = this.supplierRepository.findAllByImportedIs(value);
        List<SupplierDto> dtos = new ArrayList<>();

        for (Supplier supplier : suppliers) {
            SupplierDto dto = this.modelMapper
                    .map(supplier, SupplierDto.class);

            dto.setPartsCount(supplier.getParts().size());
            dtos.add(dto);
        }

        return dtos.stream()
                .sorted((a, b) -> b.getPartsCount().compareTo(a.getPartsCount()))
                .collect(Collectors.toList());
    }
}
