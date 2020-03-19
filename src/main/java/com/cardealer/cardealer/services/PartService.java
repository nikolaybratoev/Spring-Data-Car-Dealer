package com.cardealer.cardealer.services;

import com.cardealer.cardealer.dtos.PartDto;
import com.cardealer.cardealer.dtos.PartsSeedDto;

import java.util.Set;

public interface PartService {

    void seedParts(PartsSeedDto[] dtos);

    Set<PartDto> getRandomParts();
}
