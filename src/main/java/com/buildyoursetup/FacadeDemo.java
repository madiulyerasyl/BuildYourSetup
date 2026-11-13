package com.buildyoursetup;

import com.buildyoursetup.model.*;
import com.buildyoursetup.patterns.builder.PCBuildBuilder;
import com.buildyoursetup.patterns.facade.*;

public class FacadeDemo {

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

        PCBuild pc = new PCBuildBuilder("Gaming PC")
                .withCPU(cpu)
                .withGPU(gpu)
                .withRAM(ram)
                .build();

        PCValidationFacade facade = new PCValidationFacade();

        ValidationResult result = facade.validateBuild(pc);

        System.out.println("=== FACADE PATTERN DEMO ===");
        System.out.println("Build: " + pc.getName());
        System.out.println(result);

        RAM unsupportedRam = new RAM(
                "Old Memory", "Generic",
                10000, 8, "DDR3"
        );

        PCBuild invalidPC = new PCBuildBuilder("Invalid PC")
                .withCPU(cpu)
                .withGPU(gpu)
                .withRAM(unsupportedRam)
                .build();

        ValidationResult invalidResult =
                facade.validateBuild(invalidPC);

        System.out.println("\n=== INVALID BUILD ===");
        System.out.println(invalidResult);
    }
}
