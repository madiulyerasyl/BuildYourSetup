package com.buildyoursetup.patterns.factory;

import com.buildyoursetup.model.Component;

public abstract class ComponentFactory {

    public abstract Component createComponent(
            String name,
            String brand,
            double price
    );
}
