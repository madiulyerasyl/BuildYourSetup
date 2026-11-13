package com.buildyoursetup.patterns.observer;

import com.buildyoursetup.model.PCBuild;

public class PriceObserver implements BuildObserver {

    private double lastCalculatedPrice;

    @Override
    public void onBuildChanged(PCBuild build) {

        lastCalculatedPrice = build.calculateTotalPrice();

        System.out.println(
                "[PRICE UPDATE] Total: "
                        + lastCalculatedPrice + " KZT"
        );
    }

    public double getLastCalculatedPrice() {
        return lastCalculatedPrice;
    }
}
