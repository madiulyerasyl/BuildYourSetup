package com.buildyoursetup.patterns.decorator;

import com.buildyoursetup.model.PCBuild;
import java.util.Objects;

public class BasicBuild implements BuildOption {

    private final PCBuild build;

    public BasicBuild(PCBuild build) {
        this.build = Objects.requireNonNull(build);
    }

    @Override
    public String getDescription() {
        return build.getName();
    }

    @Override
    public double getTotalPrice() {
        return build.calculateTotalPrice();
    }
}
