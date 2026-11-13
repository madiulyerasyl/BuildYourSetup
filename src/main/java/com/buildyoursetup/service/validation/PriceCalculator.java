package com.buildyoursetup.service.validation;

import com.buildyoursetup.model.PCBuild;

public class PriceCalculator {

    public double calculate(PCBuild build) {

        if (build == null) {
            throw new IllegalArgumentException(
                    "PC Build cannot be null"
            );
        }

        return build.calculateTotalPrice();
    }
}
