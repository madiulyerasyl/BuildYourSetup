package com.buildyoursetup;

import com.buildyoursetup.model.*;
import com.buildyoursetup.patterns.builder.PCBuildBuilder;
import com.buildyoursetup.patterns.factory.*;

public class FactoryDemo {

    public static void main(String[] args) {

        ComponentFactory cpuFactory = new CPUFactory();
        ComponentFactory gpuFactory = new GPUFactory();
        ComponentFactory ramFactory = new RAMFactory();

        CPU cpu = (CPU) cpuFactory.createComponent(
                "Ryzen 5 7600", "AMD", 95000
        );

        GPU gpu = (GPU) gpuFactory.createComponent(
                "RTX 4060", "NVIDIA", 180000
        );

        RAM ram = (RAM) ramFactory.createComponent(
                "Vengeance", "Corsair", 45000
        );

        PCBuild gamingPC = new PCBuildBuilder("Gaming PC")
                .withCPU(cpu)
                .withGPU(gpu)
                .withRAM(ram)
                .build();

        System.out.println("=== FACTORY METHOD DEMO ===");
        System.out.println("CPU: " + cpu);
        System.out.println("GPU: " + gpu);
        System.out.println("RAM: " + ram);
        System.out.println("Build: " + gamingPC.getName());
        System.out.println("Total: "
                + gamingPC.calculateTotalPrice() + " KZT");
    }
}
