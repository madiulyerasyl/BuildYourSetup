package com.buildyoursetup;

import com.buildyoursetup.model.*;
import com.buildyoursetup.patterns.strategy.*;
import com.buildyoursetup.service.PCRecommendationService;

import java.util.List;

public class StrategyDemo {

    public static void main(String[] args) {

        List<CPU> cpus = List.of(
                new CPU("Ryzen 5 7600", "AMD", 95000, "AM5", 6),
                new CPU("Ryzen 7 7700", "AMD", 145000, "AM5", 8),
                new CPU("Ryzen 9 7900X", "AMD", 220000, "AM5", 12)
        );

        List<GPU> gpus = List.of(
                new GPU("RTX 4060", "NVIDIA", 180000, 8),
                new GPU("RTX 4070", "NVIDIA", 300000, 12),
                new GPU("RTX 4080", "NVIDIA", 550000, 16)
        );

        List<RAM> rams = List.of(
                new RAM("Vengeance 16GB", "Corsair", 25000, 16, "DDR5"),
                new RAM("Vengeance 32GB", "Corsair", 45000, 32, "DDR5"),
                new RAM("Vengeance 64GB", "Corsair", 90000, 64, "DDR5")
        );

        PCRecommendationService service =
                new PCRecommendationService(new BudgetStrategy());

        PCBuild budgetPC = service.recommend(
                "Budget Gaming PC", cpus, gpus, rams
        );

        printBuild(service.getCurrentStrategyName(), budgetPC);

        service.setStrategy(new PerformanceStrategy());

        PCBuild performancePC = service.recommend(
                "Performance Gaming PC", cpus, gpus, rams
        );

        printBuild(service.getCurrentStrategyName(), performancePC);
    }

    private static void printBuild(String strategy, PCBuild build) {

        System.out.println("\n=== " + strategy + " ===");
        System.out.println("Build: " + build.getName());
        System.out.println("CPU: " + build.getCpu());
        System.out.println("GPU: " + build.getGpu());
        System.out.println("RAM: " + build.getRam());
        System.out.println("Total: "
                + build.calculateTotalPrice() + " KZT");
    }
}
