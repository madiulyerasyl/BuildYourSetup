package com.buildyoursetup.patterns.facade;

import com.buildyoursetup.model.PCBuild;
import com.buildyoursetup.service.validation.*;

public class PCValidationFacade {

    private final CompletenessChecker completenessChecker;
    private final CompatibilityChecker compatibilityChecker;
    private final PriceCalculator priceCalculator;

    public PCValidationFacade() {
        this.completenessChecker = new CompletenessChecker();
        this.compatibilityChecker = new CompatibilityChecker();
        this.priceCalculator = new PriceCalculator();
    }

    public ValidationResult validateBuild(PCBuild build) {

        boolean complete =
                completenessChecker.isComplete(build);

        boolean compatible =
                compatibilityChecker.isCompatible(build);

        double price =
                priceCalculator.calculate(build);

        return new ValidationResult(
                complete,
                compatible,
                price
        );
    }
}

