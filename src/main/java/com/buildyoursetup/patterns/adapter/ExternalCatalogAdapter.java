package com.buildyoursetup.patterns.adapter;

import com.buildyoursetup.external.ExternalCatalog;
import com.buildyoursetup.external.ExternalProduct;
import com.buildyoursetup.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ExternalCatalogAdapter implements ComponentCatalog {

    private final ExternalCatalog externalCatalog;

    public ExternalCatalogAdapter(ExternalCatalog externalCatalog) {
        this.externalCatalog = Objects.requireNonNull(externalCatalog);
    }

    @Override
    public List<Component> getComponents() {

        List<Component> components = new ArrayList<>();

        for (ExternalProduct product : externalCatalog.fetchProducts()) {

            Component component = convertProduct(product);

            components.add(component);
        }

        return components;
    }

    private Component convertProduct(ExternalProduct product) {

        String name = product.getProductTitle();
        String brand = product.getManufacturer();
        double price = product.getCost();

        switch (product.getCategory()) {

            case "PROCESSOR":
                return new CPU(name, brand, price, "AM5", 6);

            case "GRAPHICS_CARD":
                return new GPU(name, brand, price, 8);

            case "MEMORY":
                return new RAM(name, brand, price, 32, "DDR5");

            default:
                throw new IllegalArgumentException(
                        "Unsupported category: "
                                + product.getCategory()
                );
        }
    }
}
