package com.buildyoursetup.patterns.strategy;

import com.buildyoursetup.model.*;
import com.buildyoursetup.patterns.builder.PCBuildBuilder;

import java.util.Comparator;
import java.util.List;

public class BudgetStrategy implements BuildStrategy {

    @Override
    public PCBuild recommend(
            String buildName,
            List<CPU> cpus,
            List<GPU> gpus,
            List<RAM> rams
    ) {
        CPU cpu = cpus.stream()
                .min(Comparator.comparingDouble(CPU::getPrice))
                .orElseThrow(() ->
                        new IllegalArgumentException("No CPUs available"));

        GPU gpu = gpus.stream()
                .min(Comparator.comparingDouble(GPU::getPrice))
                .orElseThrow(() ->
                        new IllegalArgumentException("No GPUs available"));

        RAM ram = rams.stream()
                .min(Comparator.comparingDouble(RAM::getPrice))
                .orElseThrow(() ->
                        new IllegalArgumentException("No RAM available"));

        return new PCBuildBuilder(buildName)
                .withCPU(cpu)
                .withGPU(gpu)
                .withRAM(ram)
                .build();
    }

    @Override
    public String getStrategyName() {
        return "Budget Strategy";
    }
}
