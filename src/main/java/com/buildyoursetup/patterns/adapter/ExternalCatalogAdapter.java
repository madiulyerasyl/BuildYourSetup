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

        switch (product.getProductId()) {

            case "CPU-001":
                return new CPU(name, brand, price, "AM5", 6);

            case "CPU-002":
                return new CPU(name, brand, price, "AM5", 8);

            case "GPU-001":
                return new GPU(name, brand, price, 8);

            case "GPU-002":
                return new GPU(name, brand, price, 12);

            case "RAM-001":
                return new RAM(name, brand, price, 32, "DDR5");

            case "RAM-002":
                return new RAM(name, brand, price, 64, "DDR5");

            default:
                throw new IllegalArgumentException(
                        "Unknown product: " + product.getProductId()
                );
        }
    }

}
