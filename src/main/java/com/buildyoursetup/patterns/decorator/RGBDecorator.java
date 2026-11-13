package com.buildyoursetup.patterns.decorator;

public class RGBDecorator extends BuildDecorator {

    public RGBDecorator(BuildOption wrappedBuild) {
        super(wrappedBuild);
    }

    @Override
    public String getDescription() {
        return super.getDescription() + " + RGB Lighting";
    }

    @Override
    public double getTotalPrice() {
        return super.getTotalPrice() + 15000;
    }
}
