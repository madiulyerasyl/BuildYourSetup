package com.buildyoursetup.patterns.decorator;

public class WarrantyDecorator extends BuildDecorator {

    public WarrantyDecorator(BuildOption wrappedBuild) {
        super(wrappedBuild);
    }

    @Override
    public String getDescription() {
        return super.getDescription() + " + Extended Warranty";
    }

    @Override
    public double getTotalPrice() {
        return super.getTotalPrice() + 25000;
    }
}
