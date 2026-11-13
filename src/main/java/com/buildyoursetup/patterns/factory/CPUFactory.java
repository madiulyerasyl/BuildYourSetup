package com.buildyoursetup.patterns.factory;

import com.buildyoursetup.model.CPU;
import com.buildyoursetup.model.Component;

public class CPUFactory extends ComponentFactory {

    @Override
    public CPU createComponent(
            String name,
            String brand,
            double price
    ) {
        return new CPU(name, brand, price, "AM5", 6);
    }
}
