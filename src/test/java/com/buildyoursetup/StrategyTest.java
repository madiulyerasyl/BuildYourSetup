package com.buildyoursetup;

import com.buildyoursetup.model.*;
import com.buildyoursetup.patterns.strategy.*;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class StrategyTest {

    @Test
    void budgetStrategySelectsCheapestComponents() {

        List<CPU> cpus = List.of(
                new CPU("CPU A", "AMD", 50000, "AM5", 6),
                new CPU("CPU B", "AMD", 100000, "AM5", 8)
        );

        List<GPU> gpus = List.of(
                new GPU("GPU A", "NVIDIA", 100000, 8),
                new GPU("GPU B", "NVIDIA", 200000, 12)
        );

        List<RAM> rams = List.of(
                new RAM("RAM A", "Corsair", 20000, 16, "DDR5"),
                new RAM("RAM B", "Corsair", 40000, 32, "DDR5")
        );

        BuildStrategy strategy = new BudgetStrategy();

        PCBuild build = strategy.recommend(
                "Test PC", cpus, gpus, rams
        );

        assertEquals(170000, build.calculateTotalPrice());
        assertEquals("CPU A", build.getCpu().getName());
        assertEquals("GPU A", build.getGpu().getName());
        assertEquals("RAM A", build.getRam().getName());
    }
}
