package com.buildyoursetup.patterns.facade;

public class ValidationResult {

    private final boolean complete;
    private final boolean compatible;
    private final double totalPrice;

    public ValidationResult(
            boolean complete,
            boolean compatible,
            double totalPrice
    ) {
        this.complete = complete;
        this.compatible = compatible;
        this.totalPrice = totalPrice;
    }

    public boolean isComplete() {
        return complete;
    }

    public boolean isCompatible() {
        return compatible;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public boolean isValid() {
        return complete && compatible;
    }

    @Override
    public String toString() {
        return "Complete: " + complete
                + "\nCompatible: " + compatible
                + "\nTotal Price: " + totalPrice + " KZT"
                + "\nValid: " + isValid();
    }
}
