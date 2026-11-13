package com.buildyoursetup.patterns.factory;

import com.buildyoursetup.model.RAM;

public class RAMFactory extends ComponentFactory {

    @Override
    public RAM createComponent(
            String name,
            String brand,
            double price
    ) {
        return new RAM(name, brand, price, 32, "DDR5");
    }
}
