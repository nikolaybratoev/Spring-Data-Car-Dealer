package com.cardealer.cardealer.services;

import com.cardealer.cardealer.dtos.SupplierDto;
import com.cardealer.cardealer.dtos.SupplierSeedDto;
import com.cardealer.cardealer.entities.Supplier;

import java.util.List;

public interface SupplierService {

    void seedSuppliers(SupplierSeedDto[] dtos);

    List<Supplier> getRandomSuppliers();

    Supplier getRandomSupplier();

    List<SupplierDto> getAllSuppliersThatImportParts(boolean value);
}
