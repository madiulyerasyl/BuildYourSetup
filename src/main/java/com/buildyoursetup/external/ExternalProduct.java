package com.buildyoursetup.external;

public class ExternalProduct {

    private final String productId;
    private final String productTitle;
    private final String manufacturer;
    private final double cost;
    private final String category;

    public ExternalProduct(
            String productId,
            String productTitle,
            String manufacturer,
            double cost,
            String category
    ) {
        this.productId = productId;
        this.productTitle = productTitle;
        this.manufacturer = manufacturer;
        this.cost = cost;
        this.category = category;
    }

    public String getProductId() {
        return productId;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public double getCost() {
        return cost;
    }

    public String getCategory() {
        return category;
    }
}
