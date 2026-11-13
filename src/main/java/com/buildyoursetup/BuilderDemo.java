package com.buildyoursetup;

import com.buildyoursetup.model.*;
import com.buildyoursetup.patterns.builder.PCBuildBuilder;

public class BuilderDemo {

    public static void main(String[] args) {

        CPU cpu = new CPU(
                "Ryzen 5 7600", "AMD",
                95000, "AM5", 6
        );

        GPU gpu = new GPU(
                "RTX 4060", "NVIDIA",
                180000, 8
        );

        RAM ram = new RAM(
                "Vengeance", "Corsair",
                45000, 32, "DDR5"
        );

        PCBuild gamingPC = new PCBuildBuilder("Gaming PC")
                .withCPU(cpu)
                .withGPU(gpu)
                .withRAM(ram)
                .build();

        System.out.println("=== BUILDER PATTERN DEMO ===");
        System.out.println("Build name: " + gamingPC.getName());
        System.out.println("CPU: " + gamingPC.getCpu());
        System.out.println("GPU: " + gamingPC.getGpu());
        System.out.println("RAM: " + gamingPC.getRam());
        System.out.println("Total price: "
                + gamingPC.calculateTotalPrice() + " KZT");

        try {
            new PCBuildBuilder("Incomplete PC")
                    .withCPU(cpu)
                    .build();
        } catch (IllegalStateException e) {
            System.out.println("Validation: " + e.getMessage());
        }
    }
}
