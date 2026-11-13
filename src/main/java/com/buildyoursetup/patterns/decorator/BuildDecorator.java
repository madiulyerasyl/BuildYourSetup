package com.buildyoursetup.patterns.decorator;

import java.util.Objects;

public abstract class BuildDecorator implements BuildOption {

    protected final BuildOption wrappedBuild;

    public BuildDecorator(BuildOption wrappedBuild) {
        this.wrappedBuild = Objects.requireNonNull(wrappedBuild);
    }

    @Override
    public String getDescription() {
        return wrappedBuild.getDescription();
    }

    @Override
    public double getTotalPrice() {
        return wrappedBuild.getTotalPrice();
    }
}
