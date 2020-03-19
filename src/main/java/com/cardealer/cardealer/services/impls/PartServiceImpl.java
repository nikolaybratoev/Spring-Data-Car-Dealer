package com.cardealer.cardealer.services.impls;

import com.cardealer.cardealer.dtos.PartDto;
import com.cardealer.cardealer.dtos.PartsSeedDto;
import com.cardealer.cardealer.entities.Part;
import com.cardealer.cardealer.repositories.PartRepository;
import com.cardealer.cardealer.services.PartService;
import com.cardealer.cardealer.services.SupplierService;
import com.cardealer.cardealer.utils.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@Service
@Transactional
public class PartServiceImpl implements PartService {

    private final PartRepository partRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final SupplierService supplierService;

    public PartServiceImpl(PartRepository partRepository, ModelMapper modelMapper,
                           ValidationUtil validationUtil, SupplierService supplierService) {
        this.partRepository = partRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.supplierService = supplierService;
    }

    @Override
    public void seedParts(PartsSeedDto[] dtos) {
        if (this.partRepository.count() != 0) {
            return;
        }

        Arrays.stream(dtos)
                .forEach(dto -> {
                    if (this.validationUtil.isValid(dto)) {
                        Part part = this.modelMapper
                                .map(dto, Part.class);

                        Random random = new Random();
                        int randomNum = random.nextInt(2);
                        if (randomNum == 1) {
                            part.setSupplier(this.supplierService.getRandomSupplier());
                        }

                        this.partRepository.saveAndFlush(part);
                    } else {
                        this.validationUtil.getViolations(dto)
                                .stream()
                                .map(ConstraintViolation::getMessage)
                                .forEach(System.out::println);
                    }
                });
    }

    @Override
    public Set<PartDto> getRandomParts() {
        Random random = new Random();
        int randomCounter = ThreadLocalRandom.current().nextInt(10, 20 + 1);
        Set<Part> set = new HashSet<>();
        for (int i = 0; i < randomCounter; i++) {
            long id = random.nextInt((int) this.partRepository.count()) + 1;
            set.add(this.partRepository.getOne(id));
        }

        Set<PartDto> partDtoSet = new HashSet<>();
        for (Part part : set) {
            PartDto partDto = this.modelMapper
                    .map(part, PartDto.class);
            partDtoSet.add(partDto);
        }
        return partDtoSet;
    }
}
