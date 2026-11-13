package com.buildyoursetup.patterns.factory;

import com.buildyoursetup.model.GPU;

public class GPUFactory extends ComponentFactory {

    @Override
    public GPU createComponent(
            String name,
            String brand,
            double price
    ) {
        return new GPU(name, brand, price, 8);
    }
}
