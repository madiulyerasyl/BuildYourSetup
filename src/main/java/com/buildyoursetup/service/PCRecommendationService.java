package com.buildyoursetup.service;

import com.buildyoursetup.model.*;
import com.buildyoursetup.patterns.strategy.BuildStrategy;

import java.util.List;
import java.util.Objects;

public class PCRecommendationService {

    private BuildStrategy strategy;

    public PCRecommendationService(BuildStrategy strategy) {
        setStrategy(strategy);
    }

    public void setStrategy(BuildStrategy strategy) {
        this.strategy = Objects.requireNonNull(
                strategy, "Strategy cannot be null"
        );
    }

    public String getCurrentStrategyName() {
        return strategy.getStrategyName();
    }

    public PCBuild recommend(
            String name,
            List<CPU> cpus,
            List<GPU> gpus,
            List<RAM> rams
    ) {
        return strategy.recommend(name, cpus, gpus, rams);
    }
}
